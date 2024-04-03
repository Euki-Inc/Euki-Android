package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.model.ContentItem;

import java.util.Arrays;
import java.util.List;

public class MenstruationContentManagerImpl implements  MenstruationContentManager{
    private static final List<String> sContentIds = Arrays.asList("reusable_pad", "disposable_pad", "tampon", "menstrual_cup", "menstrual_disc", "period_underwear", "liner");

    private final ContentManager mContentManager;

    public MenstruationContentManagerImpl(ContentManager contentManager) {
        this.mContentManager = contentManager;
    }

    @Override
    public ContentItem getMethodContentItem(Integer index) {
        String contentId = sContentIds.get(index);
        return mContentManager.getContentItem(contentId);
    }
}
