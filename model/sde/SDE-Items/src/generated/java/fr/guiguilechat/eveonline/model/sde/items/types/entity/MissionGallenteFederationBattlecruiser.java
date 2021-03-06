package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import fr.guiguilechat.eveonline.model.sde.items.types.Entity;
import org.yaml.snakeyaml.Yaml;

public class MissionGallenteFederationBattlecruiser
    extends Entity
{
    public final static String RESOURCE_PATH = "SDE/items/entity/MissionGallenteFederationBattlecruiser.yaml";
    private static LinkedHashMap<String, MissionGallenteFederationBattlecruiser> cache = (null);

    @Override
    public int getGroupId() {
        return  681;
    }

    @Override
    public Class<?> getGroup() {
        return MissionGallenteFederationBattlecruiser.class;
    }

    public static synchronized LinkedHashMap<String, MissionGallenteFederationBattlecruiser> load() {
        if (cache == null) {
            try {
                cache = new Yaml().loadAs(new InputStreamReader(MissionGallenteFederationBattlecruiser.class.getClassLoader().getResourceAsStream((RESOURCE_PATH))), (Container.class)).items;
            } catch (final Exception exception) {
                throw new UnsupportedOperationException("catch this", exception);
            }
        }
        return (cache);
    }

    private static class Container {
        public LinkedHashMap<String, MissionGallenteFederationBattlecruiser> items;
    }
}
