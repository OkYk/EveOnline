package fr.guiguilechat.eveonline.model.sde.items.types.owner;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.types.Owner;
import org.yaml.snakeyaml.Yaml;

public class Corporation
    extends Owner
{
    public final static String RESOURCE_PATH = "SDE/items/owner/Corporation.yaml";
    private static LinkedHashMap<String, Corporation> cache = (null);

    @Override
    public int getGroupId() {
        return  2;
    }

    @Override
    public Class<?> getGroup() {
        return Corporation.class;
    }

    public static synchronized LinkedHashMap<String, Corporation> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(Corporation.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, Corporation> items;
    }
}
