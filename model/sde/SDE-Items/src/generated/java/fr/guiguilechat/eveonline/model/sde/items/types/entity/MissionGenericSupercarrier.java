package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.types.Entity;
import org.yaml.snakeyaml.Yaml;

public class MissionGenericSupercarrier
    extends Entity
{
    public final static String RESOURCE_PATH = "SDE/items/entity/MissionGenericSupercarrier.yaml";
    private static LinkedHashMap<String, MissionGenericSupercarrier> cache = (null);

    @Override
    public int getGroupId() {
        return  1465;
    }

    @Override
    public Class<?> getGroup() {
        return MissionGenericSupercarrier.class;
    }

    public static synchronized LinkedHashMap<String, MissionGenericSupercarrier> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(MissionGenericSupercarrier.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, MissionGenericSupercarrier> items;
    }
}
