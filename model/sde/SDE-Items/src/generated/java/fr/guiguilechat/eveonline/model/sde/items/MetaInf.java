package fr.guiguilechat.eveonline.model.sde.items;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class MetaInf {
    public LinkedHashMap<Integer, String> id2name = new LinkedHashMap<>();
    public LinkedHashMap<String, String> name2group = new LinkedHashMap<>();
    public LinkedHashMap<String, String> group2class = new LinkedHashMap<>();
    private static Map<String, Map<String, ? extends Item>> groupcache = new HashMap<>();
    public final static String RESOURCE_PATH = "SDE/items/metainf.yaml";
    private static MetaInf cache = (null);

    @SuppressWarnings("unchecked")
    public static Item getItem(String name) {
        if (name == null) {
            return null;
        }
        String group = MetaInf.load().name2group.get(name);
        if (group == null) {
            return null;
        }
        Map<String, ? extends Item> map = groupcache.get(group);
        if (map == null) {
            try {
                String className = ("fr.guiguilechat.eveonline.model.sde.items.types."+ group.replaceAll("/", "."));
                Class<?> loadclass = MetaInf.class.getClassLoader().loadClass(className);
                if (loadclass!= null) {
                    map = ((Map<String, ? extends Item> ) loadclass.getMethod("load").invoke(null));
                }
            } catch (final Exception exception) {
                throw new UnsupportedOperationException(exception);
            }
            if (map == null) {
                map = Collections.emptyMap();
            }
            groupcache.put(group, map);
        }
        return map.get(name);
    }

    public static Item getItem(int id) {
        return MetaInf.getItem(MetaInf.load().id2name.get(id));
    }

    public static synchronized MetaInf load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(MetaInf.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), MetaInf.class);
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }
}
