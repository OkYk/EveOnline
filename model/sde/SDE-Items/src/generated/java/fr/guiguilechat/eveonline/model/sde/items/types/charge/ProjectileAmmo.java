
package fr.guiguilechat.eveonline.model.sde.items.types.charge;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.Charge;
import org.yaml.snakeyaml.Yaml;

public class ProjectileAmmo
    extends Charge
{

    /**
     * The size of the charges that can fit in the turret/whatever.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double ChargeSize;
    /**
     * Just for the UI to display base damage on shield.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double BaseShieldDamage;
    /**
     * Just for the UI to display base damage on armor.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double BaseArmorDamage;
    /**
     * Tech level of an item
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double TechLevel;
    /**
     * One of the groups of launcher this charge can be loaded into.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double LauncherGroup;
    /**
     * For charges, hidden attribute used by sentry guns to modify target pick range.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double EntityFlyRangeMultiplier;
    /**
     * EM damage done.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double EmDamage;
    /**
     * Scale the tracking speed of a weapon.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double TrackingSpeedMultiplier;
    /**
     * Explosive damage done.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double ExplosiveDamage;
    /**
     * Kinetic damage done.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double KineticDamage;
    /**
     * Thermal damage done.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double ThermalDamage;
    /**
     * Multiplier of range the relevant weapon.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double WeaponRangeMultiplier;
    /**
     * The main color of a ship type.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double MainColor;
    /**
     * meta group of type
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double MetaGroupID;
    public final static String RESOURCE_PATH = "SDE/items/charge/ProjectileAmmo.yaml";
    private static LinkedHashMap<String, ProjectileAmmo> cache = (null);

    @Override
    public int getGroupId() {
        return  83;
    }

    @Override
    public Class<?> getGroup() {
        return ProjectileAmmo.class;
    }

    public static LinkedHashMap<String, ProjectileAmmo> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(ProjectileAmmo.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, ProjectileAmmo> items;

    }

}