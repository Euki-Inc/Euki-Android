package com.kollectivemobile.euki.utils.strings;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;

import com.kollectivemobile.euki.utils.Utils;

/**
 * Created by pAk on 10/30/16.
 */

public class BulletIndentSpan implements LeadingMarginSpan {

    private final int gapWidth;
    private final int leadWidth;

    public BulletIndentSpan(int leadGap, int gapWidth) {
        this.leadWidth = leadGap;
        this.gapWidth = gapWidth;
    }

    public int getLeadingMargin(boolean first) {
        return leadWidth + gapWidth;
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout l) {
        if (first) {
            Paint.Style orgStyle = p.getStyle();
            p.setStyle(Paint.Style.FILL);
            float width = p.measureText("4.");
            c.drawText("●", (leadWidth + x - width / 2) * dir, bottom - p.descent() - Utils.dpFromInt(8), p);
            p.setStyle(orgStyle);
        }
    }
}
