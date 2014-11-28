package com.wadiyan.runemate.scripts.airrunecrafter;

import com.wadiyan.runemate.util.Painter;

/**
 * @author: Supreme Leader
 */
public class AirRcPaint extends Painter {

    private final String[] paintData;
    private int runesCrafted = 0;
    private String status = "Loading...";

    public AirRcPaint() {
        super("Air Runecrafter", 1.01);
        paintData = new String[5];
    }

    @Override
    public String[] paintData() {
        paintData[0] = "Time Ran: " + getFormatedRunTime();
        paintData[1] = "Status:";
        paintData[2] = SPACE + status;
        paintData[3] = "Runes Crafted:";
        paintData[4] = SPACE + formatNumber(runesCrafted) + " (" + formatedPerHour(runesCrafted) + " P/H)";
        return paintData;
    }

    public void addToRunesCrafted (int amount) {
        runesCrafted += amount;
    }

    public void setStatus (String status) {
        this.status = status;
    }
}
