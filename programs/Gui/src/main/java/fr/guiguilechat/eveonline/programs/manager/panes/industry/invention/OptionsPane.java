package fr.guiguilechat.eveonline.programs.manager.panes.industry.invention;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import fr.guiguilechat.eveonline.model.apiv2.APIRoot;
import fr.guiguilechat.eveonline.model.apiv2.Account;
import fr.guiguilechat.eveonline.model.apiv2.Account.EveChar;
import fr.guiguilechat.eveonline.model.sde.locations.Region;
import fr.guiguilechat.eveonline.programs.manager.Manager;
import fr.guiguilechat.eveonline.programs.manager.Settings.InventionParams;
import fr.guiguilechat.eveonline.programs.manager.Settings.InventionParams.TARGETDECRYPTOR;
import fr.guiguilechat.eveonline.programs.manager.panes.EvePane;
import fr.guiguilechat.eveonline.programs.manager.panes.ScrollAdd;
import fr.guiguilechat.eveonline.programs.manager.panes.industry.invention.InventerPane.StructBonus;
import fr.guiguilechat.eveonline.programs.manager.representation.ChoiceBoxRepresentation;
import fr.guiguilechat.eveonline.programs.manager.representation.PaneWithRepresentation;
import fr.guiguilechat.eveonline.programs.manager.representation.Representation;
import fr.guiguilechat.eveonline.programs.manager.representation.TextFieldRepresentation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

public class OptionsPane extends HBox implements EvePane, PaneWithRepresentation {

	protected final Manager parent;

	@Override
	public Manager parent() {
		return parent;
	}

	protected Collection<Representation<?>> representations;

	@Override
	public Collection<Representation<?>> GetRepresentations() {
		return representations;
	}

	public Button computeBtn = new Button("COMPUTE");

	public ChoiceBoxRepresentation<EveChar> characterSkills;

	public TextField bpPattern = new TextField();
	public ChoiceBoxRepresentation<TARGETDECRYPTOR> bestDecryptor;
	public TextFieldRepresentation<Double> maxCycleReduction;
	public TextFieldRepresentation<Double> minHours;

	public TextFieldRepresentation<Double> sellTax, brokerFee;
	public ChoiceBoxRepresentation<String> marketRegion;

	public TextFieldRepresentation<Double> copyTax;
	public ChoiceBoxRepresentation<String> copyRegion;
	public ChoiceBoxRepresentation<String> copySystem;
	public ChoiceBoxRepresentation<InventerPane.StructBonus> copystruct;

	public TextFieldRepresentation<Double> inventTax;
	public ChoiceBoxRepresentation<String> inventRegion;
	public ChoiceBoxRepresentation<String> inventSystem;
	public ChoiceBoxRepresentation<InventerPane.StructBonus> inventstruct;

	public TextFieldRepresentation<Double> manufTax;
	public ChoiceBoxRepresentation<String> manufRegion;
	public ChoiceBoxRepresentation<String> manufSystem;
	public ChoiceBoxRepresentation<InventerPane.StructBonus> manufstruct;

	ObservableList<EveChar> chars;

	public OptionsPane(Manager parent) {
		this.parent = parent;
		chars = FXCollections.observableArrayList(InventerPane.ALL5);
	}

	@Override
	public void onIsShown(boolean shown) {
		if (shown) {
			load();
		}
	}

	boolean loaded = false;

