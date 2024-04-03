package com.kollectivemobile.euki.manager;

import android.util.Pair;

import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.model.TileItem;
import com.kollectivemobile.euki.model.database.dao.AppSettingsDAO;
import com.kollectivemobile.euki.model.database.entity.AppSettings;

import java.util.ArrayList;
import java.util.List;

public class HomeManagerImpl implements HomeManager {
    private AppSettingsDAO mAppSettingsDAO;
    private List<TileItem> mItems;

    public HomeManagerImpl(AppSettingsDAO appSettingsDAO) {
        this.mAppSettingsDAO = appSettingsDAO;
        this.mItems = new ArrayList<>();
    }

    @Override
    public List<TileItem> getMainItems() {
        AppSettings appSettings = mAppSettingsDAO.getAppSettings();
        List<TileItem> items = new ArrayList<>();
        items.add(new TileItem(0, "menstruation", "ic_main_menstruation"));
        items.add(new TileItem(1, "abortion", "ic_main_abortion"));
        items.add(new TileItem(2, "contraception", "ic_main_contraception"));
        items.add(new TileItem(3, "sexuality", "ic_main_sexuality"));
        items.add(new TileItem(4, "miscarriage", "ic_main_miscarriage"));
        items.add(new TileItem(5, "pregnancy_options", "ic_main_pregnancy"));
        items.add(new TileItem(6, "stis", "ic_main_stis"));

        for (ContentItem item : items) {
            item.setTitle(appSettings.getMainTitles().get(item.getId()));
        }

        mItems.clear();
        mItems.addAll(items);

        return items;
    }

    @Override
    public void saveTitle(TileItem contentItem, String title) {
        AppSettings appSettings = mAppSettingsDAO.getAppSettings();
        appSettings.getMainTitles().put(contentItem.getId(), title);
        mAppSettingsDAO.update(appSettings);
    }

    @Override
    public void saveOrder(List<TileItem> usedItems, List<TileItem> notUsedItems) {
        AppSettings appSettings = mAppSettingsDAO.getAppSettings();
        List<String> usedIds = new ArrayList<>();
        List<String> notUsedIds = new ArrayList<>();

        for (ContentItem contentItem : usedItems) {
            usedIds.add(contentItem.getId());
        }
        for (ContentItem contentItem : notUsedItems) {
            notUsedIds.add(contentItem.getId());
        }

        appSettings.setHomeItemsUsed(usedIds);
        appSettings.setHomeItemsNotUsed(notUsedIds);
        mAppSettingsDAO.update(appSettings);
    }

    @Override
    public Pair<List<TileItem>, List<TileItem>> getHomeOrder() {
        AppSettings appSettings = mAppSettingsDAO.getAppSettings();
        List<TileItem> usedItems = new ArrayList<>();
        List<TileItem> notUsedItems = new ArrayList<>();

        for (String id : appSettings.getHomeItemsUsed()) {
            for (TileItem item : mItems) {
                if (item.getId().equals(id)) {
                    usedItems.add(item);
                    break;
                }
            }
        }

        for (String id : appSettings.getHomeItemsNotUsed()) {
            for (TileItem item : mItems) {
                if (item.getId().equals(id)) {
                    notUsedItems.add(item);
                    break;
                }
            }
        }

        if (usedItems.size() == 0 && notUsedItems.size() == 0) {
            usedItems.addAll(mItems);
        }

        for (TileItem item : usedItems) {
            item.setUsed(true);
        }

        for (TileItem item : notUsedItems) {
            item.setUsed(false);
        }

        return new Pair<>(usedItems, notUsedItems);
    }
}
