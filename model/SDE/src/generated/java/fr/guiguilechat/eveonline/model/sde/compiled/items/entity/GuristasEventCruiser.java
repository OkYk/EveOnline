
package fr.guiguilechat.eveonline.model.sde.compiled.items.entity;

import java.io.FileReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.compiled.items.Entity;
import org.yaml.snakeyaml.Yaml;

public class GuristasEventCruiser
    extends Entity
{

    public final static String RESOURCE_PATH = "SDE/entity/GuristasEventCruiser.yaml";
    private static LinkedHashMap<String, GuristasEventCruiser> cache = (null);

    @Override
    public int getGroupId() {
        return  1665;
    }

    @Override
    public Class<?> getGroup() {
        return GuristasEventCruiser.class;
    }

    public static LinkedHashMap<String, GuristasEventCruiser> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new FileReader((RESOURCE_PATH)), (Container.class)).items;
            } catch (Exception _x) {
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, GuristasEventCruiser> items;

    }

}