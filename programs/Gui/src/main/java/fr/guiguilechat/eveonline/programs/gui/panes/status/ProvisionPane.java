package fr.guiguilechat.eveonline.programs.gui.panes.status;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.LoggerFactory;

import fr.guiguilechat.eveonline.programs.gui.Manager;
import fr.guiguilechat.eveonline.programs.gui.Settings.ProvisionType;
import fr.guiguilechat.eveonline.programs.gui.panes.EvePane;
import fr.guiguilechat.eveonline.programs.gui.panes.status.ProvisionPane.ProvisionData;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ProvisionPane extends TableView<ProvisionData> implements EvePane {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProvisionPane.class);

	public static class ProvisionData {
		public String item;
		public String who;
		public long required;
		public long owned;

		@Override
		public int hashCode() {
			return (int) (item.hashCode() + who.hashCode() + required + owned);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj.getClass() != ProvisionData.class) {
				return false;
			}
			ProvisionData o = (ProvisionData) obj;
			return item.equals(o.item) && who.equals(o.who) && required == o.required && owned == o.owned;
		}

		@Override
		public String toString() {
			return item;
		}
	}

	protected final Manager parent;

	@Override
	public Manager parent() {
		return parent;
	}

	public ProvisionPane(Manager parent) {
		this.parent = parent;

		TableColumn<ProvisionData, String> desCol = new TableColumn<>("item");
		desCol.setCellValueFactory(ed -> new ReadOnlyObjectWrapper<>(ed.getValue().item));
		desCol.setMinWidth(400);
		getColumns().add(desCol);

		TableColumn<ProvisionData, Long> missingCol = new TableColumn<>("missing");
		missingCol.setCellValueFactory(ed -> new ReadOnlyObjectWrapper<>(ed.getValue().required - ed.getValue().owned));
		getColumns().add(missingCol);

		TableColumn<ProvisionData, String> whoCol = new TableColumn<>("who");
		whoCol.setCellValueFactory(ed -> new ReadOnlyObjectWrapper<>(ed.getValue().who));
		getColumns().add(whoCol);

		TableColumn<ProvisionData, Long> ownedcol = new TableColumn<>("owned");
		ownedcol.setCellValueFactory(ed -> new ReadOnlyObjectWrapper<>(ed.getValue().owned));
		getColumns().add(ownedcol);
	}

	//
	// provision preparation.
	//

	protected static class ProvisionPreparation {
		public String name;
		public ProvisionData ed;
		public boolean added = false;
		public int itemID;
	}

	HashMap<Integer, ProvisionPreparation> itemsProvisionsMaterial = new HashMap<>();
	HashMap<Integer, ProvisionPreparation> itemsProvisionsProdcut = new HashMap<>();
	HashMap<Integer, ProvisionPreparation> itemsProvisionsSO = new HashMap<>();

	/** get a provision preparation of given type for given item id */
	public ProvisionPreparation getProvision(int itemID, ProvisionType ptype) {
		HashMap<Integer, ProvisionPreparation> map = null;
		switch (ptype) {
		case MATERIAL:
			map = itemsProvisionsMaterial;
			break;
		case PRODUCT:
			map = itemsProvisionsProdcut;
			break;
		case SO:
			map = itemsProvisionsSO;
			break;
		default:
			throw new UnsupportedOperationException("handle " + ptype);
		}
		ProvisionPreparation ret = map.get(itemID);
		if (ret == null) {
			logger.trace("creating provision for item " + itemID);
			ret = new ProvisionPreparation();
			ret.name = db().getElementById(itemID);
			ret.ed = new ProvisionData();
			ret.itemID = itemID;
			map.put(itemID, ret);
		}
		return ret;
	}

	/**
	 * prepare provisions for focused team.
	 */
	protected void prepareProvisions() {
		itemsProvisionsMaterial.values().stream().forEach(pp -> pp.ed.required = 0);
		for (ProvisionType ptype : ProvisionType.values()) {
			for (Entry<Integer, Integer> e : parent.getFTeamProvision(ptype).total.entrySet()) {
				ProvisionPreparation pr = getProvision(e.getKey(), ptype);
				pr.ed.required = e.getValue();
				pr.ed.who = parent().settings.focusedTeam;
			}
		}
		onTeamNewItems(parent().settings.focusedTeam, parent().getFTeamItems());
	}

	@Override
	public void onNewProvision(ProvisionType ptype, int itemID, int qtty) {
		if (shown) {
			ProvisionPreparation pr = getProvision(itemID, ptype);
			pr.ed.required = qtty;
			pr.ed.who = parent().settings.focusedTeam;
			updateItemQuantity(parent().getFTeamItems().getOrDefault(itemID, 0l), pr);
		}
	}

	@Override
	public void onTeamNewItems(String team, Map<Integer, Long> itemsDiff) {
		if (!shown || team == null || !team.equals(parent.settings.focusedTeam)) {
			logger.trace("skipping items diff for team " + team);
			return;
		}
		logger.trace("new items for focused team " + team + " : " + itemsDiff);
		Map<Integer, Long> items = parent().getFTeamItems();
		for (Integer itemID : itemsDiff.keySet()) {
			updateItemQuantity(items.getOrDefault(itemID, 0l), getProvision(itemID, ProvisionType.MATERIAL));
		}
		sort();
	}

	/** update graphics on the modification of provisioned item's quantity */
	protected void updateItemQuantity(long qtty, ProvisionPreparation pp) {
		if (pp.ed.required > 0) {
			logger.trace("updating items " + pp.name + " required" + pp.ed.required + " qtty" + qtty);
		}
		if (qtty < pp.ed.required) {
			logger.trace("adding item " + pp.name + " required " + pp.ed.required + ", we have " + qtty);
			pp.ed.item = pp.name;
			pp.ed.owned = qtty;
			if (!pp.added) {
				getItems().add(pp.ed);
			}
			pp.added = true;
		} else {
			if (pp.added) {
				getItems().remove(pp.ed);
				logger.trace("removing item " + pp.name + " required " + pp.ed.required + ", we have " + qtty);
			}
			pp.added = false;
		}

	}

	@Override
	public void onFocusedTeam(String teamName) {
		getItems().clear();
		itemsProvisionsMaterial.values().forEach(pp -> pp.added = false);
		if (shown) {
			prepareProvisions();
		}
	}

	boolean shown = false;

	@Override
	public void onIsShown(boolean shown) {
		this.shown = shown;
		if (shown) {
			prepareProvisions();
		}
	}

}