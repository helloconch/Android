package com.android.tv;

/**
 * Created by Administrator on 2016/9/13.
 */
public class FocusItem {
    /**
     * 焦点状态
     */
    private boolean focus;
    /**
     * 方向键经过的次数
     */
    private int keyCount;


    public FocusItem(boolean focus) {
        this.focus = focus;
        if (!this.focus)
            this.keyCount = 0;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public int getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(int keyCount) {
        this.keyCount = keyCount;
    }
}
