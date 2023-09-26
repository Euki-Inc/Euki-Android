package com.kollectivemobile.euki.manager;

import android.util.Log;

import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContentManagerImpl implements ContentManager {
    private static final String ABORTION_KNOWLEDGE_JSON = "abortion_knowledge";
    private static final String ABORTION_WALKTHROUGH_JSON = "abortion_walkthrough";
    private static final String STIS_JSON = "stis";
    private static final String CONTRACEPTION_JSON = "contraception";
    private static final String MISCARRIAGE_JSON = "miscarriage";
    private static final String SEX_RELATIONSHIPS_JSON = "sex_relationships";
    private static final String PREGNANCY_OPTIONS = "pregnancy_options";

    private ContentItem mAbortionKnowledge;
    private ContentItem mAbortionWalkthrough;
    private ContentItem mSTI;
    private ContentItem mContraception;
    private ContentItem mMiscarriage;
    private ContentItem mSexRelationships;
    private ContentItem mPregnancyOptions;

    private AbortionContentManager mAbortionContentManager;
    private PrivacyContentManager mPrivacyContentManager;

    public ContentManagerImpl(AbortionContentManager abortionContentManager, PrivacyContentManager privacyContentManager) {
        this.mAbortionContentManager = abortionContentManager;
        this.mPrivacyContentManager = privacyContentManager;
    }

    @Override
    public void getAbortionKnowledge(EukiCallback<ContentItem> callback) {
        callback.onSuccess(getAbortionKnowledge());
    }

    @Override
    public void getAbortionWalkthrough(EukiCallback<ContentItem> callback) {
        callback.onSuccess(getAbortionWalkthrough());
    }

    @Override
    public void getSTIs(EukiCallback<ContentItem> callback) {
        callback.onSuccess(getSTI());
    }

    @Override
    public void getContraception(EukiCallback<ContentItem> callback) {
        callback.onSuccess(getContraception());
    }

    @Override
    public void getMiscarriage(EukiCallback<ContentItem> callback) {
        callback.onSuccess(getMiscarriage());
    }

    @Override
    public void getSexRelationships(EukiCallback<ContentItem> callback) {
        callback.onSuccess(getSexRelationships());
    }

    @Override
    public void getPregnancyOptions(EukiCallback<ContentItem> callback) {
        callback.onSuccess(getPregnancyOptions());
    }

    @Override
    public ContentItem getContentItem(String id) {
        ContentItem foundContentItem = getContentItem(id, getAbortionKnowledge());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        foundContentItem = getContentItem(id, getAbortionWalkthrough());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        foundContentItem = getContentItem(id, getSTI());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        foundContentItem = getContentItem(id, getContraception());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        foundContentItem = getContentItem(id, getMiscarriage());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        foundContentItem = getContentItem(id, getSexRelationships());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        foundContentItem = getContentItem(id, getPregnancyOptions());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        // Abortion ContentItems

        foundContentItem = getContentItem(id, mAbortionContentManager.getAbortionMifeMiso12());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        foundContentItem = getContentItem(id, mAbortionContentManager.getAbortionMiso12());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        foundContentItem = getContentItem(id, mAbortionContentManager.getAbortionSuctionVacuum());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        foundContentItem = getContentItem(id, mAbortionContentManager.getAbortionDilationEvacuation());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        foundContentItem = getContentItem(id, mAbortionContentManager.getMedialAbortion());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        // Privacy ContentItems

        foundContentItem = getContentItem(id, mPrivacyContentManager.getPrivacyFAQs());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        foundContentItem = getContentItem(id, mPrivacyContentManager.getPrivacyStatement());
        if (foundContentItem != null) {
            return foundContentItem;
        }

        return null;
    }

    @Override
    public ContentItem getContentItem(String id, ContentItem contentItem) {
        if (contentItem == null || contentItem.getId() == null) {
            return null;
        }

        if (contentItem.getId().equals(id)) {
            return contentItem;
        }

        if (contentItem.getContentItems() != null) {
            for (ContentItem childItem : contentItem.getContentItems()) {
                ContentItem foundItem = getContentItem(id, childItem);
                if (foundItem != null) {
                    return foundItem;
                }
            }
        }

        if (contentItem.getExpandableItems() != null) {
            for (ContentItem childItem : contentItem.getExpandableItems()) {
                ContentItem foundItem = getContentItem(id, childItem);
                if (foundItem != null) {
                    foundItem.setParent(contentItem);
                    return foundItem;
                }
            }
        }

        if (contentItem.getSelectableItems() != null) {
            for (ContentItem childItem : contentItem.getSelectableItems()) {
                ContentItem foundItem = getContentItem(id, childItem);
                if (foundItem != null) {
                    return foundItem;
                }
            }
        }

        if (contentItem.getSelectableRowItems() != null) {
            for (ContentItem childItem : contentItem.getSelectableRowItems()) {
                ContentItem foundItem = getContentItem(id, childItem);
                if (foundItem != null) {
                    return foundItem;
                }
            }
        }

        return null;
    }

    @Override
    public List<ContentItem> searchContentItem(String searchText) {
        List<ContentItem> items = new ArrayList<>();

        items.addAll(searchContentItem(searchText, getAbortionKnowledge()));
        items.addAll(searchContentItem(searchText, getAbortionWalkthrough()));
        items.addAll(searchContentItem(searchText, getSTI()));
        items.addAll(searchContentItem(searchText, getContraception()));
        items.addAll(searchContentItem(searchText, getMiscarriage()));
        items.addAll(searchContentItem(searchText, getSexRelationships()));
        items.addAll(searchContentItem(searchText, getPregnancyOptions()));

        //Abortion ContentItems

        items.addAll(searchContentItem(searchText, mAbortionContentManager.getAbortionMifeMiso12()));
        items.addAll(searchContentItem(searchText, mAbortionContentManager.getAbortionMiso12()));
        items.addAll(searchContentItem(searchText, mAbortionContentManager.getAbortionSuctionVacuum()));
        items.addAll(searchContentItem(searchText, mAbortionContentManager.getAbortionDilationEvacuation()));
        items.addAll(searchContentItem(searchText, mAbortionContentManager.getMedialAbortion()));

        List<ContentItem> foundItems = new ArrayList<>();

        for (ContentItem item : items) {
            if (!foundItems.contains(item)) {
                foundItems.add(item);
            }
        }

        return foundItems;
    }

    @Override
    public List<ContentItem> searchContentItem(String searchText, ContentItem contentItem) {
        List<ContentItem> items = new ArrayList<>();

        if (contentItem.getId() == null) {
            return items;
        }

        String id = Utils.getLocalized(contentItem.getId());
        if (id != null && id.toLowerCase().contains(searchText.toLowerCase())) {
            items.add(contentItem);
        }

        String title = Utils.getLocalized(contentItem.getTitle());
        if (title != null && title.toLowerCase().contains(searchText.toLowerCase())) {
            items.add(contentItem);
        }

        String content = Utils.getLocalized(contentItem.getContent());
        if (content != null && content.toLowerCase().contains(searchText.toLowerCase())) {
            items.add(contentItem);
        }

        if (contentItem.getContentItems() != null) {
            for (ContentItem childItem : contentItem.getContentItems()) {
                if (searchContentItem(searchText, childItem).size() > 0) {
                    items.add(contentItem);
                }
            }
        }

        if (contentItem.getExpandableItems() != null) {
            for (ContentItem childItem : contentItem.getExpandableItems()) {
                if (searchContentItem(searchText, childItem).size() > 0) {
                    items.add(contentItem);
                }
            }
        }

        if (contentItem.getSelectableItems() != null) {
            for (ContentItem childItem : contentItem.getSelectableItems()) {
                List<ContentItem> foundItems = searchContentItem(searchText, childItem);
                items.addAll(foundItems);
            }
        }

        if (contentItem.getSelectableRowItems() != null) {
            for (ContentItem childItem : contentItem.getSelectableRowItems()) {
                List<ContentItem> foundItems = searchContentItem(searchText, childItem);
                items.addAll(foundItems);
            }
        }

        List<ContentItem> foundItems = new ArrayList<>();

        for (ContentItem item : items) {
            if (!foundItems.contains(item)) {
                foundItems.add(item);
            }
        }

        return foundItems;
    }

    private ContentItem createContentItem(String filename) {
        JSONObject jsonObject = Utils.getJsonFromAssets(filename);
        return new ContentItem(jsonObject);
    }

    /*
        Gettters
     */

    public ContentItem getAbortionKnowledge() {
        if (mAbortionKnowledge == null) {
            mAbortionKnowledge = createContentItem(ABORTION_KNOWLEDGE_JSON);
        }
        return mAbortionKnowledge;
    }

    public ContentItem getAbortionWalkthrough() {
        if (mAbortionWalkthrough == null) {
            mAbortionWalkthrough = createContentItem(ABORTION_WALKTHROUGH_JSON);
        }
        return mAbortionWalkthrough;
    }

    public ContentItem getSTI() {
        if (mSTI == null) {
            mSTI = createContentItem(STIS_JSON);
        }
        return mSTI;
    }

    public ContentItem getContraception() {
        if (mContraception == null) {
            mContraception = createContentItem(CONTRACEPTION_JSON);
        }
        return mContraception;
    }

    public ContentItem getMiscarriage() {
        if (mMiscarriage == null) {
            mMiscarriage = createContentItem(MISCARRIAGE_JSON);
        }
        return mMiscarriage;
    }

    public ContentItem getSexRelationships() {
        if (mSexRelationships == null) {
            mSexRelationships = createContentItem(SEX_RELATIONSHIPS_JSON);
        }
        return mSexRelationships;
    }

    public ContentItem getPregnancyOptions() {
        if (mPregnancyOptions == null) {
            mPregnancyOptions = createContentItem(PREGNANCY_OPTIONS);
        }
        return mPregnancyOptions;
    }
}
