package xyz.rokkiitt.sector.objects.wings;

import cn.nukkit.entity.data.*;
import xyz.rokkiitt.sector.Main;

import javax.imageio.*;
import java.nio.charset.*;
import java.awt.image.*;
import java.awt.*;
import java.nio.file.*;
import cn.nukkit.nbt.stream.*;
import java.io.*;

public class Wings
{
    private static final int PIXEL_SIZE = 4;
    public static final int SINGLE_SKIN_SIZE = 8192;
    public static final int DOUBLE_SKIN_SIZE = 16384;
    public static final int SKIN_128_64_SIZE = 32768;
    public static final int SKIN_128_128_SIZE = 65536;
    
    public static Skin apply(final Skin skin, final String wings) {
        try {
            final Skin newSkin = new Skin();
            newSkin.setSkinId("winged-" + skin.getSkinId());
            newSkin.setSkinData(skin.getSkinData());
            newSkin.setCapeData(skin.getCapeData());
            final BufferedImage original = toImage(newSkin);
            final BufferedImage wingsImage = ImageIO.read(new File(Main.getPlugin().getDataFolder(), "skins/wings/" + wings + ".png"));
            newSkin.setSkinData(img_tricky(resize_image(original, wingsImage.getWidth(), wingsImage.getHeight()), wingsImage));
            newSkin.setGeometryName("geometry." + wings);
            newSkin.setGeometryData(Files.readString(new File(Main.getPlugin().getDataFolder(), "skins/wings/" + wings + ".json").toPath()));
            return newSkin;
        }
        catch (Exception e) {
            Main.getPlugin().getLogger().alert("Wystapil blad podczas nadawania skrzydel: " + e.getMessage());
            return null;
        }
    }
    
    public static Skin applyIncognito(final Skin skin) {
        try {
            final Skin newSkin = new Skin();
            newSkin.setSkinId(skin.getSkinId());
            newSkin.setSkinData(fromImage(new File(Main.getPlugin().getDataFolder(), "skins/incognito.png").toPath()));
            newSkin.setGeometryName("geometry.default");
            newSkin.setGeometryData(new String(Files.readAllBytes(new File(Main.getPlugin().getDataFolder(), "skins/default.json").toPath()), StandardCharsets.UTF_8));
            return newSkin;
        }
        catch (Exception e) {
            Main.getPlugin().getLogger().alert("Wystapil blad podczas nadawania incognito: " + e.getMessage());
            return null;
        }
    }
    
    public static Skin applyDeafult(final Skin skin) {
        try {
            final Skin newSkin = new Skin();
            newSkin.setSkinId(skin.getSkinId());
            newSkin.setSkinData(fromImage(new File(Main.getPlugin().getDataFolder(), "skins/default.png").toPath()));
            newSkin.setGeometryName("geometry.default");
            newSkin.setGeometryData(Files.readString(new File(Main.getPlugin().getDataFolder(), "skins/default.json").toPath()));
            return newSkin;
        }
        catch (Exception e) {
            Main.getPlugin().getLogger().alert("Wystapil blad podczas nadawania deafultowego skina: " + e.getMessage());
            return null;
        }
    }
    
    public static Skin applyNether(final Skin skin) {
        try {
            final Skin newSkin = new Skin();
            newSkin.setSkinId(skin.getSkinId());
            newSkin.setSkinData(fromImage(new File(Main.getPlugin().getDataFolder(), "skins/nether.png").toPath()));
            newSkin.setGeometryName("geometry.default");
            newSkin.setGeometryData(new String(Files.readAllBytes(new File(Main.getPlugin().getDataFolder(), "skins/default.json").toPath()), StandardCharsets.UTF_8));
            return newSkin;
        }
        catch (Exception e) {
            Main.getPlugin().getLogger().alert("Wystapil blad podczas nadawania deafultowego skina: " + e.getMessage());
            return null;
        }
    }
    
    public static boolean isExists(final String wings) {
        return new File(Main.getPlugin().getDataFolder(), "skins/wings/" + wings + ".png").exists() && new File(Main.getPlugin().getDataFolder(), "skins/wings/" + wings + ".json").exists();
    }
    
    private static BufferedImage img_tricky(final BufferedImage skin, final BufferedImage wings) {
        final Graphics g = skin.getGraphics();
        g.drawImage(wings, 0, 0, null);
        g.dispose();
        return skin;
    }
    
    private static BufferedImage resize_image(final BufferedImage image, final int w, final int h) {
        final BufferedImage dst = new BufferedImage(w, h, 2);
        final Graphics2D g = dst.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return dst;
    }
    
    public static BufferedImage toImage(final Skin skin) {
        final byte[] skindata = skin.getSkinData().data;
        int width;
        int height;
        if (skindata.length == 8192) {
            width = 64;
            height = 32;
        }
        else if (skindata.length == 16384) {
            width = 64;
            height = 64;
        }
        else if (skindata.length == 32768) {
            width = 128;
            height = 64;
        }
        else {
            if (skindata.length != 65536) {
                throw new IllegalStateException("invalid skin");
            }
            width = 128;
            height = 128;
        }
        final BufferedImage image = new BufferedImage(width, height, 6);
        final ByteArrayInputStream data = new ByteArrayInputStream(skindata);
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                final Color color = new Color(data.read(), data.read(), data.read(), data.read());
                image.setRGB(x, y, color.getRGB());
            }
        }
        return image;
    }
    
    public static byte[] fromImage(final Path path) {
        try {
            return parseBufferedImage(ImageIO.read(path.toFile()));
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static byte[] fromImage(final BufferedImage img) {
        try {
            return parseBufferedImage(img);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private static byte[] parseBufferedImage(final BufferedImage image) throws IOException {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        try {
            for (int y = 0; y < image.getHeight(); ++y) {
                for (int x = 0; x < image.getWidth(); ++x) {
                    final Color color = new Color(image.getRGB(x, y), true);
                    out.write(color.getRed());
                    out.write(color.getGreen());
                    out.write(color.getBlue());
                    out.write(color.getAlpha());
                }
            }
            image.flush();
            final byte[] byteArray = out.toByteArray();
            out.close();
            return byteArray;
        }
        catch (Throwable t) {
            try {
                out.close();
            }
            catch (Throwable t2) {
                t.addSuppressed(t2);
            }
            throw t;
        }
    }
}
