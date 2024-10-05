package ac.grim.grimac.manager.init.load;

import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.HitboxData;

public class CollisionLoader implements Initable {

    @Override
    public void start() {
        LogUtil.info("Loading collisions data...");
        HitboxData.load();
        CollisionData.load();
    }

}
