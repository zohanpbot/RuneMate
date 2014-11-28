package com.wadiyan.runemate.core;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.util.calculations.Distance;
import com.runemate.game.api.script.Execution;

/**
 * @author: Supreme Leader
 */
public class Interact {

    public static boolean npc (Npc npc, String action) {
        if (npc == null) {
            return false;
        }
        if (Distance.to(npc) > 6) {
            for (int i = 0; i < 20 && Distance.to(npc) > 6; i++) {
                BresenhamPath.buildTo(npc).step();
                Execution.delay(500, 1500);
            }
        }
        if (!npc.isVisible()) {
            Camera.turnTo(npc);
        }
        return npc.interact(action);
    }

    public static boolean object (GameObject obj, String action) {
        if (obj == null) {
            return false;
        }
        if (Distance.to(obj) > 6) {
            for (int i = 0; i < 20 && Distance.to(obj) > 6; i++) {
                BresenhamPath.buildTo(obj).step();
                Execution.delay(500, 1500);
            }
        }
        if (!obj.isVisible()) {
            Camera.turnTo(obj);
        }
        return obj.interact(action);
    }
}
