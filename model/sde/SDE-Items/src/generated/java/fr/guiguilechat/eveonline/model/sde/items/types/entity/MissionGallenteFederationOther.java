package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import fr.guiguilechat.eveonline.model.sde.items.types.Entity;

public class MissionGallenteFederationOther
    extends Entity
{

    @Override
    public int getGroupId() {
        return  682;
    }

    @Override
    public Class<?> getGroup() {
        return MissionGallenteFederationOther.class;
    }
}
