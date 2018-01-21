
package fr.guiguilechat.eveonline.model.sde.items.types.module;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.Module;
import org.yaml.snakeyaml.Yaml;

public class RigCore
    extends Module
{

    /**
     * How much of the upgrade capacity is used when this is fitted to a ship.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double UpgradeCost;
    /**
     * Bonus to duration.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double DurationBonus;
    /**
     * Autogenerated skill attribute, PowerNeedBonus
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double PowerNeedBonus;
    /**
     * Autogenerated skill attribute, repairBonus
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double RepairBonus;
    /**
     * Autogenerated skill attribute, rangeSkillBonus
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double RangeSkillBonus;
    /**
     * Multiplier to a recharge rate time.
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double ShieldRechargeRateMultiplier;
    /**
     * Autogenerated skill attribute, cpu OutputBonus
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double CpuOutputBonus2;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double RigSize;
    /**
     * Autogenerated skill attribute, rechargeratebonus
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double Rechargeratebonus;
    /**
     * Autogenerated skill attribute, miningAmountBonus
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double MiningAmountBonus;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(3.0D)
    public double MaxGangModules;
    /**
     * Autogenerated skill attribute, cpuNeedBonus
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double CpuNeedBonus;
    /**
     * Autogenerated skill attribute, CapacitorCapacityBonus
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double CapacitorCapacityBonus;
    /**
     * Autogenerated skill attribute, PowerOutputBonus
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double PowerEngineeringOutputBonus;
    /**
     * Autogenerated skill attribute, CapRechargeBonus
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double CapRechargeBonus;
    /**
     * Skill attribute for increasing strength of Propulsion modules.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double PropulsionSkillPropulsionStrengthBonus;
    /**
     * Autogenerated skill attribute, capNeedBonus
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double CapNeedBonus;
    public final static String RESOURCE_PATH = "SDE/items/module/RigCore.yaml";
    private static LinkedHashMap<String, RigCore> cache = (null);

    @Override
    public int getGroupId() {
        return  781;
    }

    @Override
    public Class<?> getGroup() {
        return RigCore.class;
    }

    public static LinkedHashMap<String, RigCore> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(RigCore.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, RigCore> items;

    }

}