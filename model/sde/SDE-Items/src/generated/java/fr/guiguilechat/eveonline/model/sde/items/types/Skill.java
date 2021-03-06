package fr.guiguilechat.eveonline.model.sde.items.types;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import fr.guiguilechat.eveonline.model.sde.items.Item;
import fr.guiguilechat.eveonline.model.sde.items.annotations.DefaultIntValue;
import fr.guiguilechat.eveonline.model.sde.items.annotations.HighIsGood;
import fr.guiguilechat.eveonline.model.sde.items.annotations.Stackable;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Armor;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.CorporationManagement;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Drones;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.ElectronicSystems;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Engineering;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.FakeSkills;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.FleetSupport;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Gunnery;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Missiles;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Navigation;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.NeuralEnhancement;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.PlanetManagement;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Production;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.ResourceProcessing;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Rigging;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Scanning;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Science;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Shields;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Social;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.SpaceshipCommand;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.StructureManagement;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Subsystems;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Targeting;
import fr.guiguilechat.eveonline.model.sde.items.types.skill.Trade;

public abstract class Skill
    extends Item
{
    /**
     * Level of skill
     */
    @HighIsGood(true)
    @Stackable(true)
    @DefaultIntValue(0)
    public int SkillLevel;

    @Override
    public int getCategoryId() {
        return  16;
    }

    @Override
    public Class<?> getCategory() {
        return Skill.class;
    }

    public static Map<String, ? extends Skill> loadCategory() {
        return Stream.of(Armor.load(), CorporationManagement.load(), Drones.load(), ElectronicSystems.load(), Engineering.load(), FakeSkills.load(), FleetSupport.load(), Gunnery.load(), Missiles.load(), Navigation.load(), NeuralEnhancement.load(), PlanetManagement.load(), Production.load(), ResourceProcessing.load(), Rigging.load(), Scanning.load(), Science.load(), Shields.load(), Social.load(), SpaceshipCommand.load(), StructureManagement.load(), Subsystems.load(), Targeting.load(), Trade.load()).flatMap((m -> m.entrySet().stream())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
