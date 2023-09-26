package com.kollectivemobile.euki.manager;

import android.util.Pair;

import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.model.TileItem;

import java.util.List;

public interface HomeManager {
    List<TileItem> getMainItems();
    void saveTitle(TileItem contentItem, String title);
    void saveOrder(List<TileItem> usedItems, List<TileItem> notUsedItems);
    Pair<List<TileItem>, List<TileItem>> getHomeOrder();
}
