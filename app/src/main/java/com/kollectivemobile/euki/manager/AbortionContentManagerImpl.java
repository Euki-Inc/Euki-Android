package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbortionContentManagerImpl implements AbortionContentManager {
    private Map<String, String> links = new HashMap<>();

    public AbortionContentManagerImpl() {
        links.clear();
        links.put(Utils.getLocalized("world_health_organization"), "https://apps.who.int/iris/bitstream/handle/10665/278968/9789241550406-eng.pdf?ua=1");
    }

    @Override
    public ContentItem getAbortionMifeMiso12() {
        ContentItem item = new ContentItem();
        item.setAbottionItem(true);
        item.setId("mife_miso_12_weeks");

        List<ContentItem> childItems = new ArrayList<>();
        for (int i=1; i<=7; i++) {
            String text = "mife_miso_12_text_" + i;
            String image = "icon_mife_miso_12_" + i;
            ContentItem childItem = new ContentItem(text);

            if (i < 7) {
                childItem.setImageIcon(image);
            }

            childItem.setLinks(links);
            childItem.setItemId(i);
            childItems.add(childItem);
        }
        item.setContentItems(childItems);
        item.setExpandableItems(getAbortionPillsExpandableItems("mife_miso_12_subheading_%d", "abortion_subheading_%d_content", item));

        return item;
    }

    @Override
    public ContentItem getAbortionMiso12() {
        ContentItem item = new ContentItem();
        item.setAbottionItem(true);
        item.setId("misoprostol_12_weeks");

        List<ContentItem> childItems = new ArrayList<>();
        for (int i=1; i<=6; i++) {
            String text = "misoprostol_12_text_" + i;
            String image = "icon_miso_12_" + i;
            ContentItem childItem = new ContentItem(text);

            if (i < 6) {
                childItem.setImageIcon(image);
            }

            childItem.setLinks(links);
            childItem.setItemId(i);
            childItems.add(childItem);
        }
        item.setContentItems(childItems);
        item.setExpandableItems(getAbortionPillsExpandableItems("misoprostol_12_subheading_%d", "abortion_subheading_%d_content", item));

        return item;
    }

    @Override
    public ContentItem getAbortionSuctionVacuum() {
        ContentItem item = new ContentItem();
        item.setAbottionItem(true);
        item.setId("suction_or_vacuum");

        List<ContentItem> childItems = new ArrayList<>();
        for (int i=1; i<=5; i++) {
            String text = "suction_or_vacuum_text_" + (i-1);
            String image = "icon_surgical_" + i;
            ContentItem childItem;

            if (i == 1) {
                childItem = new ContentItem();
            } else {
                childItem = new ContentItem(text);
            }

            if (i < 5) {
                childItem.setImageIcon(image);
            }

            childItem.setLinks(links);
            childItem.setItemId(i);
            childItems.add(childItem);
        }
        item.setContentItems(childItems);
        item.setExpandableItems(getAbortionSuctionExpandableItems("suction_or_vacuum_subheading_%d", item));

        return item;
    }

    @Override
    public ContentItem getAbortionDilationEvacuation() {
        ContentItem item = new ContentItem();
        item.setAbottionItem(true);
        item.setId("dilation_evacuation");

        List<ContentItem> childItems = new ArrayList<>();
        for (int i=1; i<=5; i++) {
            String text = "dilation_evacuation_text_" + (i-1);
            String image = "icon_dilation_" + i;
            ContentItem childItem;

            if (i == 1) {
                childItem = new ContentItem();
            } else {
                childItem = new ContentItem(text);
            }

            if (i < 5) {
                childItem.setImageIcon(image);
            }

            childItem.setLinks(links);
            childItem.setItemId(i);
            childItems.add(childItem);
        }
        item.setContentItems(childItems);
        item.setExpandableItems(getAbortionSuctionExpandableItems("dilation_evacuation_subheading_%d", item));

        return item;
    }

    @Override
    public ContentItem getMedialAbortion() {
        ContentItem item = new ContentItem("medical_abortion");
        item.setContent("medical_abortion_content");
        Map<String, String> linksMap = new HashMap<>();
        linksMap.put(App.getContext().getString(R.string.world_health_organization), "https://apps.who.int/iris/bitstream/handle/10665/278968/9789241550406-eng.pdf?ua=1");
        linksMap.put(App.getContext().getString(R.string.abortion_on_demand), "https://abortionondemand.org/");
        linksMap.put("ⓘ", "telehealth_popup_info");
        item.setLinks(linksMap);
        return item;
    }

    private List<ContentItem> getAbortionPillsExpandableItems(String textFormat, String contentFormat, ContentItem parentItem) {
        List<ContentItem> items = new ArrayList<>();
        Map<String, String> links = new HashMap<>();
        links.put("Miscarriage and Abortion Hotline", "https://www.mahotline.org/");
        links.put("Repro Legal Hotline", "https://www.reprolegalhelpline.org/");

        for (int i=1; i<=5; i++) {
            String text = String.format(textFormat, i);
            String content = String.format(contentFormat, i);
            ContentItem item = new ContentItem(text);
            item.setExpandable(true);
            item.setContent(content);
            item.setParent(parentItem);
            item.setItemId(i);
            item.setLinks(links);
            items.add(item);
        }

        return items;
    }

    private List<ContentItem> getAbortionSuctionExpandableItems(String textFormat, ContentItem parentItem) {
        List<ContentItem> items = new ArrayList<>();

        for (int i=1; i<=2; i++) {
            String text = String.format(textFormat, i);
            String content = i == 1 ? "abortion_medical_aftercare" : "abortion_emotional_aftercare";
            ContentItem item = new ContentItem(text);
            item.setExpandable(true);
            item.setContent(content);
            item.setParent(parentItem);
            item.setItemId(i);
            items.add(item);
        }

        return items;
    }
}
