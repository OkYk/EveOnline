package fr.guiguilechat.eveonline.model.sde.items.types.charge;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultIntValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.Charge;
import org.yaml.snakeyaml.Yaml;

public class FlexArmorHardenerScript
    extends Charge
{
    /**
     * The size of the charges that can fit in the turret/whatever.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int ChargeSize;
    /**
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int EmDamageResistanceBonusBonus;
    /**
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int ExplosiveDamageResistanceBonusBonus;
    /**
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int KineticDamageResistanceBonusBonus;
    /**
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int ThermalDamageResistanceBonusBonus;
    /**
     * Tech level of an item
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(1)
    public int TechLevel;
    /**
     * One of the groups of launcher this charge can be loaded into.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int LauncherGroup;
    public final static String RESOURCE_PATH = "SDE/items/charge/FlexArmorHardenerScript.yaml";
    private static LinkedHashMap<String, FlexArmorHardenerScript> cache = (null);

    @Override
    public int getGroupId() {
        return  1701;
    }

    @Override
    public Class<?> getGroup() {
        return FlexArmorHardenerScript.class;
    }

    public static synchronized LinkedHashMap<String, FlexArmorHardenerScript> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(FlexArmorHardenerScript.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, FlexArmorHardenerScript> items;
    }
}
