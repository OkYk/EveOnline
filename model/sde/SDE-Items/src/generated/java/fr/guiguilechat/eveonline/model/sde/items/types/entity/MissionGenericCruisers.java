package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.types.Entity;
import org.yaml.snakeyaml.Yaml;

public class MissionGenericCruisers
    extends Entity
{
    public final static String RESOURCE_PATH = "SDE/items/entity/MissionGenericCruisers.yaml";
    private static LinkedHashMap<String, MissionGenericCruisers> cache = (null);

    @Override
    public int getGroupId() {
        return  817;
    }

    @Override
    public Class<?> getGroup() {
        return MissionGenericCruisers.class;
    }

    public static synchronized LinkedHashMap<String, MissionGenericCruisers> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(MissionGenericCruisers.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, MissionGenericCruisers> items;
    }
}
