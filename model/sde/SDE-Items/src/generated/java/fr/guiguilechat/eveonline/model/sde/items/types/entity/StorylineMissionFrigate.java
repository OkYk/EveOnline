package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.types.Entity;
import org.yaml.snakeyaml.Yaml;

public class StorylineMissionFrigate
    extends Entity
{
    public final static String RESOURCE_PATH = "SDE/items/entity/StorylineMissionFrigate.yaml";
    private static LinkedHashMap<String, StorylineMissionFrigate> cache = (null);

    @Override
    public int getGroupId() {
        return  527;
    }

    @Override
    public Class<?> getGroup() {
        return StorylineMissionFrigate.class;
    }

    public static synchronized LinkedHashMap<String, StorylineMissionFrigate> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(StorylineMissionFrigate.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, StorylineMissionFrigate> items;
    }
}