	protected void load() {
		if (loaded) {
			return;
		}
		InventionParams settings = parent().settings.invention;
		characterSkills = new ChoiceBoxRepresentation<>(
				() -> parent.streamChars().filter(c -> c.name.equals(settings.characterSkills)).findAny().orElse(InventerPane.ALL5),
				c -> settings.characterSkills = c.name,
				chars);
		characterSkills.getBox().setConverter(new StringConverter<Account.EveChar>() {

			@Override
			public String toString(EveChar object) {
				return object != null ? object.name : "";
			}

			@Override
			public EveChar fromString(String string) {
				throw new UnsupportedOperationException();
			}
		});
		List<String> regions = Region.load().values().stream().filter(r -> !r.isWormhole).map(r -> r.name)
				.collect(Collectors.toList());
		//		 = parent.db().getLocations().entrySet().stream().filter(e -> e.getValue().parentRegion == null)
		//				.map(Map.Entry::getKey).sorted(String::compareToIgnoreCase)
		//				.filter(s -> !(s.charAt(0) >= 'a' && s.charAt(0) <= 'z')).collect(Collectors.toList());
		marketRegion = new ChoiceBoxRepresentation<>(
				() -> regions.stream().filter(r -> r.equals(settings.marketRegion)).findAny().orElse("TheForge"),
				settings::setMarketRegion,
				regions);

		sellTax = TextFieldRepresentation.positivDecimal(settings::getSellTax, settings::setSellTax);
		sellTax.getField().setTooltip(new Tooltip("percentage of the sale that is due as tax"));
		sellTax.getField().setOnScroll(new ScrollAdd.DoubleScrollAdd(0.1, sellTax.getField()));

		brokerFee = TextFieldRepresentation.positivDecimal(settings::getBrokerFee, settings::setBrokerFee);
		brokerFee.getField().setTooltip(new Tooltip(
				"when buying at BO value or selling at SO value, the percentage of the transaction that is due as broker fee"));
		brokerFee.getField().setOnScroll(new ScrollAdd.DoubleScrollAdd(0.1, brokerFee.getField()));

		bestDecryptor = new ChoiceBoxRepresentation<>(settings::getTarget, settings::setTarget, TARGETDECRYPTOR.values());
		maxCycleReduction = TextFieldRepresentation.positivDecimal(settings::getMaxCycleReduction,
				settings::setMaxCycleReduction);
		maxCycleReduction.getField().setOnScroll(new ScrollAdd.DoubleScrollAdd(1.0, maxCycleReduction.getField()));
		maxCycleReduction.getField()
		.setTooltip(new Tooltip("possible reduction on gain, in percentage, to calculate the number of cycles."));

		minHours = TextFieldRepresentation.positivDecimal(settings::getMinActionHours, settings::setMinActionHours);
		minHours.getField().setTooltip(new Tooltip(
				"The minimum time, in hours, we consider between actions. If a cycle invention or manufacture lasts less than this value, it is actually set to this value"));
		minHours.getField().setOnScroll(new ScrollAdd.DoubleScrollAdd(1.0, minHours.getField()));

		copyTax = TextFieldRepresentation.positivDecimal(settings::getCopyTax, settings::setCopyTax);
		copyTax.getField().setTooltip(new Tooltip("station tax on copying"));
		copyTax.getField().setOnScroll(new ScrollAdd.DoubleScrollAdd(0.1, copyTax.getField()));

		copySystem = makeSystemSelection(settings::getCopySystem, settings::setCopySystem, settings::getCopyRegion);
		copyRegion = makeRegionSelection(settings::getCopyRegion, settings::setCopyRegion, copySystem.getBox().getItems());

		copystruct = new ChoiceBoxRepresentation<>(
				() -> (settings.copystruct == null ? StructBonus.none : StructBonus.valueOf(settings.copystruct)),
				sb -> settings.setCopystruct(sb.name()),
				StructBonus.values());

		inventTax = TextFieldRepresentation.positivDecimal(settings::getInventTax, settings::setInventTax);
		inventTax.getField().setTooltip(new Tooltip("station tax on invention"));
		inventTax.getField().setOnScroll(new ScrollAdd.DoubleScrollAdd(0.1, inventTax.getField()));

		inventSystem = makeSystemSelection(settings::getInventSystem, settings::setInventSystem, settings::getInventRegion);
		inventRegion = makeRegionSelection(settings::getInventRegion, settings::setInventRegion,
				inventSystem.getBox().getItems());

		inventstruct = new ChoiceBoxRepresentation<>(
				() -> (settings.inventstruct == null ? StructBonus.none : StructBonus.valueOf(settings.inventstruct)),
				sb -> settings.setInventstruct(sb.name()), StructBonus.values());

		manufTax = TextFieldRepresentation.positivDecimal(settings::getManufactureTax, settings::setManufactureTax);
		manufTax.getField().setTooltip(new Tooltip("station tax on manufacture"));
		manufTax.getField().setOnScroll(new ScrollAdd.DoubleScrollAdd(0.1, manufTax.getField()));

		manufSystem = makeSystemSelection(settings::getManufSystem, settings::setManufSystem, settings::getManufRegion);
		manufRegion = makeRegionSelection(settings::getManufRegion, settings::setManufRegion,
				manufSystem.getBox().getItems());

		manufstruct = new ChoiceBoxRepresentation<>(
				() -> (settings.manufstruct == null ? StructBonus.none : StructBonus.valueOf(settings.manufstruct)),
				sb -> settings.setManufstruct(sb.name()), StructBonus.values());

		representations = Arrays.asList(brokerFee, characterSkills, copySystem, copyRegion, copystruct, copyTax,
				inventSystem, inventRegion, inventstruct, inventTax, manufSystem, manufRegion, manufstruct, manufTax,
				marketRegion, maxCycleReduction, minHours, sellTax, bestDecryptor);
		for (Representation<?> r : representations) {
			r.getRegion().setMaxWidth(70);
		}
		bpPattern.setMaxWidth(70);

		GridPane mainpane = new GridPane();
		mainpane.setStyle("-fx-border-color: black; -fx-border-width: 1;");
		mainpane.addRow(0, new Label("character"), characterSkills.getBox());
		mainpane.addRow(1, new Label("sell tax %"), sellTax.getField());
		mainpane.addRow(2, new Label("product name"), bpPattern);
		bpPattern.setTooltip(new Tooltip("specify a pattern to limit the products. eg \"small\""));
		mainpane.addRow(3, new Label("best descryp"), bestDecryptor.getBox());
		mainpane.addRow(4, new Label("max reduction"), maxCycleReduction.getField());
		mainpane.addRow(5, new Label("min hours"), minHours.getField());
		mainpane.addRow(6, computeBtn);

		GridPane importPane = new GridPane();
		importPane.setStyle("-fx-border-color: black; -fx-border-width: 1;");
		importPane.addRow(0, new Label("import"));
		importPane.addRow(1, new Label("region"), marketRegion.getBox());
		importPane.addRow(2, new Label("broker %"), brokerFee.getField());

		GridPane copyPane = new GridPane();
		copyPane.setStyle("-fx-border-color: black; -fx-border-width: 1;");
		copyPane.addRow(0, new Label("copying"));
		copyPane.addRow(1, new Label("tax %"), copyTax.getField());
		copyPane.addRow(2, new Label("struct"), copystruct.getBox());
		copyPane.addRow(3, new Label("region"), copyRegion.getBox());
		copyPane.addRow(4, new Label("system"), copySystem.getBox());

		GridPane inventPane = new GridPane();
		inventPane.setStyle("-fx-border-color: black; -fx-border-width: 1;");
		inventPane.addRow(0, new Label("invent"));
		inventPane.addRow(1, new Label("tax %"), inventTax.getField());
		inventPane.addRow(2, new Label("struct"), inventstruct.getBox());
		inventPane.addRow(3, new Label("region"), inventRegion.getBox());
		inventPane.addRow(4, new Label("system"), inventSystem.getBox());

		GridPane manufPane = new GridPane();
		manufPane.setStyle("-fx-border-color: black; -fx-border-width: 1;");
		manufPane.addRow(0, new Label("manuf"));
		manufPane.addRow(1, new Label("tax %"), manufTax.getField());
		manufPane.addRow(2, new Label("struct"), manufstruct.getBox());
		manufPane.addRow(3, new Label("region"), manufRegion.getBox());
		manufPane.addRow(4, new Label("system"), manufSystem.getBox());

		getChildren().addAll(mainpane, importPane, copyPane, inventPane, manufPane);
		loaded = true;
	}

