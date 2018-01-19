
package fr.guiguilechat.eveonline.model.sde.compiled.items.celestial;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.compiled.items.Celestial;
import org.yaml.snakeyaml.Yaml;

public class Ring
    extends Celestial
{

    public final static String RESOURCE_PATH = "SDE/items/celestial/Ring.yaml";
    private static LinkedHashMap<String, Ring> cache = (null);

    @Override
    public int getGroupId() {
        return  13;
    }

    @Override
    public Class<?> getGroup() {
        return Ring.class;
    }

    public static LinkedHashMap<String, Ring> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(Ring.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, Ring> items;

    }

}