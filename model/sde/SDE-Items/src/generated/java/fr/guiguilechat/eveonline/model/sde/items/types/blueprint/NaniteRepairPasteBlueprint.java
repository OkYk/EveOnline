package fr.guiguilechat.eveonline.model.sde.items.types.blueprint;

import fr.guiguilechat.eveonline.model.sde.items.types.Blueprint;

public class NaniteRepairPasteBlueprint
    extends Blueprint
{

    @Override
    public int getGroupId() {
        return  1046;
    }

    @Override
    public Class<?> getGroup() {
        return NaniteRepairPasteBlueprint.class;
    }
}
