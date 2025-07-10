package io.github.netbeans.mvnrunner.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.beans.BeanInfo;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ImageUtils {

    public static Image getImage(@Nullable String iconUri, @Nonnull Image defaultImage) {
        Image result = defaultImage;
        if (iconUri != null) {
            Image loadedImage = ImageUtilities.loadImage(iconUri, false);
            if (loadedImage == null) {
                try {
                    URI imageUri = new URI(iconUri);
                    if (imageUri.isAbsolute()) {
                        loadedImage = ImageIO.read(imageUri.toURL());
                    } else {
                        loadedImage = ImageIO.read(new File(iconUri));
                    }
                } catch (URISyntaxException | IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (loadedImage != null) {
                result = loadedImage;
            }
        }
        return result;
    }

    public static Image getNodeScaledImage(int type, @Nullable String iconUri, @Nonnull Image defaultImage) {
        Image image = getImage(iconUri, defaultImage);
        return switch (type) {
            case BeanInfo.ICON_COLOR_16x16, BeanInfo.ICON_MONO_16x16 -> resize(image, 16, 16);
            case BeanInfo.ICON_COLOR_32x32, BeanInfo.ICON_MONO_32x32 -> resize(image, 32, 32);
            default -> resize(image, 16, 16);
        };
    }

    @Nonnull
    public static Image resize(@Nonnull Image image, int desiredWidth, int desiredHeight) {
        // Get the dimensions of the original image
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        // Calculate the aspect ratio
        double aspectRatio = (double) width / height;
        // Calc width and height but keeping ratio
        if ((desiredWidth / desiredHeight) > aspectRatio) {
            double scale = (double) desiredHeight / height;
            width = (int) (width * scale);
            height = (int) desiredHeight;
        } else if ((desiredWidth / desiredHeight) < aspectRatio) {
            double scale = (double) desiredWidth / width;
            width = (int) desiredWidth;
            height = (int) (height * scale);
        } else {
            // No scaling needed, just resize to the desired size
            width = desiredWidth;
            height = desiredHeight;
        }
        // Create target image
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Get the Graphics2D object for drawing on the BufferedImage
        Graphics2D g2d = (Graphics2D) resizedImage.getGraphics();
        // Set rendering hints to improve image quality
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        // Draw the original image on the resized BufferedImage
        g2d.drawImage(image, 0, 0, width, height, null);
        return resizedImage;
    }
}
