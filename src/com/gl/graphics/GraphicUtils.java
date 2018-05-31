package com.gl.graphics;

import com.gl.game.tiles.GameTile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.UncheckedIOException;

public final class GraphicUtils {

    public static final int MAX_RGB_VALUE = 255;

    public static Graphics2D getGraphicsWithHints(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        return g2d;
    }

    public static void drawImage(Graphics g, BufferedImage img, int x, int y){
        drawImage(g, img, x, y, 0, 0, img.getWidth(), img.getHeight());
    }

    public static void drawImage(Graphics g, Image img, int x, int y){
        g.drawImage(img, x, y, null);
    }

    public static void drawImage(Graphics g, BufferedImage img, int x, int y, int fromX, int fromY, int width, int height){
        Image cropped = img.getSubimage(fromX, fromY, width, height);
        g.drawImage(cropped, x, y, null);
    }

    public static void fillRect(Graphics g, int x, int y, int width, int height, Color color){
        Color cur = g.getColor();
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(cur);
    }

    public static void drawPolygon(Graphics g, int[] xPoints, int[] yPoints, Color color){
        //this.setAAMode(true);
        Color cur = g.getColor();
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, xPoints.length);
        g.setColor(cur);
        //this.setAAMode(false);
    }

    public static void fillCircle(Graphics g, int x, int y, int diameter, Color color){
        Color cur = g.getColor();
        g.setColor(color);
        g.fillOval(x, y, diameter, diameter);
        g.setColor(cur);
    }

    public static void fillRing(Graphics g, int x, int y, int diameter, int thickness, Color color){
        int innerDiameter = diameter - 2 * thickness;

        // Create ring
        Ellipse2D outer = new Ellipse2D.Double(x, y, diameter, diameter);
        Ellipse2D inner = new Ellipse2D.Double(x + thickness, y + thickness, innerDiameter, innerDiameter);
        Area ringArea = new Area(outer);
        ringArea.subtract(new Area(inner));

        // draw
        Color cur = g.getColor();
        g.setColor(color);
        ((Graphics2D) g).fill(ringArea);
        g.setColor(cur);
    }

    public static void drawString(Graphics g, String str, Font font, int x, int y, Color color){
        g.setFont(font);
        Color cur = g.getColor();
        g.setColor(color);
        g.drawString(str, x, y);
        g.setColor(cur);
    }

    public static void createStar(Graphics2D g, int arms, Point center, double rOuter, double rInner, Color color){
        double angle = Math.PI / arms;

        GeneralPath path = new GeneralPath();

        double offset = -Math.PI / (2 * arms);
        for (int i = 0; i < Math.abs(2 * arms); i++){
            double r = (i & 1) == 0 ? rOuter : rInner;
            Point2D.Double p = new Point2D.Double(center.x + Math.cos(offset + i * angle) * r, center.y + Math.sin(offset + i * angle) * r);
            if (i == 0) path.moveTo(p.getX(), p.getY());
            else path.lineTo(p.getX(), p.getY());
        }

        path.closePath();
        Color cur = g.getColor();
        g.setColor(color);
        g.fill(path);
        g.setColor(cur);
    }

    public static Image getScaledImage(Image img, int newWidth, int newHeight){
        return img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
    }

    public static Image flipHorizontally(Image img){
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        Image flipped = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        flipped.getGraphics().drawImage(img, width, 0, -width, height,null);

        return flipped;
    }

    public static Image rotateBy(Image img, double radians){
        BufferedImage rotated = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = (Graphics2D) rotated.getGraphics();
        g2d.rotate(radians, rotated.getWidth() / 2, rotated.getHeight() / 2);
        g2d.drawImage(img, 0, 0, null);
        return rotated;
    }

    // TODO: save a map with already loaded images to prevent duplicated loads
    public static BufferedImage loadImage(String name){
        try{
            return ImageIO.read(GameTile.class.getClassLoader().getResourceAsStream("com/gl/assets/" + name + ".png"));
        } catch (IOException e){
           throw new UncheckedIOException("Error: could not find " + name + ".png in the 'assets' folder", e);
        }
    }

    public static Image copyImage(Image source){
        BufferedImage b = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = GraphicUtils.getGraphicsWithHints(b.getGraphics());
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public static Color changeTransparency(Color color, int transparency){
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), transparency);
    }

    public static Image paintWithColor(Image img, Color color){
        BufferedImage painted = (BufferedImage) copyImage(img);
        WritableRaster raster = painted.getRaster();

        for (int x = 0; x < img.getWidth(null); x++){
            for (int y = 0; y < img.getHeight(null); y++){
                int[] pixelColors = raster.getPixel(x, y, (int[]) null);
                pixelColors[0] = color.getRed();
                pixelColors[1] = color.getGreen();
                pixelColors[2] = color.getBlue();
                raster.setPixel(x, y, pixelColors);
            }
        }

        return painted;
    }

    public static int getMaxFittingFontSize(Graphics g, Font font, String string, int width, int height){
        int minSize = 0;
        int maxSize = 288;
        int curSize = (minSize + maxSize) / 2;

        while (maxSize - minSize > 2){
            FontMetrics fm = g.getFontMetrics(new Font(font.getName(), font.getStyle(), curSize));
            int fontWidth = fm.stringWidth(string);
            int fontHeight = fm.getLeading() + fm.getMaxAscent() + fm.getMaxDescent();

            if ((fontWidth > width) || (fontHeight > height)){
                maxSize = curSize;
                curSize = (maxSize + minSize) / 2;
            } else {
                minSize = curSize;
                curSize = (minSize + maxSize) / 2;
            }
        }

        return curSize;
    }
}
