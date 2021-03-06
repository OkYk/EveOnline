package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.types.Entity;
import org.yaml.snakeyaml.Yaml;

public class DeadspaceGuristasFrigate
    extends Entity
{
    public final static String RESOURCE_PATH = "SDE/items/entity/DeadspaceGuristasFrigate.yaml";
    private static LinkedHashMap<String, DeadspaceGuristasFrigate> cache = (null);

    @Override
    public int getGroupId() {
        return  615;
    }

    @Override
    public Class<?> getGroup() {
        return DeadspaceGuristasFrigate.class;
    }

    public static synchronized LinkedHashMap<String, DeadspaceGuristasFrigate> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(DeadspaceGuristasFrigate.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, DeadspaceGuristasFrigate> items;
    }
}
