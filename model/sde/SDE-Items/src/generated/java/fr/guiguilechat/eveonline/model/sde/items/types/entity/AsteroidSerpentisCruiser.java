package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.types.Entity;
import org.yaml.snakeyaml.Yaml;

public class AsteroidSerpentisCruiser
    extends Entity
{
    public final static String RESOURCE_PATH = "SDE/items/entity/AsteroidSerpentisCruiser.yaml";
    private static LinkedHashMap<String, AsteroidSerpentisCruiser> cache = (null);

    @Override
    public int getGroupId() {
        return  571;
    }

    @Override
    public Class<?> getGroup() {
        return AsteroidSerpentisCruiser.class;
    }

    public static synchronized LinkedHashMap<String, AsteroidSerpentisCruiser> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(AsteroidSerpentisCruiser.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, AsteroidSerpentisCruiser> items;
    }
}
