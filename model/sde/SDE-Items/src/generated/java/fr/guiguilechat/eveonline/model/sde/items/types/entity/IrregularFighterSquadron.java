package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import fr.guiguilechat.eveonline.model.sde.items.types.Entity;

public class IrregularFighterSquadron
    extends Entity
{

    @Override
    public int getGroupId() {
        return  1455;
    }

    @Override
    public Class<?> getGroup() {
        return IrregularFighterSquadron.class;
    }
}
