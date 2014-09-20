package com.sleepbot.datetimepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.TextView;

/**
 * Fake Button class, used so TextViews can announce themselves as Buttons, for accessibility.
 */
@SuppressLint("Override")
public class AccessibleTextView extends TextView {

    @SuppressLint("Override")
	public AccessibleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("Override")
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        if (Build.VERSION.SDK_INT >= 14) {
            //super.onInitializeAccessibilityEvent(event);
            event.setClassName(Button.class.getName());
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        if (Build.VERSION.SDK_INT >= 14) {
            //super.onInitializeAccessibilityNodeInfo(info);
            //info.setClassName(Button.class.getName());
        }
    }
}

