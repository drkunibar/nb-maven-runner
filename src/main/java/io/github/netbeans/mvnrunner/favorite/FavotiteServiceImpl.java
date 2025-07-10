package io.github.netbeans.mvnrunner.favorite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = FavoriteService.class)
public class FavotiteServiceImpl implements FavoriteService {

    private final List<FavoriteChangeListener> listeners;
    private final Map<String, FavoriteDescriptor> descriptorMap;

    public FavotiteServiceImpl() {
        this.descriptorMap = new LinkedHashMap<>();
        this.listeners = new ArrayList<>();
    }

    @Override
    public Collection<FavoriteDescriptor> getFavorites() {
        return descriptorMap.values();
    }

    @Override
    public void addFavorite(@Nonnull FavoriteDescriptor fd) {
        String key = fd.getIdentifier();
        descriptorMap.put(key, fd);
        fireChangeEvent();
    }

    @Override
    public void removeFavorite(String favoriteIdentifier) {
        descriptorMap.remove(favoriteIdentifier);
        fireChangeEvent();
    }

    @Override
    public void fireChangeEvent() {
        for (FavoriteChangeListener listener : listeners) {
            listener.favoriteChanged();
        }
    }

    @Override
    public void addFavoritableListener(@Nonnull FavoriteChangeListener favoriteChangeListener) {
        listeners.add(favoriteChangeListener);
    }

}
