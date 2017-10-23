package fr.guiguilechat.eveonline.programs.gui.panes.provision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.guiguilechat.eveonline.model.database.yaml.LPOffer;
import fr.guiguilechat.eveonline.programs.gui.Manager;
import fr.guiguilechat.eveonline.programs.gui.panes.EvePane;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ProvisionLPStorePane extends BorderPane implements EvePane {

	private static final Logger logger = LoggerFactory.getLogger(ProvisionLPStorePane.class);

	protected Manager parent;

	@Override
	public Manager parent() {
		return parent;
	}

	public ProvisionLPStorePane(Manager parent) {
		this.parent = parent;
	}

	boolean loaded = false;
	protected ArrayList<LPOffer> lpoffers;

	ChoiceBox<String> corporationChoice = new ChoiceBox<>();
	ChoiceBox<Boolean> blueprintAllowedChoice = new ChoiceBox<>();
	HBox selectionPane = new HBox();
	TableView<OfferRow> listOffersPane = new TableView<>();

	public void load() {
		if (loaded) {
			return;
		}
		if (lpoffers == null) {
			lpoffers = db().getLPOffers();
		}
		selectionPane.getChildren().add(new Label("corporation: "));
		corporationChoice.getItems()
		.addAll(lpoffers.stream().map(lo -> lo.corporation).distinct().sorted().collect(Collectors.toList()));
		// allow no value in the choicebox
		corporationChoice.getItems().add(null);
		corporationChoice.setOnAction(ev -> updateOffers());
		selectionPane.getChildren().add(corporationChoice);

		selectionPane.getChildren().add(new Label("blueprints: "));
		blueprintAllowedChoice.getItems().addAll(true, false, null);
		blueprintAllowedChoice.setOnAction(ev -> updateOffers());
		selectionPane.getChildren().add(blueprintAllowedChoice);

		setTop(selectionPane);

		TableColumn<OfferRow, String> namecol = new TableColumn<>("name");
		namecol.setCellValueFactory(ed -> new ReadOnlyObjectWrapper<>(ed.getValue().offer.offer_name));
		listOffersPane.getColumns().add(namecol);

		TableColumn<OfferRow, Integer> lpcol = new TableColumn<>("lp");
		lpcol.setCellValueFactory(ed -> new ReadOnlyObjectWrapper<>(ed.getValue().offer.requirements.lp));
		lpcol.setMinWidth(80);
		lpcol.setMaxWidth(80);
		listOffersPane.getColumns().add(lpcol);
		lpcol.setSortType(SortType.DESCENDING);
		listOffersPane.getSortOrder().add(lpcol);

		TableColumn<OfferRow, TextField> nbCol = new TableColumn<>();
		nbCol.setCellValueFactory(ed -> new ReadOnlyObjectWrapper<>(ed.getValue().nb_field));
		nbCol.setMinWidth(50);
		nbCol.setMaxWidth(50);
		listOffersPane.getColumns().add(nbCol);

		TableColumn<OfferRow, Button> sendCol = new TableColumn<>("");
		sendCol.setCellValueFactory(ed -> new ReadOnlyObjectWrapper<>(ed.getValue().bt_send));
		sendCol.setMinWidth(100);
		sendCol.setMaxWidth(100);
		listOffersPane.getColumns().add(sendCol);

		listOffersPane.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		setCenter(listOffersPane);
		loaded = true;
		updateOffers();
	}

	protected void updateOffers() {
		logger.trace("updating offers");
		listOffersPane.getItems().clear();
		if (!loaded) {
			return;
		}

		String corp = corporationChoice.getValue();
		if (corp == null) {
			return;
		}
		Boolean bp = blueprintAllowedChoice.getValue();
		for (LPOffer lo : lpoffers) {
			boolean isbp = lo.offer_name.contains("Blueprint");
			if (corp != null && corp.equals(lo.corporation) && !(bp == Boolean.TRUE && !isbp)
					&& !(bp == Boolean.FALSE && isbp)) {
				listOffersPane.getItems().add(getRow(lo));
			}
		}
		listOffersPane.sort();
	}

	/**
	 * show a lp store offer as a row
	 *
	 */
	protected static class OfferRow {
		public LPOffer offer;
		public TextField nb_field;
		public Button bt_send;
	}

	protected HashMap<LPOffer, OfferRow> cacherows = new HashMap<>();

	protected OfferRow getRow(LPOffer offer) {
		if (cacherows.containsKey(offer)) {
			return cacherows.get(offer);
		}
		OfferRow ret = new OfferRow();
		ret.offer = offer;
		int provision_nb = parent().getFTeamProvision().lpoffers.getOrDefault(offer.id, 0);
		ret.nb_field = new TextField("" + provision_nb);
		ret.bt_send = new Button("provision");
		ret.bt_send.setOnAction(ev -> provision(ret, ret.nb_field.getText()));
		cacherows.put(offer, ret);
		return ret;
	}

	public void provision(OfferRow row, String nb_text) {
		int nb_provision = Integer.parseInt(nb_text);
		parent().provisionLPOffer(row.offer, nb_provision);
	}

	@Override
	public void onFocusedTeam(String teamName) {
		updateOffers();
	}

	@Override
	public void onNewProvision(int itemID, int qtty) {
		updateOffers();
	}

	@Override
	public void onIsShown(boolean shown) {
		logger.debug("Provision lpstore is shown " + shown);
		if (shown) {
			load();
		}
	}
}
