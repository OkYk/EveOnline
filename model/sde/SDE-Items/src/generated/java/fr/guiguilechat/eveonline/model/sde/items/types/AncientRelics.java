package fr.guiguilechat.eveonline.model.sde.items.types;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import fr.guiguilechat.eveonline.model.sde.items.Item;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultDoubleValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.ancientrelics.SleeperDefensiveRelics;
import fr.guiguilechat.eveonline.model.sde.items.types.ancientrelics.SleeperElectronicsRelics;
import fr.guiguilechat.eveonline.model.sde.items.types.ancientrelics.SleeperEngineeringRelics;
import fr.guiguilechat.eveonline.model.sde.items.types.ancientrelics.SleeperHullRelics;
import fr.guiguilechat.eveonline.model.sde.items.types.ancientrelics.SleeperOffensiveRelics;
import fr.guiguilechat.eveonline.model.sde.items.types.ancientrelics.SleeperPropulsionRelics;

public abstract class AncientRelics
    extends Item
{
    /**
     * This is a bookkeeping attribute for blueprints, which will hopefully be deprecated by the end of 2014
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(0.0)
    public double IndustryBlueprintRank;

    @Override
    public int getCategoryId() {
        return  34;
    }

    @Override
    public Class<?> getCategory() {
        return AncientRelics.class;
    }

    public static Map<String, ? extends AncientRelics> loadCategory() {
        return Stream.of(SleeperDefensiveRelics.load(), SleeperElectronicsRelics.load(), SleeperEngineeringRelics.load(), SleeperHullRelics.load(), SleeperOffensiveRelics.load(), SleeperPropulsionRelics.load()).flatMap((m -> m.entrySet().stream())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
