
package fr.guiguilechat.eveonline.model.sde.compiled.items.commodity;

import java.io.FileReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.compiled.annotations.DefaultValue;
import fr.guiguilechat.eveonline.model.sde.compiled.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.compiled.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.compiled.items.Commodity;
import org.yaml.snakeyaml.Yaml;

public class DecryptorsSleepers
    extends Commodity
{

    /**
     * Modifies the max runs in a blueprint created through invention
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double InventionMaxRunModifier;
    /**
     * Modifies base chance of successful invention
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double InventionPropabilityMultiplier;
    /**
     * Modifies the mineral efficiency of invented BPCs
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double InventionMEModifier;
    /**
     * Modifies the time efficiency of invented BPCs
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double InventionTEModifier;
    public final static String RESOURCE_PATH = "SDE/commodity/DecryptorsSleepers.yaml";
    private static LinkedHashMap<String, DecryptorsSleepers> cache = (null);

    @Override
    public int getGroupId() {
        return  732;
    }

    @Override
    public Class<?> getGroup() {
        return DecryptorsSleepers.class;
    }

    public static LinkedHashMap<String, DecryptorsSleepers> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new FileReader((RESOURCE_PATH)), (Container.class)).items;
            } catch (Exception _x) {
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, DecryptorsSleepers> items;

    }

}