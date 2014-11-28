package com.wadiyan.runemate.scripts.airrunecrafter;

import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.util.calculations.Distance;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingScript;
import com.wadiyan.runemate.core.Interact;

import java.awt.*;
import java.util.concurrent.Callable;

/**
 * @author: Supreme Leader
 */
public class AirRunecrafter extends LoopingScript implements PaintListener {

    /*
    * CONSTANTS / IDS
    **/
//    OBJECT IDS
    public static final int ENTER_ALTAR_ID = 2452;
    public static final int ALTAR_ID = 2478;
    public static final int PORTAL_ID = 2465;
//    ITEM IDS
    public static final int RUNE_ESSENCE = 1436;
    public static final int AIR_RUNE = 556;
//    COORDINATES
    public static final Coordinate BANK_COORDINATE = new Coordinate(3182, 3436, 0);
    public static final Coordinate ALTAR_COORDINATE = new Coordinate(3132, 3404, 0);

    /*
    * PAINT DATA
    * */
    private AirRcPaint paint;
    private String status = "Loading...";


    @Override
    public void onStart(String... args) {
        getEventDispatcher().addListener(this);
        setLoopDelay(200, 300);
        paint = new AirRcPaint();
    }

    @Override
    public void onLoop() {
        if (Inventory.contains(RUNE_ESSENCE)) {
            LocatableEntityQueryResults<GameObject> query =  GameObjects.getLoaded(ALTAR_ID);
            if (query.size() > 0) {
                paint.setStatus("Crafting runes");
                GameObject altar = query.nearest();
                if (Interact.object(altar, "Craft-rune")) {
                    final int count = Inventory.getQuantity(AIR_RUNE);
                    Execution.delayUntil(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            if (!Inventory.contains(RUNE_ESSENCE)){
                                paint.addToRunesCrafted(Inventory.getQuantity(AIR_RUNE) - count);
                                return true;
                            }
                            return false;
                        }
                    });
                }
            } else {
                if (Distance.to(ALTAR_COORDINATE) < 4) {
                    paint.setStatus("Entering altar");
                    LocatableEntityQueryResults<GameObject> queryResults = GameObjects.getLoaded(ENTER_ALTAR_ID);
                    if (queryResults.size() > 0) {
                        GameObject altar = queryResults.nearest();
                        if (Interact.object(altar, "Enter")){
                            Execution.delay(2500, 3500);
                        }
                    }
                } else {
                    paint.setStatus("Walking to ruins");
                    BresenhamPath.buildTo(ALTAR_COORDINATE).step();
                }
            }
        } else {
            LocatableEntityQueryResults<GameObject> query = GameObjects.getLoaded(PORTAL_ID);
            if (query.size() > 0) {
                paint.setStatus("Exiting altar");
                GameObject portal = query.nearest();
                if (Interact.object(portal, "Enter")){
                    Execution.delay(2500, 3500);
                }
            } else {
                if (Distance.to(BANK_COORDINATE) < 4) {
                    if (Bank.isOpen()) {
                        paint.setStatus("Getting essence");
                        if (Inventory.containsAnyExcept(RUNE_ESSENCE)) {
                            Bank.depositInventory();
                        }
                        Bank.withdraw(RUNE_ESSENCE, 0);
                        if (Bank.close()) {
                            Execution.delayUntil(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return Inventory.contains(RUNE_ESSENCE);
                                }
                            }, 2000);
                        }
                    } else {
                        paint.setStatus("Opening bank");
                        Bank.open();
                    }
                } else {
                    paint.setStatus("Walking to bank");
                    BresenhamPath.buildTo(BANK_COORDINATE).step();
                }
            }
        }
    }

    @Override
    public void onPaint(Graphics2D g) {
        if (paint != null) {
            paint.onPaint(g);
        }
    }
}
