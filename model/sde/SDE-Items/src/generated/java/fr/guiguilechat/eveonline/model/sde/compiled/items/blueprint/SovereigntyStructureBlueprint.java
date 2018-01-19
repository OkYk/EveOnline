
package fr.guiguilechat.eveonline.model.sde.compiled.items.blueprint;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.compiled.annotations.DefaultValue;
import fr.guiguilechat.eveonline.model.sde.compiled.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.compiled.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.compiled.items.Blueprint;
import org.yaml.snakeyaml.Yaml;

public class SovereigntyStructureBlueprint
    extends Blueprint
{

    /**
     * This is a bookkeeping attribute for blueprints, which will hopefully be deprecated by the end of 2014
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double IndustryBlueprintRank;
    public final static String RESOURCE_PATH = "SDE/items/blueprint/SovereigntyStructureBlueprint.yaml";
    private static LinkedHashMap<String, SovereigntyStructureBlueprint> cache = (null);

    @Override
    public int getGroupId() {
        return  1045;
    }

    @Override
    public Class<?> getGroup() {
        return SovereigntyStructureBlueprint.class;
    }

    public static LinkedHashMap<String, SovereigntyStructureBlueprint> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(SovereigntyStructureBlueprint.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, SovereigntyStructureBlueprint> items;

    }

}