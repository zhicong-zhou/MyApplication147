package com.jzkl.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView{
    private OnScrollChangeListener scrollChangeListener = null;
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollChangeListener != null) {
            scrollChangeListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
    public void setScrollViewListener(OnScrollChangeListener onScrollChangeListener) {
        this.scrollChangeListener = onScrollChangeListener;
    }

    public interface OnScrollChangeListener {
        void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy);
    }
}
