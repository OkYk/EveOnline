package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import fr.guiguilechat.eveonline.model.sde.items.types.Entity;

public class IrregularMiningBarge
    extends Entity
{

    @Override
    public int getGroupId() {
        return  1762;
    }

    @Override
    public Class<?> getGroup() {
        return IrregularMiningBarge.class;
    }
}
