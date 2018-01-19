
package fr.guiguilechat.eveonline.model.sde.compiled.items.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.compiled.items.Entity;
import org.yaml.snakeyaml.Yaml;

public class MissionCONCORDFrigate
    extends Entity
{

    public final static String RESOURCE_PATH = "SDE/items/entity/MissionCONCORDFrigate.yaml";
    private static LinkedHashMap<String, MissionCONCORDFrigate> cache = (null);

    @Override
    public int getGroupId() {
        return  693;
    }

    @Override
    public Class<?> getGroup() {
        return MissionCONCORDFrigate.class;
    }

    public static LinkedHashMap<String, MissionCONCORDFrigate> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(MissionCONCORDFrigate.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, MissionCONCORDFrigate> items;

    }

}