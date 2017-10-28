package fr.guiguilechat.eveonline.programs.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.guiguilechat.eveonline.model.database.apiv2.APIRoot;
import fr.guiguilechat.eveonline.model.database.apiv2.Account.EveChar;
import fr.guiguilechat.eveonline.model.database.apiv2.Char.Content;
import fr.guiguilechat.eveonline.model.database.apiv2.Char.OrderEntry;
import fr.guiguilechat.eveonline.model.database.yaml.LPOffer;
import fr.guiguilechat.eveonline.model.database.yaml.LPOffer.ItemRef;
import fr.guiguilechat.eveonline.model.database.yaml.Station;
import fr.guiguilechat.eveonline.model.database.yaml.YamlDatabase;
import fr.guiguilechat.eveonline.programs.gui.Settings.TeamDescription;
import fr.guiguilechat.eveonline.programs.gui.Settings.TeamDescription.Provision;
import fr.guiguilechat.eveonline.programs.gui.panes.EvePane;
import fr.guiguilechat.eveonline.programs.gui.panes.options.OptionPane;
import fr.guiguilechat.eveonline.programs.gui.panes.overview.OverViewPane;
import fr.guiguilechat.eveonline.programs.gui.panes.provision.ProvisionPane;
import fr.guiguilechat.eveonline.programs.settings.ISettings;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Manager extends Application implements EvePane {

	private static final Logger logger = LoggerFactory.getLogger(Manager.class);

	public static void main(String[] args) {
		int parrallelism = Runtime.getRuntime().availableProcessors() * 10;
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "" + parrallelism);

		launch(args);
	}

	@Override
	public Manager parent() {
		return this;
	}

	public Settings settings = ISettings.load(Settings.class);

	public final ObservableList<APIRoot> apis = FXCollections.observableArrayList();

	public BorderPane mainLayout = new BorderPane();

	public OverViewPane overviewPane = new OverViewPane(this);
	public ProvisionPane provisionpane = new ProvisionPane(this);
	public OptionPane optionPane = new OptionPane(this);
	TabPane tabs;
	Tab overviewtab, provisiontab, optionstab;

	public EvePane[] children = new EvePane[] { overviewPane, provisionpane, optionPane };

	@Override
	public EvePane[] subEvePanes() {
		return children;
	}

	@Override
	public boolean isShownSubPane(EvePane child) {
		Tab selected = tabs.getSelectionModel().getSelectedItem();
		return selected == overviewtab && child == overviewPane || selected == provisiontab && child == provisionpane
				|| selected == optionstab && child == optionPane;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		logger.debug("start manager");
		primaryStage.setTitle("guigui lechat manager");

		// set the tabs
		overviewtab = new Tab("overview", overviewPane);
		provisiontab = new Tab("provision", provisionpane);
		optionstab = new Tab("options", optionPane);
		tabs = new TabPane(overviewtab, provisiontab, optionstab);
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		tabs.setSide(Side.LEFT);
		tabs.getSelectionModel().selectedItemProperty().addListener((ov, old, now) -> {
			if (old != null) {
				((EvePane) old.getContent()).propagateIsShown(false);
			}
			if (now != null) {
				((EvePane) now.getContent()).propagateIsShown(true);
			}
		});
		// prevent moving out of options until we have at least one correct API.
		tabs.getSelectionModel().selectedItemProperty().addListener((obj, old, now) -> checkAPIOrSetOptionsTab());
		checkAPIOrSetOptionsTab();

		TitledPane tpDebug = new TitledPane("debug", debugPane);
		tpDebug.setExpanded(false);
		mainLayout.setCenter(tabs);
		mainLayout.setBottom(tpDebug);

		logger.debug("making scene");
		Scene scene = new Scene(mainLayout, 800, 900);
		primaryStage.setScene(scene);
		primaryStage.show();

		logger.debug("propagate apis");
		for (Entry<Integer, String> a : settings.apiKeys.entrySet()) {
			apis.add(new APIRoot(a.getKey(), a.getValue()));
		}
		propagateNewAPI(apis.toArray(new APIRoot[0]));

		logger.debug("propagate teams");
		for (String team : settings.teams.keySet()) {
			propagateNewTeam(team);
			for (String charname : settings.teams.get(team).members) {
				propagateAdd2Team(team, charname);
			}
		}
		propagateFocusedTeam(settings.focusedTeam);
		propagateStart();
		logger.debug("manager started");
		new Thread(this::precache).start();
	}

	protected void precache() {
		db().getAgents();
		db.getLocations();
		db.getStations();
		db.getMetaInfs();
		db.getLPOffers();
		db.getModules();
		db.getHulls();
		db.getBlueprints();
	}

	protected void checkAPIOrSetOptionsTab() {
		if (settings.apiKeys.isEmpty()) {
			tabs.getSelectionModel().select(optionstab);
		}
	}

	//
	// event handling
	//

	@Override
	public void onDelAPI(int key) {
		for (Iterator<APIRoot> it = apis.iterator(); it.hasNext();) {
			if (it.next().key.keyID == key) {
				it.remove();
			}
		}
	}

	@Override
	public void onDelTeam(String name) {
		cachedTeamItems.remove(name);
	}

	@Override
	public void onTeamAddSystem(String team, String systemName) {
		if (team == null) {
			return;
		}
		// for each character of the team, we get the assets of this character on
		// the given location and we add them to the team assets.
		HashMap<Integer, Long> itemsGains = new HashMap<>();
		streamTeamCharacters(team).forEach(c -> {
			Map<String, Map<Integer, Long>> charItems = getCharItems(c);
			if (charItems != null) {
				Map<Integer, Long> locItems = charItems.get(systemName);
				for (Entry<Integer, Long> e : locItems.entrySet()) {
					itemsGains.put(e.getKey(), itemsGains.getOrDefault(e.getKey(), 0l) + e.getValue());
				}
			}
		});
		Map<Integer, Long> storedItemsValues = cachedTeamItems.get(team);
		if (storedItemsValues == null) {
			storedItemsValues = itemsGains;
			cachedTeamItems.put(team, storedItemsValues);
		} else {
			for (Entry<Integer, Long> e : itemsGains.entrySet()) {
				long newval = e.getValue() + storedItemsValues.getOrDefault(e.getKey(), 0l);
				if (newval != 0) {
					storedItemsValues.put(e.getKey(), newval);
				} else {
					storedItemsValues.remove(e.getKey());
				}
			}
		}
		propagateTeamNewItems(team, itemsGains);
	}

	// external calls
	// modification of the settings
	//

	// API

	public void removeApi(int keyID) {
		settings.apiKeys.remove(keyID);
		settings.store();
		propagateDelAPI(keyID);
	}

	public APIRoot addAPI(int key, String code) {
		// first check the api
		APIRoot newapi = new APIRoot(key, code);
		if (newapi.getInfos() == null) {
			return null;
		}
		// then remove former api with same key
		String oldCode = settings.apiKeys.put(key, code);
		settings.store();

		// we can't modify the apiroot, as they have final args. so remove and add.
		if (oldCode != null) {
			apis.removeIf(ar -> ar.key.keyID == key);
		}
		apis.add(newapi);
		if (oldCode == null) {
			propagateNewAPI(newapi);
		}
		return newapi;
	}

	public APIRoot getAPI(int key) {
		for (APIRoot a : apis) {
			if (a.key.keyID == key) {
				return a;
			}
		}
		return null;
	}

	public Stream<EveChar> streamChars() {
		return apis.stream().flatMap(a -> a.account.characters().stream());
	}

	public Stream<EveChar> streamTeamCharacters(String team) {
		if (team != null && settings.teams.containsKey(team)) {
			Set<String> members = settings.teams.get(team).members;
			return streamChars().filter(c -> members.contains(c.name));
		}
		return Stream.empty();
	}

	public Stream<EveChar> streamFTeamCharacters() {
		return streamTeamCharacters(settings.focusedTeam);
	}

	// team

	public void addTeam(String name) {
		settings.teams.put(name, new TeamDescription());
		settings.store();
		propagateNewTeam(name);
	}

	public void delTeam(String name) {
		settings.teams.remove(name);
		settings.store();
		propagateDelTeam(name);
	}

	public boolean renameTeam(String old, String now) {
		if (old != null && settings.teams.keySet().contains(old) && now != null && now.length() > 0
				&& !settings.teams.keySet().contains(now)) {
			settings.teams.put(now, settings.teams.get(old));
			settings.teams.remove(old);
			if (old.equals(settings.focusedTeam)) {
				settings.focusedTeam = now;
			}
			settings.store();
			propagateRenameTeam(old, now);
			return true;
		} else {
			return false;
		}
	}

	public boolean copyTeam(String from, String newname) {
		if (from != null && settings.teams.containsKey(from) && newname != null && newname.length() > 0
				&& !settings.teams.containsKey(newname)) {
			settings.teams.put(newname, settings.teams.get(from).clone());
			settings.store();
			propagateNewTeam(newname);
			return true;
		} else {
			return false;
		}
	}

	public void add2Team(String character, String team) {
		settings.teams.get(team).members.add(character);
		settings.store();
		propagateAdd2Team(team, character);
	}

	public void del2Team(String character, String team) {
		settings.teams.get(team).members.remove(character);
		settings.store();
		propagateDel2Team(team, character);
	}

	public void setFocusedTeam(String name) {
		debug("focusing on team " + name);
		settings.focusedTeam = name;
		settings.store();
		propagateFocusedTeam(name);
	}

	/**
	 * find all the possible location ID for the given team.
	 *
	 * @return
	 */
	public Set<String> getTeamPossibleSystems(String team) {
		Set<String> allowedChars = settings.teams.get(team).members;
		Stream<EveChar> chars = apis.parallelStream().flatMap(a -> a.account.characters().parallelStream())
				.filter(c -> allowedChars.contains(c.name));
		return chars.flatMap(this::streamCharPossibleSystems).collect(Collectors.toSet());
	}

	public Stream<String> streamCharPossibleSystems(EveChar c) {
		HashMap<Long, Station> stationsById = db.getStationById();
		return Stream.concat(c.marketOrders().stream().map(oe -> oe.stationID), c.assetList().keySet().stream()).distinct()
				.map(l -> stationsById.get(l)).filter(s -> s != null).map(s -> s.system);
	}

	/**
	 * get the set of system the focused team is limited to
	 *
	 * @return
	 */
	public Set<String> getFTeamSystemLimit() {
		return getTeamSystemLimit(settings.focusedTeam);
	}

	public Set<String> getTeamSystemLimit(String team) {
		if (team != null) {
			return settings.teams.get(team).systems;
		}
		return Collections.emptySet();
	}

	/**
	 * add a system to the limit of a team
	 *
	 * @param teamName
	 * @param sysName
	 * @return
	 */
	public boolean addTeamSystem(String teamName, String sysName) {
		Set<String> teamSystems = settings.teams.get(teamName).systems;
		if (!teamSystems.add(sysName)) {
			return false;
		}
		propagateAddTeamSystem(teamName, sysName);
		settings.store();
		return true;
	}

	/**
	 * remove a system from the limit of a team
	 *
	 * @param teamName
	 * @param sysName
	 * @return
	 */
	public boolean remTeamSystem(String teamName, String sysName) {
		Set<String> teamSystems = settings.teams.get(teamName).systems;
		if (!teamSystems.remove(sysName)) {
			return false;
		}
		propagateRemTeamSystem(teamName, sysName);
		settings.store();
		return true;
	}

	// provision

	/** get the provision of the focused team. */
	public Provision getFTeamProvision() {
		if (settings.focusedTeam == null) {
			return null;
		}
		return settings.teams.get(settings.focusedTeam).provision;
	}

	public void provision(HashMap<Integer, Integer> items) {
		debug("provision " + items);
		for (Entry<Integer, Integer> e : items.entrySet()) {
			propagateNewProvision(e.getKey(), e.getValue());
			getFTeamProvision().totalIn.put(e.getKey(),
					Math.max(0, getFTeamProvision().totalIn.getOrDefault(e.getKey(), 0) + e.getValue()));
		}
		settings.store();
	}

	/** set the requirement in lp offer to given value for the focused team */
	public void provisionLPOffer(LPOffer offer, int requirement) {
		Provision p = getFTeamProvision();
		int diff = requirement - p.lpoffersIn.getOrDefault(offer.id, 0);
		if (requirement <= 0) {
			p.lpoffersIn.remove(offer.id);
		} else {
			p.lpoffersIn.put(offer.id, requirement);
		}
		for (ItemRef e : offer.requirements.items) {
			int newQtty = p.totalIn.getOrDefault(e.type_id, 0) + e.quantity * diff;
			propagateNewProvision(e.type_id, newQtty);
			if (newQtty > 0) {
				p.totalIn.put(e.type_id, newQtty);
			} else {
				p.totalIn.remove(e.type_id);
			}
		}
		settings.store();
	}

	// items

	/** only fetch characer data once every X minutes */
	protected int assetCacheDelayMinutes = 30;

	/** char->system->typeID->qtty */
	protected Map<Long, Map<String, Map<Integer, Long>>> itemsByCharName = Collections.synchronizedMap(new HashMap<>());
	protected Map<Long, Date> expireItemsByCharName = new HashMap<>();

	public Map<String, Map<Integer, Long>> getCharItems(EveChar c) {
		Date cacheExpire = expireItemsByCharName.get(c.characterID);
		Date now = new Date();
		if (cacheExpire != null && cacheExpire.after(now)) {
			logger.trace("returning old cache for character " + c.name);
			return itemsByCharName.get(c.characterID);
		} else {
			Map<String, Map<Integer, Long>> itemsqtty = fetchCharItems(c);
			itemsByCharName.put(c.characterID, itemsqtty);
			expireItemsByCharName.put(c.characterID, new Date(now.getTime() + assetCacheDelayMinutes * 60000));
			logger.trace("new items for " + c.name + " : " + itemsqtty);
			return itemsqtty;
		}
	}

	/** fetch the assets and BO of given character */
	protected Map<String, Map<Integer, Long>> fetchCharItems(EveChar c) {
		HashMap<Long, Station> stationById = db().getStationById();
		// for each
		HashMap<String, Map<Integer, Long>> itemsqtty = new HashMap<>();
		for (Entry<Long, ArrayList<Content>> e : c.assetList().entrySet()) {
			Station station = stationById.get(e.getKey());
			if (station != null) {
				Map<Integer, Long> localGains = itemsqtty.get(station.system);
				if (localGains == null) {
					localGains = new HashMap<>();
					itemsqtty.put(station.system, localGains);
				}
				for (Content co : e.getValue()) {
					localGains.put(co.typeID, co.quantity + localGains.getOrDefault(co.typeID, 0l));
				}
			} else {
				logger.debug("no station for id " + e.getKey());
			}
		}
		for (OrderEntry a : c.marketOrders()) {
			if (a.isOpen() && a.isBuyOrder()) {
				Station station = stationById.get(a.stationID);
				if (station != null) {
					Map<Integer, Long> localGains = itemsqtty.get(station.system);
					if (localGains == null) {
						localGains = new HashMap<>();
						itemsqtty.put(station.system, localGains);
					}
					localGains.put(a.typeID, a.volRemaining + localGains.getOrDefault(a.typeID, 0l));
				} else {
					logger.debug("no station for id " + a.stationID);
				}
			}
		}
		return itemsqtty;
	}

	/**
	 * contains for each team the last list of items retrieved
	 */
	protected Map<String, Map<Integer, Long>> cachedTeamItems = Collections.synchronizedMap(new HashMap<>());

	// getting the items

	/**
	 * get the list of items for given team. fires a
	 * {@link #propagateFocusedTeamNewItems(Map)}
	 *
	 * @param team
	 *          the name of the team
	 * @return the new list of items.
	 */
	public Map<Integer, Long> getTeamItems(String team) {
		if (team == null) {
			return Collections.emptyMap();
		}
		Set<String> teamSystems = getTeamSystemLimit(team);
		logger.trace("recomputing items for team " + team);
		// recompute the whole map. successive calls to
		// getItems use the cache, making it cost effective.
		Map<Integer, Long> newItems = fetchTeamAssets(team, teamSystems);
		Map<Integer, Long> oldItems = cachedTeamItems.get(team);
		Map<Integer, Long> diff = new HashMap<>(newItems);
		if (oldItems != null) {
			for (Entry<Integer, Long> e : oldItems.entrySet()) {
				long value = newItems.getOrDefault(e.getKey(), 0l) - e.getValue();
				if (value != 0) {
					diff.put(e.getKey(), value);
				} else {
					diff.remove(e.getKey());
				}
			}
		}
		cachedTeamItems.put(team, newItems);
		if (!diff.isEmpty()) {
			logger.trace("items diff for team " + team + " : " + diff);
			propagateTeamNewItems(team, diff);
		}
		return newItems;
	}

	private Map<Integer, Long> fetchTeamAssets(String name, Set<String> teamSystems) {
		Stream<Map.Entry<Integer, Long>> teamAssetsStream = streamTeamCharacters(name)
				.flatMap(c -> getCharItems(c).entrySet().stream())
				.filter(e -> teamSystems.isEmpty() || teamSystems.contains(e.getKey()))
				.flatMap(e -> e.getValue().entrySet().stream());
		return teamAssetsStream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
	}

	public Map<Integer, Long> getFTeamItems() {
		if (settings.focusedTeam == null) {
			logger.debug("null focused team");
			return Collections.emptyMap();
		}
		return getTeamItems(settings.focusedTeam);
	}

	/**
	 * compute the difference in items assets and buy orders between last call and
	 * now. if we already called for those assets {@value #assetCacheDelayMinutes}
	 * or less before, we don't request again.
	 *
	 * <p>
	 * This should be thread-safe. Only the cache is manipulated, which is in a
	 * synchronize map.
	 * </p>
	 *
	 * @param c
	 *          the character to get the items for
	 * @return a new Hashmap, system->itemid->difference in number.
	 */
	protected Map<String, Map<Integer, Long>> computeItemsDiff(EveChar c) {

		// if we don't need to fetch data gain, return empty map
		Date now = new Date();
		Date cacheExpiration = expireItemsByCharName.get(c.characterID);
		if (cacheExpiration != null && cacheExpiration.after(now)) {
			return Collections.emptyMap();
		}
		logger.trace("invalid cache entry for character " + c.name);
		// compute difference between old and new item list
		Map<String, Map<Integer, Long>> oldItems = itemsByCharName.get(c.characterID);
		Map<String, Map<Integer, Long>> newItems = getCharItems(c);
		if (oldItems == null) {
			logger.trace("items diff for " + c.name + " : " + newItems);
			return newItems;
		} else {
			Map<String, Map<Integer, Long>> itemsDiff = new HashMap<>();
			for (String systemName : Stream.concat(oldItems.keySet().stream(), itemsDiff.keySet().stream())
					.collect(Collectors.toSet())) {
				Map<Integer, Long> systemDiff = new HashMap<>();
				itemsDiff.put(systemName, systemDiff);
				Map<Integer, Long> systemNew = newItems.get(systemName);
				if (systemNew != null) {
					systemDiff.putAll(systemNew);
				}
				Map<Integer, Long> systemOld = oldItems.get(systemName);
				if (systemOld != null) {
					for (Entry<Integer, Long> e : systemOld.entrySet()) {
						long newval = systemDiff.getOrDefault(e.getKey(), 0l) - e.getValue();
						if (newval == 0) {
							systemDiff.remove(e.getKey());
						} else {
							systemDiff.put(e.getKey(), newval);
						}
					}
				}
				if (systemDiff.isEmpty()) {
					itemsDiff.remove(systemName);
				}
			}
			logger.trace("items diff for " + c.name + " : " + itemsDiff);
			return itemsDiff;
		}
	}

	// debug

	protected static class DebugEntry {
		String message;
		Class<? extends EvePane> context;
		Date date;
	}

	protected TableView<DebugEntry> debugPane = new TableView<>();
	{
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		TableColumn<DebugEntry, String> dateCol = new TableColumn<>("date");
		dateCol.setCellValueFactory(ct -> new ReadOnlyObjectWrapper<>(dateFormat.format(ct.getValue().date)));
		debugPane.getColumns().add(dateCol);

		TableColumn<DebugEntry, String> ctxtCol = new TableColumn<>("context");
		ctxtCol.setCellValueFactory(ct -> new ReadOnlyObjectWrapper<>(ct.getValue().context.getSimpleName()));
		ctxtCol.setMinWidth(130);
		debugPane.getColumns().add(ctxtCol);

		TableColumn<DebugEntry, String> messCol = new TableColumn<>("message");
		messCol.setCellValueFactory(ct -> new ReadOnlyObjectWrapper<>(ct.getValue().message));
		messCol.setMinWidth(500);
		debugPane.getColumns().add(messCol);

		dateCol.setSortType(TableColumn.SortType.DESCENDING);
		dateCol.setSortable(true);
		debugPane.getSortOrder().add(dateCol);

	}

	/**
	 * add item to the debug pane. synchronized.
	 *
	 * @param clazz
	 *          the class of the item which wants to add an entry
	 * @param data
	 *          the information
	 */
	public void printDebug(Class<? extends EvePane> clazz, String data) {
		synchronized (debugPane) {
			DebugEntry de = new DebugEntry();
			de.message = data;
			de.context = clazz;
			de.date = new Date();
			debugPane.getItems().add(de);
			debugPane.sort();
		}
	}

	// database

	protected YamlDatabase db = new YamlDatabase();

	@Override
	public YamlDatabase db() {
		return db;
	}

}
