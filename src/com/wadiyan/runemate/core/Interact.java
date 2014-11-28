package com.wadiyan.runemate.core;

import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.util.calculations.Distance;

/**
 * @author: Supreme Leader
 */
public class Interact {

    public static boolean locatable (LocatableEntity obj, String action) {
        if (obj != null) {
            if (Distance.to(obj) < 5) {
                if (!obj.isVisible()){
                    Camera.turnTo(obj);
                    return false;
                } else {
                    return obj.interact(action);
                }
            } else {
                BresenhamPath.buildTo(obj).step();
                return false;
            }
        } else {
            return false;
        }
    }
}
