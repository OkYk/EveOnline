package fr.guiguilechat.eveonline.model.sde.items.types.entity;

import fr.guiguilechat.eveonline.model.sde.items.types.Entity;

public class IrregularTitan
    extends Entity
{

    @Override
    public int getGroupId() {
        return  1759;
    }

    @Override
    public Class<?> getGroup() {
        return IrregularTitan.class;
    }
}
