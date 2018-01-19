
package fr.guiguilechat.eveonline.model.sde.compiled.items.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.compiled.items.Entity;
import org.yaml.snakeyaml.Yaml;

public class AsteroidAngelCartelHauler
    extends Entity
{

    public final static String RESOURCE_PATH = "SDE/items/entity/AsteroidAngelCartelHauler.yaml";
    private static LinkedHashMap<String, AsteroidAngelCartelHauler> cache = (null);

    @Override
    public int getGroupId() {
        return  554;
    }

    @Override
    public Class<?> getGroup() {
        return AsteroidAngelCartelHauler.class;
    }

    public static LinkedHashMap<String, AsteroidAngelCartelHauler> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(AsteroidAngelCartelHauler.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, AsteroidAngelCartelHauler> items;

    }

}