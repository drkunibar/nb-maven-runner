package io.github.netbeans.mvnrunner.favorite;

import java.awt.Image;
import java.util.Base64;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.openide.util.ImageUtilities;

import lombok.Value;

import io.github.netbeans.mvnrunner.node.FavoriteListNode;
import io.github.netbeans.mvnrunner.util.ImageUtils;

@Value
public class FavoriteDescriptor {

    @Nonnull
    private final String identifier;
    @Nonnull
    private final String name;
    @Nullable
    private final String imageUri;
    @Nullable
    private final String description;

    public FavoriteDescriptor(String identifier, String name, String imageUri, String description) {
        this.identifier = identifier;
        this.name = name;
        this.imageUri = imageUri;
        this.description = description;
    }

    public Image getIcon(int type) {
        Image result = ImageUtilities.loadImage(FavoriteListNode.FAVORITES_ICON);
        return ImageUtils.getImage(imageUri, result);
    }

    @SuppressWarnings("null")
    public static String serialize(@Nonnull FavoriteDescriptor descriptor) {
        Base64.Encoder encoder = Base64.getEncoder();
        StringBuilder sb = new StringBuilder().append("identifier=")
                .append(encoder.encodeToString(descriptor.getIdentifier().getBytes()))
                .append(",name=")
                .append(encoder.encodeToString(descriptor.getName().getBytes()));
        if (descriptor.getImageUri() != null) {
            sb.append(",imegUri=").append(encoder.encodeToString(descriptor.getImageUri().getBytes()));
        }
        if (descriptor.getDescription() != null) {
            sb.append(",description=").append(encoder.encodeToString(descriptor.getDescription().getBytes()));
        }
        return sb.toString();
    }

    public static FavoriteDescriptor deserialize(@Nonnull String serializedDescriptor) {
        Base64.Decoder decoder = Base64.getDecoder();
        String[] parts = StringUtils.split(serializedDescriptor, ",");
        String identifier = "";
        String name = "";
        String imageUri = null;
        String description = null;
        for (String part : parts) {
            String[] keyValue = StringUtils.split(part, "=");
            if (keyValue.length < 2) {
                continue;
            }
            String key = keyValue[0];
            String value = (keyValue[1] != null) ? new String(decoder.decode(keyValue[1])) : null;
            switch (key) {
                case "identifier" -> identifier = value;
                case "name" -> name = value;
                case "imegUri" -> imageUri = value;
                case "description" -> description = value;
                default -> {
                }
            }
        }
        return new FavoriteDescriptor(identifier, name, imageUri, description);
    }
}
