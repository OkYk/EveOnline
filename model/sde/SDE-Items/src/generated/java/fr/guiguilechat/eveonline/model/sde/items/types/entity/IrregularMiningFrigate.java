package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import fr.guiguilechat.eveonline.model.sde.items.types.Entity;

public class IrregularMiningFrigate
    extends Entity
{

    @Override
    public int getGroupId() {
        return  1761;
    }

    @Override
    public Class<?> getGroup() {
        return IrregularMiningFrigate.class;
    }
}
