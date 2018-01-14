
package fr.guiguilechat.eveonline.model.sde.compiled.items.apparel;

import java.io.FileReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.compiled.items.Apparel;
import org.yaml.snakeyaml.Yaml;

public class Headwear
    extends Apparel
{

    public final static String RESOURCE_PATH = "SDE/apparel/Headwear.yaml";
    private static LinkedHashMap<String, Headwear> cache = (null);

    @Override
    public int getGroupId() {
        return  1092;
    }

    @Override
    public Class<?> getGroup() {
        return Headwear.class;
    }

    public static LinkedHashMap<String, Headwear> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new FileReader((RESOURCE_PATH)), (Container.class)).items;
            } catch (Exception _x) {
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, Headwear> items;

    }

}