	@Override
	public void onNewXMLV2(APIRoot... apis) {
		if (apis != null) {
			for (APIRoot ar : apis) {
				if (!ar.isCorp()) {
					chars.addAll(ar.account.characters());
				}
			}
		}
	}

	public static ChoiceBoxRepresentation<String> makeSystemSelection(Supplier<String> getter, Consumer<String> setter,
			Supplier<String> region) {
		return new ChoiceBoxRepresentation<>(getter, setter,
				region.get() != null ? Region.load().get(region.get()).system().sorted().collect(Collectors.toList())
						: Collections.emptyList());
	}

	public static ChoiceBoxRepresentation<String> makeRegionSelection(Supplier<String> getter, Consumer<String> setter,
			ObservableList<String> systems) {
		ChoiceBoxRepresentation<String> copyRegion = new ChoiceBoxRepresentation<>(getter, setter, Region.load().keySet());
		copyRegion.getBox().setTooltip(new Tooltip("region where we place the copy jobs"));
		copyRegion.getBox().valueProperty().addListener((ob, old, now) -> {
			Region nowR = null;
			if (now != null) {
				nowR = Region.load().get(now);
			}
			systems.clear();
			if (nowR != null) {
				nowR.system().forEach(systems::add);
			}
			Collections.sort(systems);
		});
		return copyRegion;
	}

}
