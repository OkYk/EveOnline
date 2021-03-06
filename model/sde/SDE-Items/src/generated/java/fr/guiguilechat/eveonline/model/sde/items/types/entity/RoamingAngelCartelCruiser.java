package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.types.Entity;
import org.yaml.snakeyaml.Yaml;

public class RoamingAngelCartelCruiser
    extends Entity
{
    public final static String RESOURCE_PATH = "SDE/items/entity/RoamingAngelCartelCruiser.yaml";
    private static LinkedHashMap<String, RoamingAngelCartelCruiser> cache = (null);

    @Override
    public int getGroupId() {
        return  1728;
    }

    @Override
    public Class<?> getGroup() {
        return RoamingAngelCartelCruiser.class;
    }

    public static synchronized LinkedHashMap<String, RoamingAngelCartelCruiser> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(RoamingAngelCartelCruiser.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, RoamingAngelCartelCruiser> items;
    }
}
