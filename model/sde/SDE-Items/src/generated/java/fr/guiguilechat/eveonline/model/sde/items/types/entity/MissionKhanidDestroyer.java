package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import fr.guiguilechat.eveonline.model.sde.items.types.Entity;

public class MissionKhanidDestroyer
    extends Entity
{

    @Override
    public int getGroupId() {
        return  688;
    }

    @Override
    public Class<?> getGroup() {
        return MissionKhanidDestroyer.class;
    }
}
