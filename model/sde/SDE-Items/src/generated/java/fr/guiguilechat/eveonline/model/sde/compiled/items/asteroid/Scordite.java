
package fr.guiguilechat.eveonline.model.sde.compiled.items.asteroid;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.compiled.annotations.DefaultValue;
import fr.guiguilechat.eveonline.model.sde.compiled.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.compiled.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.compiled.items.Asteroid;
import org.yaml.snakeyaml.Yaml;

public class Scordite
    extends Asteroid
{

    /**
     * Resistance against Stasis Webifiers
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double StasisWebifierResistance;
    /**
     * What type this type can be compressed into
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double CompressionTypeID;
    /**
     * Number of items needed to be able to compress it
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double CompressionQuantityNeeded;
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
     * Reference for grouping ores in visual displays. All variants of one ore should have the same BasicType ID
     * 
     */
    @HighIsGood(false)
    @Stackable(false)
    @DefaultValue(0.0D)
    public double OreBasicType;
    /**
     * max visual size for asteroids to fit moon chunk
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(16255.0D)
    public double AsteroidMaxRadius;
    /**
     *  0: Mission/NPE Ore
     *  1: Standard Ore/Ice
     *  2: +5% Ore
     *  3: +10% Ore
     *  4: High Quality Ice or Extracted Ore
     *  5: Jackpot Moon Ore
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double AsteroidMetaLevel;
    /**
     * Controls how quickly an asteroid radius increases as its quantity grows.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double AsteroidRadiusGrowthFactor;
    /**
     * Sets the radius of the asteroid ball when it has a quantity of 1 unit
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(90.0D)
    public double AsteroidRadiusUnitSize;
    public final static String RESOURCE_PATH = "SDE/items/asteroid/Scordite.yaml";
    private static LinkedHashMap<String, Scordite> cache = (null);

    @Override
    public int getGroupId() {
        return  460;
    }

    @Override
    public Class<?> getGroup() {
        return Scordite.class;
    }

    public static LinkedHashMap<String, Scordite> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(Scordite.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, Scordite> items;

    }

}