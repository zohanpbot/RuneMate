package com.wadiyan.runemate.util;

import com.runemate.game.api.client.paint.PaintListener;

import java.awt.*;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author: Supreme Leader
 */
public abstract class Painter implements PaintListener {

    public static final String SPACE = "    ";
    private static final Font LARGE_FONT = new Font("Arial", Font.PLAIN, 20);
    private static final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    private final String name;
    private final double version;
    private final Long startTime;

    public Painter(final String name, final double version) {
        this.name = name;
        this.version = version;
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onPaint(Graphics2D g) {
        drawBackground(g);
        drawText(g);
    }

    protected void drawBackground(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, 200, (paintData().length * 16) + 85);
        g.setStroke(new BasicStroke(2));
        g.setColor(new Color(24, 143, 1));
        g.drawRect(1, 1, 198, (paintData().length * 16) + 85);
    }

    protected void drawText(Graphics2D g) {
        g.setFont(LARGE_FONT);
        g.setColor(new Color(246,255, 250));
        FontMetrics fontMetrics = g.getFontMetrics(LARGE_FONT);
        int width = fontMetrics.stringWidth(name);
        g.drawString(name, 100 - (width / 2), 30);
        g.setFont(REGULAR_FONT);
        fontMetrics = g.getFontMetrics(REGULAR_FONT);
        width = fontMetrics.stringWidth("v" + version);
        g.drawString("v" + version , 100 - (width / 2), 45);
        width = fontMetrics.stringWidth("Dev: Supreme Leader");
        g.drawString("Dev: Supreme Leader" , 100 - (width / 2), 60);
        for (int i = 0; i < paintData().length; i++) {
            g.drawString(paintData()[i], 8, 85 + (i * 16));
        }
        g.setColor(new Color(24, 143, 1));
        g.setStroke(new BasicStroke(2));
        g.drawLine(8, 65, 192, 65);
    }

    protected int perHour(int val) {
        return (int) ((val) * 3600000D / (System.currentTimeMillis() - startTime));
    }

    protected String formatTime(long time) {
        String hms = String.format(
                "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                        .toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes(time)));
        return hms;
    }

    protected String formatNumber(int i) {
        return NumberFormat.getIntegerInstance().format(i);
    }

    protected String formatedPerHour (int i) {
        return formatNumber(perHour(i));
    }

    protected long getRunTimer () {
        return System.currentTimeMillis() - startTime;
    }

    protected String getFormatedRunTime () {
        return formatTime(getRunTimer());
    }

    public abstract String[] paintData();
}
