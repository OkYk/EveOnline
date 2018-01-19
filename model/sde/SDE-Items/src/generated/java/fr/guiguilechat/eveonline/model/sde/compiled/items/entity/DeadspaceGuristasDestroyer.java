
package fr.guiguilechat.eveonline.model.sde.compiled.items.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.compiled.items.Entity;
import org.yaml.snakeyaml.Yaml;

public class DeadspaceGuristasDestroyer
    extends Entity
{

    public final static String RESOURCE_PATH = "SDE/items/entity/DeadspaceGuristasDestroyer.yaml";
    private static LinkedHashMap<String, DeadspaceGuristasDestroyer> cache = (null);

    @Override
    public int getGroupId() {
        return  614;
    }

    @Override
    public Class<?> getGroup() {
        return DeadspaceGuristasDestroyer.class;
    }

    public static LinkedHashMap<String, DeadspaceGuristasDestroyer> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(DeadspaceGuristasDestroyer.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, DeadspaceGuristasDestroyer> items;

    }

}