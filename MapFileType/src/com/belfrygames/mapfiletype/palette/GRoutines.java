package com.belfrygames.mapfiletype.palette;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *
 * @author tomas
 */
public class GRoutines {

    public static final GraphicsDevice defaultGd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    public static final GraphicsConfiguration graphicsConf = defaultGd.getDefaultConfiguration();

    public static BufferedImage getBuffered(Image i) {
        BufferedImage returned;
        if (i instanceof BufferedImage) {
            returned = (BufferedImage) i;
        } else {
            returned = createImage(i.getWidth(null), i.getHeight(null));
            returned.createGraphics().drawImage(i, 0, 0, null);
        }
        return returned;
    }

    public static BufferedImage createImage(int width, int height) {
        return createImage(width, height, Color.white);
    }

    public static BufferedImage createImage(int width, int height, Color defaultC) {
        BufferedImage returned = defaultGd.getDefaultConfiguration().createCompatibleImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = getFastestGraphic(returned);
        g2.setColor(defaultC);
        g2.fillRect(0, 0, width, height);
        g2.dispose();
        return returned;
    }

    public static Graphics2D getFastestGraphic(BufferedImage bi) {
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        Graphics2D returned = bi.createGraphics();
        returned.setColor(Color.black);
        returned.setRenderingHints(hints);
        return returned;
    }
}
