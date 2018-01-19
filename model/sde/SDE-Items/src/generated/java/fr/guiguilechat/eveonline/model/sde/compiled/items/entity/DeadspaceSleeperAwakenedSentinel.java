
package fr.guiguilechat.eveonline.model.sde.compiled.items.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.compiled.items.Entity;
import org.yaml.snakeyaml.Yaml;

public class DeadspaceSleeperAwakenedSentinel
    extends Entity
{

    public final static String RESOURCE_PATH = "SDE/items/entity/DeadspaceSleeperAwakenedSentinel.yaml";
    private static LinkedHashMap<String, DeadspaceSleeperAwakenedSentinel> cache = (null);

    @Override
    public int getGroupId() {
        return  960;
    }

    @Override
    public Class<?> getGroup() {
        return DeadspaceSleeperAwakenedSentinel.class;
    }

    public static LinkedHashMap<String, DeadspaceSleeperAwakenedSentinel> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(DeadspaceSleeperAwakenedSentinel.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, DeadspaceSleeperAwakenedSentinel> items;

    }

}