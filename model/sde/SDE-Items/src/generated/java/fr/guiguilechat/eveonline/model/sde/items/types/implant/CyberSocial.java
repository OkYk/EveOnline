
package fr.guiguilechat.eveonline.model.sde.items.types.implant;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.Implant;
import org.yaml.snakeyaml.Yaml;

public class CyberSocial
    extends Implant
{

    /**
     * Autogenerated skill attribute, NegotiationBonus
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double NegotiationBonus;
    /**
     * Tech level of an item
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double TechLevel;
    /**
     * Autogenerated skill attribute, socialMutator
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double SocialMutator;
    /**
     * Bonus To standing gain towards non CONCORD npcs  
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(100.0D)
    public double SocialBonus;
    /**
     * Whether an item is an implant or not
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double Implantness;
    /**
     * Autogenerated skill attribute, connectionBonusMutator
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double ConnectionBonusMutator;
    /**
     * Autogenerated skill attribute, diplomacyMutator
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double DiplomacyMutator;
    /**
     * Autogenerated skill attribute, fastTalkMutator
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double FastTalkMutator;
    public final static String RESOURCE_PATH = "SDE/items/implant/CyberSocial.yaml";
    private static LinkedHashMap<String, CyberSocial> cache = (null);

    @Override
    public int getGroupId() {
        return  750;
    }

    @Override
    public Class<?> getGroup() {
        return CyberSocial.class;
    }

    public static LinkedHashMap<String, CyberSocial> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(CyberSocial.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, CyberSocial> items;

    }

}