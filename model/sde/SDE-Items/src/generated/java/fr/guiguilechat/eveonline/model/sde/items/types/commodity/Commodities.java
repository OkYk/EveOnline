
package fr.guiguilechat.eveonline.model.sde.items.types.commodity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.Commodity;
import org.yaml.snakeyaml.Yaml;

public class Commodities
    extends Commodity
{

    /**
     * The type of damage cloud generated by the asteroid.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double DamageCloudType;
    /**
     * Required skill level for skill 1
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double RequiredSkill1Level;
    /**
     * The type ID of the skill that is required.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double RequiredSkill1;
    /**
     * The skill required to reprocess this ore type.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double ReprocessingSkillType;
    /**
     * The maximum hitpoints of an object.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double Hp;
    /**
     * %chance of new asteroid releasing damage cloud each mining turn.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double DamageCloudChance;
    public final static String RESOURCE_PATH = "SDE/items/commodity/Commodities.yaml";
    private static LinkedHashMap<String, Commodities> cache = (null);

    @Override
    public int getGroupId() {
        return  526;
    }

    @Override
    public Class<?> getGroup() {
        return Commodities.class;
    }

    public static LinkedHashMap<String, Commodities> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(Commodities.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, Commodities> items;

    }

}