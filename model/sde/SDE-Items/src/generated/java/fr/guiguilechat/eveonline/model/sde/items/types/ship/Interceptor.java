
package fr.guiguilechat.eveonline.model.sde.items.types.ship;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultDoubleValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultIntValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.Ship;
import org.yaml.snakeyaml.Yaml;

public class Interceptor
    extends Ship
{

    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int EliteBonusInterceptorRole;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int WarpBubbleImmune;
    /**
     * How many upgrades can by fitted to this ship.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int UpgradeSlotsLeft;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RigSize;
    /**
     * The number of low power slots on the ship.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int LowSlots;
    /**
     * tbd
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int MedSlots;
    /**
     * tbd
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int HiSlots;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int FwLpKill;
    /**
     * Required skill level for skill 1
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RequiredSkill1Level;
    /**
     * Deprecated.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(1)
    public int MaxDirectionalVelocity;
    /**
     * Required skill level for skill 2
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RequiredSkill2Level;
    /**
     * Deprecated.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(1.0D)
    public double MinTargetVelDmgMultiplier;
    /**
     * Required skill level for skill 3
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RequiredSkill3Level;
    /**
     * Fixed Role Bonus on a ship.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(0.0D)
    public double ShipBonusRole7;
    /**
     * meta group of type
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int MetaGroupID;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(0.0D)
    public double EliteBonusInterceptor2;
    /**
     * The resolution that the vessel can target other objects at.
     * 
     */
    @HighIsGood(true)
    @Stackable(false)
    @DefaultIntValue(0)
    public int ScanResolution;
    /**
     * The type ID of the skill that is required.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RequiredSkill1;
    /**
     * The type ID of the skill that is required.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RequiredSkill2;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int EliteBonusInterceptor;
    /**
     * The type ID of the skill that is required.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RequiredSkill3;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(0.0D)
    public double ShipBonusGF2;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(0.0D)
    public double ShipBonusMF2;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(0.0D)
    public double ShipBonusCF2;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(0.0D)
    public double ShipBonusMF;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(0.0D)
    public double ShipBonusGF;
    /**
     * scanning speed in milliseconds
     * 
     */
    @HighIsGood(false)
    @Stackable(false)
    @DefaultIntValue(0)
    public int ScanSpeed;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int ShipBonusCF;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(0.0D)
    public double ShipBonusAF;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(5.0D)
    public double ShipBonus2AF;
    /**
     * The value of this attribute is a graphicsID which controls the color scheme of this type. It is used to apply said color scheme to items of other types whose gfx representation is tied in with the attribute holder. Example: Turrets on ships.
     * 
     */
    @HighIsGood(false)
    @Stackable(false)
    @DefaultIntValue(0)
    public int TypeColorScheme;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(1.0D)
    public double HeatAttenuationHi;
    /**
     * Attribute on ships used for ship upgrades
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int UpgradeCapacity;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(1.0D)
    public double HeatAttenuationMed;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(1.0D)
    public double HeatAttenuationLow;
    /**
     * The number of rig slots on the ship.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RigSlots;
    /**
     * 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int EntosisCPUPenalty;
    /**
     * The main color of a ship type.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int MainColor;
    /**
     * Chance of being able to resist a ship scan.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int ShipScanResistance;
    public final static String RESOURCE_PATH = "SDE/items/ship/Interceptor.yaml";
    private static LinkedHashMap<String, Interceptor> cache = (null);

    @Override
    public int getGroupId() {
        return  831;
    }

    @Override
    public Class<?> getGroup() {
        return Interceptor.class;
    }

    public static LinkedHashMap<String, Interceptor> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(Interceptor.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, Interceptor> items;

    }

}
