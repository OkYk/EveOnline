
package fr.guiguilechat.eveonline.model.sde.items.types.implant;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultDoubleValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultIntValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.Implant;
import org.yaml.snakeyaml.Yaml;

public class CyberResourceProcessing
    extends Implant
{

    /**
     * Autogenerated skill attribute, miningAmountBonus
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int MiningAmountBonus;
    /**
     * Bonus to duration.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int DurationBonus;
    /**
     * Tech level of an item
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(1)
    public int TechLevel;
    /**
     * Whether an item is an implant or not
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int Implantness;
    /**
     * Autogenerated skill attribute, refiningYieldMutator
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RefiningYieldMutator;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(0.0D)
    public double IceHarvestCycleBonus;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int MiningUpgradeCPUReductionBonus;
    public final static String RESOURCE_PATH = "SDE/items/implant/CyberResourceProcessing.yaml";
    private static LinkedHashMap<String, CyberResourceProcessing> cache = (null);

    @Override
    public int getGroupId() {
        return  1229;
    }

    @Override
    public Class<?> getGroup() {
        return CyberResourceProcessing.class;
    }

    public static LinkedHashMap<String, CyberResourceProcessing> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(CyberResourceProcessing.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, CyberResourceProcessing> items;

    }

}
