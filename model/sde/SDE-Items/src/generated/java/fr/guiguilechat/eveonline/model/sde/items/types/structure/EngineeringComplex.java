package fr.guiguilechat.eveonline.model.sde.items.types.structure;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultDoubleValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultIntValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.Structure;
import org.yaml.snakeyaml.Yaml;

public class EngineeringComplex
    extends Structure
{
    /**
     * How many upgrades can by fitted to this ship.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int UpgradeSlotsLeft;
    /**
     * Defines whether an entity can be hacked or not.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int Hackable;
    /**
     * This defines the total capacity of fighters allowed in the fighter bay of the ship
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int FighterCapacity;
    /**
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int ServiceSlots;
    /**
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RigSize;
    /**
     * The maximum possible target range.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(300000)
    public int MaximumRangeCap;
    /**
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int StructureServiceRoleBonus;
    /**
     * This defines the total number of fighter launch tubes on the ship.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int FighterTubes;
    /**
     * Material bonus for Engineering Complexes Structures
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(1.0)
    public double StrEngMatBonus;
    /**
     * Cost bonus for Engineering Complexes Structures
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(1.0)
    public double StrEngCostBonus;
    /**
     * Time bonus for Engineering Complexes Structures
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultDoubleValue(1.0)
    public double StrEngTimeBonus;
    /**
     * Number of Light Fighters the structure can launch.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int FighterStandupLightSlots;
    /**
     * Number of Support Fighters the structure can launch.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int FighterStandupSupportSlots;
    /**
     * Number of Heavy Fighters the structure can launch.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int FighterStandupHeavySlots;
    /**
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultIntValue(1)
    public int StructureFullPowerStateHitpointMultiplier;
    /**
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultIntValue(0)
    public int StructureAoERoFRoleBonus;
    /**
     * Missile damage attribute used by structures as a workaround for implementing Standup BCS stacking penalties
     */
    @HighIsGood(true)
    @Stackable(false)
    @DefaultIntValue(1)
    public int HiddenMissileDamageMultiplier;
    /**
     * Number of hours of vulnerability each week required. Applies only to categoryStructure.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int VulnerabilityRequired;
    /**
     * Armor hitpoint attribute used by structures as a workaround for implementing Standup layered plating stacking penalties
     */
    @HighIsGood(true)
    @Stackable(false)
    @DefaultIntValue(1)
    public int HiddenArmorHPMultiplier;
    /**
     * Distance which tethering will engage / disengage piloted ships.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int TetheringRange;
    /**
     * The number of remaining unused launcher slots.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int LauncherSlotsLeft;
    /**
     * Attribute on ships used for ship upgrades
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int UpgradeCapacity;
    /**
     * The number of rig slots on the ship.
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int RigSlots;
    /**
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int TierDifficulty;
    public final static String RESOURCE_PATH = "SDE/items/structure/EngineeringComplex.yaml";
    private static LinkedHashMap<String, EngineeringComplex> cache = (null);

    @Override
    public int getGroupId() {
        return  1404;
    }

    @Override
    public Class<?> getGroup() {
        return EngineeringComplex.class;
    }

    public static synchronized LinkedHashMap<String, EngineeringComplex> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(EngineeringComplex.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, EngineeringComplex> items;
    }
}
