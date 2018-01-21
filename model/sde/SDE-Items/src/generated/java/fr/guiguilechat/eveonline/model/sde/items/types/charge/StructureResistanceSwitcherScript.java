
package fr.guiguilechat.eveonline.model.sde.items.types.charge;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.Charge;
import org.yaml.snakeyaml.Yaml;

public class StructureResistanceSwitcherScript
    extends Charge
{

    /**
     * Sets Explosive damage taken by Armor. 
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double ArmorExplosiveDamageResonancePostAssignment;
    /**
     * The size of the charges that can fit in the turret/whatever.
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(0.0D)
    public double ChargeSize;
    /**
     * Sets Kinetic damage taken by Armor. 
     * 
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double ArmorKineticDamageResonancePostAssignment;
    /**
     * Sets Thermal damage taken by Armor. 
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double ArmorThermalDamageResonancePostAssignment;
    /**
     * Sets Em damage taken by Shields. 
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double ShieldEmDamageResonancePostAssignment;
    /**
     * Sets Explosive damage taken by shields. 
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double ShieldExplosiveDamageResonancePostAssignment;
    /**
     * Sets kinetic damage taken by Shields. 
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double ShieldKineticDamageResonancePostAssignment;
    /**
     * Sets Thermal damage taken by Shields. 
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double ShieldThermalDamageResonancePostAssignment;
    /**
     * Sets Em damage taken by Hull. 
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double EmDamageResonancePostAssignment;
    /**
     * Sets Explosive damage taken by Hull. 
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double ExplosiveDamageResonancePostAssignment;
    /**
     * Sets Thermal damage taken by Hull. 
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double ThermalDamageResonancePostAssignment;
    /**
     * Sets Kinetic damage taken by Hull. 
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double KineticDamageResonancePostAssignment;
    /**
     * Sets Em damage taken by Armor. 
     * 
     */
    @HighIsGood(false)
    @Stackable(true)
    @DefaultValue(1.0D)
    public double ArmorEmDamageResonancePostAssignment;
    public final static String RESOURCE_PATH = "SDE/items/charge/StructureResistanceSwitcherScript.yaml";
    private static LinkedHashMap<String, StructureResistanceSwitcherScript> cache = (null);

    @Override
    public int getGroupId() {
        return  1559;
    }

    @Override
    public Class<?> getGroup() {
        return StructureResistanceSwitcherScript.class;
    }

    public static LinkedHashMap<String, StructureResistanceSwitcherScript> load() {
        if ((cache==null)) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(StructureResistanceSwitcherScript.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {

        public LinkedHashMap<String, StructureResistanceSwitcherScript> items;

    }

}