
package fr.guiguilechat.eveonline.model.sde.items.types.deployable;

import fr.guiguilechat.eveonline.model.sde.items.types.Deployable;

public class ObservatoryStructures
    extends Deployable
{


    @Override
    public int getGroupId() {
        return  1312;
    }

    @Override
    public Class<?> getGroup() {
        return ObservatoryStructures.class;
    }

}