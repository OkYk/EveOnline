package fr.guiguilechat.eveonline.model.sde.items.types.implant;

import fr.guiguilechat.eveonline.model.sde.items.types.Implant;

public class CyberDrones
    extends Implant
{

    @Override
    public int getGroupId() {
        return  739;
    }

    @Override
    public Class<?> getGroup() {
        return CyberDrones.class;
    }
}
