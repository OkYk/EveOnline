package fr.guiguilechat.eveonline.model.sde.items.types.commodity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultIntValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.Commodity;
import org.yaml.snakeyaml.Yaml;

public class DataInterfaces
    extends Commodity
{
    /**
     * Required skill level for skill 1
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RequiredSkill1Level;
    /**
     * The type ID of the skill that is required.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RequiredSkill1;
    /**
     * The maximum hitpoints of an object.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int Hp;
    /**
     * Used to show usable decryptors when starting reverse engineering based on data interface
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int DecryptorID;
    public final static String RESOURCE_PATH = "SDE/items/commodity/DataInterfaces.yaml";
    private static LinkedHashMap<String, DataInterfaces> cache = (null);

    @Override
    public int getGroupId() {
        return  716;
    }

    @Override
    public Class<?> getGroup() {
        return DataInterfaces.class;
    }

    public static synchronized LinkedHashMap<String, DataInterfaces> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(DataInterfaces.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, DataInterfaces> items;
    }
}
