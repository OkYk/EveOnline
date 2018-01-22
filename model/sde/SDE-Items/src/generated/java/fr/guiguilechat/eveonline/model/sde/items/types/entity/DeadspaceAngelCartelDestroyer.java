package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.types.Entity;
import org.yaml.snakeyaml.Yaml;

public class DeadspaceAngelCartelDestroyer
    extends Entity
{
    public final static String RESOURCE_PATH = "SDE/items/entity/DeadspaceAngelCartelDestroyer.yaml";
    private static LinkedHashMap<String, DeadspaceAngelCartelDestroyer> cache = (null);

    @Override
    public int getGroupId() {
        return  596;
    }

    @Override
    public Class<?> getGroup() {
        return DeadspaceAngelCartelDestroyer.class;
    }

    public static LinkedHashMap<String, DeadspaceAngelCartelDestroyer> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(DeadspaceAngelCartelDestroyer.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, DeadspaceAngelCartelDestroyer> items;
    }
}
