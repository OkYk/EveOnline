package fr.guiguilechat.eveonline.model.sde.items.types.celestial;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.types.Celestial;
import org.yaml.snakeyaml.Yaml;

public class CosmicAnomaly
    extends Celestial
{
    public final static String RESOURCE_PATH = "SDE/items/celestial/CosmicAnomaly.yaml";
    private static LinkedHashMap<String, CosmicAnomaly> cache = (null);

    @Override
    public int getGroupId() {
        return  885;
    }

    @Override
    public Class<?> getGroup() {
        return CosmicAnomaly.class;
    }

    public static synchronized LinkedHashMap<String, CosmicAnomaly> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(CosmicAnomaly.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, CosmicAnomaly> items;
    }
}
