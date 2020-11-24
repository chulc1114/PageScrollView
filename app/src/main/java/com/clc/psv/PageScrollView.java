package com.clc.psv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.Scroller;

public class PageScrollView extends HorizontalScrollView {
    public static final int PAGE_WIDTH = 1000;
    public static final int DISTANCE_LIMIT = 300;
    private Scroller mScroller;
    private int mDownX;
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
        /**
         * 记录mDownX，以判断滑动方向
         * 如果滑动临界是页宽的一半，不用记录mDownX
         */
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mDownX = getScrollX();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int currX = getScrollX();
            int finalX = findFinalX(currX);
            mScroller.startScroll(currX, 0, finalX - currX, 0);
            invalidate();
            if (mPageChangedListener != null) {
                mPageChangedListener.onPageChanged(finalX / PAGE_WIDTH + 1);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取scroll终点（如果滑动临界是页宽的一半）
     */
    /*private int findFinalX(int currX) {
        int multiple = currX / DISTANCE_LIMIT;
        if ((multiple & 1) == 1) {
            return DISTANCE_LIMIT * (multiple + 1);
        } else {
            return DISTANCE_LIMIT * multiple;
        }
    }*/

    /**
     * 获取scroll终点（较为通用）
     */
    private int findFinalX(int currX) {
        int remainder = currX % PAGE_WIDTH;
        int multiple = currX / PAGE_WIDTH;
        if (remainder < DISTANCE_LIMIT) {
            return PAGE_WIDTH * multiple;
        } else if (remainder > PAGE_WIDTH - DISTANCE_LIMIT) {
            return PAGE_WIDTH * (multiple + 1);
        } else {
            if (currX > mDownX) {
                return PAGE_WIDTH * (multiple + 1);
            } else {
                return PAGE_WIDTH * (multiple);
            }
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
