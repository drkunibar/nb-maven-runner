package io.github.netbeans.mvnrunner.favorite;

import java.util.Collection;
import javax.annotation.Nonnull;

public interface FavoriteService {

    void addFavorite(@Nonnull FavoriteDescriptor descriptor);

    void removeFavorite(@Nonnull String favoriteIdentifier);

    void addFavoritableListener(@Nonnull FavoriteChangeListener favoriteChangeListener);

    Collection<FavoriteDescriptor> getFavorites();

    void fireChangeEvent();
}
