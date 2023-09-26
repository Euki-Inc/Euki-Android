package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PrivacyContentManagerImpl implements PrivacyContentManager {
    private AppSettingsManager mAppSettingsManager;

    public PrivacyContentManagerImpl(AppSettingsManager appSettingsManager) {
        this.mAppSettingsManager = appSettingsManager;
    }

    @Override
    public ContentItem getPrivacyFAQs() {
        String pinCode = mAppSettingsManager.getPinCode();
        String fakePin = (pinCode == null || pinCode.equals("2222")) ? "1111" : "2222";

        ContentItem contentItem = new ContentItem("privacy_faq");
        contentItem.setId("privacy_faq");

        List<ContentItem> childItems = new ArrayList<>();
        for (int i=1; i<=4; i++) {
            String text = "privacy_faqs_title_" + i;
            String content = Utils.getLocalized("privacy_faqs_content_" + i).replace("[FAKE_PIN]", fakePin);

            ContentItem childItem = new ContentItem(text);
            childItem.setContent(content);
            childItem.setItemId(i);
            childItems.add(childItem);
        }
        contentItem.setExpandableItems(childItems);

        return contentItem;
    }

    @Override
    public ContentItem getPrivacyStatement() {
        ContentItem item = new ContentItem("privacy_statement");
        item.setContent("privacy_statement_content");
        return item;
    }
}
