package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.types.Entity;
import org.yaml.snakeyaml.Yaml;

public class AsteroidAngelCartelCommanderDestroyer
    extends Entity
{
    public final static String RESOURCE_PATH = "SDE/items/entity/AsteroidAngelCartelCommanderDestroyer.yaml";
    private static LinkedHashMap<String, AsteroidAngelCartelCommanderDestroyer> cache = (null);

    @Override
    public int getGroupId() {
        return  794;
    }

    @Override
    public Class<?> getGroup() {
        return AsteroidAngelCartelCommanderDestroyer.class;
    }

    public static synchronized LinkedHashMap<String, AsteroidAngelCartelCommanderDestroyer> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(AsteroidAngelCartelCommanderDestroyer.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, AsteroidAngelCartelCommanderDestroyer> items;
    }
}
