package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.types.Entity;
import org.yaml.snakeyaml.Yaml;

public class HiddenZenithGallenteBattleship
    extends Entity
{
    public final static String RESOURCE_PATH = "SDE/items/entity/HiddenZenithGallenteBattleship.yaml";
    private static LinkedHashMap<String, HiddenZenithGallenteBattleship> cache = (null);

    @Override
    public int getGroupId() {
        return  1795;
    }

    @Override
    public Class<?> getGroup() {
        return HiddenZenithGallenteBattleship.class;
    }

    public static synchronized LinkedHashMap<String, HiddenZenithGallenteBattleship> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(HiddenZenithGallenteBattleship.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, HiddenZenithGallenteBattleship> items;
    }
}
