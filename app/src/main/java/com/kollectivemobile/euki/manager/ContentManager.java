package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.networking.EukiCallback;

import java.util.List;

public interface ContentManager {
    void getAbortionKnowledge(EukiCallback<ContentItem> callback);
    void getAbortionWalkthrough(EukiCallback<ContentItem> callback);
    void getSTIs(EukiCallback<ContentItem> callback);
    void getContraception(EukiCallback<ContentItem> callback);
    void getMiscarriage(EukiCallback<ContentItem> callback);
    void getSexRelationships(EukiCallback<ContentItem> callback);
    void getPregnancyOptions(EukiCallback<ContentItem> callback);
    void getMenstruationOptions(EukiCallback<ContentItem> callback);

    ContentItem getContentItem(String id);
    ContentItem getContentItem(String id, ContentItem contentItem);

    List<ContentItem> searchContentItem(String searchText);
    List<ContentItem> searchContentItem(String searchText, ContentItem contentItem);
}
