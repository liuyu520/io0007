package com.jn.copy;


import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.Objects;

/**
 * Created by 黄威 on 7/29/16.<br >
 */
public class StripedProgressBarUI extends BasicProgressBarUI {
    private final boolean dir;
    private final boolean slope;
    com.apple.laf.AquaProgressBarUI a;

    protected StripedProgressBarUI(boolean dir, boolean slope) {
        super();
        this.dir = dir;
        this.slope = slope;
    }

    @Override
    protected int getBoxLength(int availableLength, int otherDimension) {
        return availableLength; //(int)Math.round(availableLength/6.0);
    }

    @Override
    public void paintIndeterminate(Graphics g, JComponent c) {
        //if (!(g instanceof Graphics2D)) {
        //    return;
        //}

        Insets b = progressBar.getInsets(); // area for border
        int barRectWidth = progressBar.getWidth() - b.right - b.left;
        int barRectHeight = progressBar.getHeight() - b.top - b.bottom;

        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        // Paint the striped box.
        boxRect = getBox(boxRect);
        if (Objects.nonNull(boxRect)) {
            int w = 10;
            int x = getAnimationIndex();
            GeneralPath p = new GeneralPath();
            if (dir) {
                p.moveTo(boxRect.x, boxRect.y);
                p.lineTo(boxRect.x + w * .5f, boxRect.y + boxRect.height);
                p.lineTo(boxRect.x + w, boxRect.y + boxRect.height);
                p.lineTo(boxRect.x + w * .5f, boxRect.y);
            } else {
                p.moveTo(boxRect.x, boxRect.y + boxRect.height);
                p.lineTo(boxRect.x + w * .5f, boxRect.y + boxRect.height);
                p.lineTo(boxRect.x + w, boxRect.y);
                p.lineTo(boxRect.x + w * .5f, boxRect.y);
            }
            p.closePath();

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(progressBar.getForeground());
            if (slope) {
                for (int i = boxRect.width + x; i > -w; i -= w) {
                    g2.fill(AffineTransform.getTranslateInstance(i, 0).createTransformedShape(p));
                }
            } else {
                for (int i = -x; i < boxRect.width; i += w) {
                    g2.fill(AffineTransform.getTranslateInstance(i, 0).createTransformedShape(p));
                }
            }
            g2.dispose();
        }
    }
}
