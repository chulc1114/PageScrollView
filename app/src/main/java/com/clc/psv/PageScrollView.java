package com.clc.psv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.Scroller;

public class PageScrollView extends HorizontalScrollView {
    public static final int PAGE_WIDTH = 1000;
    public static final int DISTANCE_LIMIT = 500;
    private Scroller mScroller;
    private PageChangedListener mPageChangedListener;

    public void setPageChangedListener(PageChangedListener pageChangedListener) {
        mPageChangedListener = pageChangedListener;
    }

    public PageScrollView(Context context) {
        super(context);
        mScroller = new Scroller(context);
    }

    public PageScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    @Override
    public void fling(int velocityX) {
        super.fling(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int scrollX = getScrollX();
            int finalX = getFinalX();
            mScroller.startScroll(scrollX, 0, finalX - scrollX, 0);
            invalidate();
            if (mPageChangedListener != null) {
                mPageChangedListener.onPageChanged(finalX / PAGE_WIDTH + 1);
            }
        }
        return super.onTouchEvent(event);
    }

    private int getFinalX() {
        int num = getScrollX() / DISTANCE_LIMIT;
        if ((num & 1) == 1) {
            return DISTANCE_LIMIT * (num + 1);
        } else {
            return DISTANCE_LIMIT * num;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }

    public interface PageChangedListener {
        void onPageChanged(int pageNum);
    }
}
