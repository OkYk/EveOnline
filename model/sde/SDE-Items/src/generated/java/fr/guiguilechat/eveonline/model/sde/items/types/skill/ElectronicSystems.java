package fr.guiguilechat.eveonline.model.sde.items.types.skill;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultDoubleValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultIntValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.Skill;
import org.yaml.snakeyaml.Yaml;

public class ElectronicSystems
    extends Skill
{
    /**
     * Skill attribute for increasing effectiveness on Target Painters
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int ScanSkillTargetPaintStrengthBonus;
    /**
     * Bonus to duration.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int DurationBonus;
    /**
     * Autogenerated skill attribute, rangeSkillBonus
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RangeSkillBonus;
    /**
     * Bonus to duration of modules requiring projected ECM.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int ProjECMDurationBonus;
    /**
     * Autogenerated skill attribute, cloakVelocityBonus
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int CloakVelocityBonus;
    /**
     * Skill bonus for cloaking targeting delay.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int CloakingTargetingDelayBonus;
    /**
     * Time constant for skill training
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(0.0)
    public double SkillTimeConstant;
    /**
     * Only refers to another dogma attribute.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int PrimaryAttribute;
    /**
     * Required skill level for skill 1
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RequiredSkill1Level;
    /**
     * Only refers to another dogma attribute.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int SecondaryAttribute;
    /**
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int ConsumptionQuantityBonus;
    /**
     * Required skill level for skill 2
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RequiredSkill2Level;
    /**
     * Autogenerated skill attribute, cpuNeedBonus
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int CpuNeedBonus;
    /**
     * The type ID of the skill that is required.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RequiredSkill1;
    /**
     * The type ID of the skill that is required.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RequiredSkill2;
    /**
     * If set to 1 on a skill then this skill can not be trained on accounts that are marked as Trial.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int CanNotBeTrainedOnTrial;
    /**
     * Autogenerated skill attribute, maxTarget Bonus
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int MaxTargetBonus;
    /**
     * Skill attribute for increasing strength of EW modules.
     */
    @HighIsGood(true)
    @Stackable(false)
    @DefaultDoubleValue(0.0)
    public double ScanSkillEwStrengthBonus;
    /**
     * Autogenerated skill attribute, capNeedBonus
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int CapNeedBonus;
    /**
     * Autogenerated skill attribute, falloffBonus
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(0.0)
    public double FalloffBonus;
    public final static String RESOURCE_PATH = "SDE/items/skill/ElectronicSystems.yaml";
    private static LinkedHashMap<String, ElectronicSystems> cache = (null);

    @Override
    public int getGroupId() {
        return  272;
    }

    @Override
    public Class<?> getGroup() {
        return ElectronicSystems.class;
    }

    public static synchronized LinkedHashMap<String, ElectronicSystems> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(ElectronicSystems.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, ElectronicSystems> items;
    }
}
