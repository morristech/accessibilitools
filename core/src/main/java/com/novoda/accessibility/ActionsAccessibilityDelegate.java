package com.novoda.accessibility;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.View;

import static android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_CLICK;
import static android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_LONG_CLICK;

public class ActionsAccessibilityDelegate extends AccessibilityDelegateCompat {

    private final Resources resources;
    private final Actions actions;

    private CharSequence clickLabel = null;
    private CharSequence longClickLabel = null;

    public ActionsAccessibilityDelegate(Resources resources, Actions actions) {
        this.resources = resources;
        this.actions = actions;
    }

    /**
     * Label describing the action that will be performed on click
     *
     * @param clickLabel
     */
    public void setClickLabel(@StringRes int clickLabel) {
        setClickLabel(resources.getString(clickLabel));
    }

    /**
     * Label describing the action that will be performed on click
     *
     * @param clickLabel
     */
    public void setClickLabel(CharSequence clickLabel) {
        this.clickLabel = clickLabel;
    }

    /**
     * Label describing the action that will be performed on long click
     *
     * @param longClickLabel
     */
    public void setLongClickLabel(@StringRes int longClickLabel) {
        setLongClickLabel(resources.getString(longClickLabel));
    }

    /**
     * Label describing the action that will be performed on long click
     *
     * @param longClickLabel
     */
    public void setLongClickLabel(CharSequence longClickLabel) {
        this.longClickLabel = longClickLabel;
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
        super.onInitializeAccessibilityNodeInfo(host, info);
        for (Action action : actions) {
            String label = resources.getString(action.getLabel());
            info.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(action.getId(), label));
        }

        addCustomDescriptionForClickEventIfNecessary(host, info);
        addCustomDescriptionForLongClickEventIfNecessary(host, info);
    }

    private void addCustomDescriptionForClickEventIfNecessary(View host, AccessibilityNodeInfoCompat info) {
        if (!host.isClickable() || clickLabel == null) {
            return;
        }

        info.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(ACTION_CLICK, clickLabel));
    }

    private void addCustomDescriptionForLongClickEventIfNecessary(View host, AccessibilityNodeInfoCompat info) {
        if (!host.isLongClickable() || longClickLabel == null) {
            return;
        }

        info.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(ACTION_LONG_CLICK, longClickLabel));
    }

    @Override
    public boolean performAccessibilityAction(View host, int actionId, Bundle args) {
        Action action = actions.findActionById(actionId);
        if (action == null) {
            return super.performAccessibilityAction(host, actionId, args);
        } else {
            action.run();
            return true;
        }
    }

}
