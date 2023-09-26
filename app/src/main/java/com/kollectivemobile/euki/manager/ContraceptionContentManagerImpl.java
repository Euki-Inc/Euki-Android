package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.model.ContentItem;

import java.util.Arrays;
import java.util.List;

public class ContraceptionContentManagerImpl implements ContraceptionContentManager{
    private static final List<String> sContentIds = Arrays.asList("iud", "copper_uid", "implant", "shot", "patch", "pill", "ring", "other_barriers", "condom", "pull_out", "tubes_tied", "rhythm");

    private ContentManager mContentManager;

    public ContraceptionContentManagerImpl(ContentManager contentManager) {
        this.mContentManager = contentManager;
    }

    @Override
    public ContentItem getMethodContentItem(Integer index) {
        String contentId = sContentIds.get(index);
        return mContentManager.getContentItem(contentId);
    }
}
