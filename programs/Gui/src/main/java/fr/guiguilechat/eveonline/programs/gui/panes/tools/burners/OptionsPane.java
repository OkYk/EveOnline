package fr.guiguilechat.eveonline.programs.gui.panes.tools.burners;

import java.util.Map;

import fr.guiguilechat.eveonline.programs.gui.Manager;
import fr.guiguilechat.eveonline.programs.gui.Settings.BurnersEval;
import fr.guiguilechat.eveonline.programs.gui.panes.EvePane;
import fr.guiguilechat.eveonline.programs.panes.ScrollAdd;
import fr.guiguilechat.eveonline.programs.panes.TypedField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class OptionsPane extends HBox implements EvePane {

	protected final Manager parent;

	@Override
	public Manager parent() {
		return parent;
	}

	public Button computeBtn = new Button("COMPUTE");

	public ChoiceBox<String> regionMarket = new ChoiceBox<>();
	public TypedField<Double> sellTax, brokerTax;
	public TypedField<Integer> lpQtty;
	public Button modifMarket = new Button("update market");

	public TypedField<Double> weightSys, weightConst, weightOut, hubConstelMult;
	public Button modifMap = new Button("update map");

	public TypedField<Double> systemTime, burnerTime;
	public Button modifTime = new Button("update times");

	public OptionsPane(Manager parent) {
		this.parent = parent;
	}

	@Override
	public void onIsShown(boolean shown) {
		if (shown) {
			load();
		}
	}

	boolean loaded = false;

	protected void load() {
		if(loaded) {
			return;
		}
		BurnersEval burnersSettings = parent().settings.burners;


		GridPane marketPane = new GridPane();
		marketPane.setStyle("-fx-border-color: black; -fx-border-width: 1;");

		// add all the regions to the choicebox
		parent.db().getLocations().entrySet().stream().filter(e -> e.getValue().parentRegion == null).map(Map.Entry::getKey)
		.forEachOrdered(regionMarket.getItems()::add);
		regionMarket.getItems().sort(String::compareToIgnoreCase);
		regionMarket.getSelectionModel().select(parent().settings.burners.region);
		marketPane.addRow(0, new Label("market region"), regionMarket);

		sellTax = TypedField.positivDecimal(burnersSettings.sellTax);
		sellTax.setTooltip(new Tooltip("percentage of the sale that is due as tax"));
		sellTax.setOnScroll(new ScrollAdd.DoubleScrollAdd(0.1, sellTax));
		marketPane.addRow(1, new Label("sell tax percentage"), sellTax);

		brokerTax = TypedField.positivDecimal(burnersSettings.brokerTax);
		brokerTax.setTooltip(new Tooltip(
				"when buying at BO value or selling at SO value, the percentage of the transaction that is due as broker fee"));
		brokerTax.setOnScroll(new ScrollAdd.DoubleScrollAdd(0.1, brokerTax));
		marketPane.addRow(2, new Label("broker tax percentage"), brokerTax);

		lpQtty = TypedField.positivIntField(burnersSettings.lpQtty);
		lpQtty.setTooltip(new Tooltip("quantity of LP to use. Higher LP quantity means less interesting BO/SO values, "));
		lpQtty.setOnScroll(new ScrollAdd.IntScrollAdd(100000, lpQtty));
		marketPane.addRow(3, new Label("LP quantity"), lpQtty);

		marketPane.addRow(4, new Label(), modifMarket);


		GridPane mapPane = new GridPane();
		mapPane.setStyle("-fx-border-color: black; -fx-border-width: 1;");

		weightSys = TypedField.positivDecimal(burnersSettings.weightSystem);
		weightSys.setTooltip(new Tooltip("probability weight of self system"));
		weightSys.setOnScroll(new ScrollAdd.DoubleScrollAdd(0.1, weightSys));
		mapPane.addRow(0, new Label("system weight"), weightSys);

		weightConst = TypedField.positivDecimal(burnersSettings.weightConstel);
		weightConst.setTooltip(new Tooltip("probability weight of system in same constelation"));
		weightConst.setOnScroll(new ScrollAdd.DoubleScrollAdd(0.1, weightConst));
		mapPane.addRow(1, new Label("constel weight"), weightConst);

		weightOut = TypedField.positivDecimal(burnersSettings.weightOut);
		weightOut.setTooltip(new Tooltip("probability weight of system in adjacent constel"));
		weightOut.setOnScroll(new ScrollAdd.DoubleScrollAdd(0.1, weightOut));
		mapPane.addRow(2, new Label("other weight"), weightOut);

		hubConstelMult = TypedField.positivDecimal(burnersSettings.hubConstelMult);
		hubConstelMult.setTooltip(new Tooltip("weight mult of system in hub constel"));
		hubConstelMult.setOnScroll(new ScrollAdd.DoubleScrollAdd(0.1, hubConstelMult));
		mapPane.addRow(3, new Label("hub mult"), hubConstelMult);

		mapPane.addRow(4, new Label(), modifMap);


		GridPane speedPane = new GridPane();
		speedPane.setStyle("-fx-border-color: black; -fx-border-width: 1;");

		systemTime = TypedField.positivDecimal(burnersSettings.systemTime);
		systemTime.setTooltip(new Tooltip("avg time to travel through a system in min"));
		systemTime.setOnScroll(new ScrollAdd.DoubleScrollAdd(0.1, systemTime));
		speedPane.addRow(0, new Label("system warp time"), systemTime);

		burnerTime = TypedField.positivDecimal(burnersSettings.burnerTime);
		burnerTime.setTooltip(new Tooltip(
				"avg time to select mission and ship, undock, make last burner warp, kill it and warp back to gate in minute."));
		burnerTime.setOnScroll(new ScrollAdd.DoubleScrollAdd(0.1, burnerTime));
		speedPane.addRow(1, new Label("burner time"), burnerTime);

		speedPane.addRow(2, new Label(), modifTime);

		getChildren().addAll(computeBtn, marketPane, mapPane, speedPane);
		loaded=true;
	}

}
