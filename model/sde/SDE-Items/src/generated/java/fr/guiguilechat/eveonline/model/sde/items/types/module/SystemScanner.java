package fr.guiguilechat.eveonline.model.sde.items.types.module;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultDoubleValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultIntValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.Module;
import org.yaml.snakeyaml.Yaml;

public class SystemScanner
    extends Module
{
    /**
     * CPU need of module
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultDoubleValue(0.0)
    public double Cpu;
    /**
     * The amount of charge used from the capacitor for a module activation.
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultDoubleValue(0.0)
    public double CapacitorNeed;
    /**
     * Length of activation time.
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultDoubleValue(0.0)
    public double Duration;
    /**
     * Maximum modules of same group that can be activated at same time, 0 = no limit, 1 = 1
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int MaxGroupActive;
    /**
     * Effective range of scanner in multiples of AUs
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(10)
    public int ScanRange;
    /**
     * current power need
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultIntValue(0)
    public int Power;
    public final static String RESOURCE_PATH = "SDE/items/module/SystemScanner.yaml";
    private static LinkedHashMap<String, SystemScanner> cache = (null);

    @Override
    public int getGroupId() {
        return  472;
    }

    @Override
    public Class<?> getGroup() {
        return SystemScanner.class;
    }

    public static synchronized LinkedHashMap<String, SystemScanner> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(SystemScanner.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, SystemScanner> items;
    }
}
