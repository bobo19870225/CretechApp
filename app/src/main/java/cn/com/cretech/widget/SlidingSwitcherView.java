package cn.com.cretech.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.com.cretech.R;

public class SlidingSwitcherView extends RelativeLayout
{
    //第一个子view
    private LinearLayout mSwitcherLayout;
    //第二个子view
    private LinearLayout mDotsLayout;

    //第一个子view的宽度
    private int mSwitcerViewWidth;
    //item的数量
    private int mItemCount;

    //最右边，单位px
    private int mRightEdge;
    //最左边, 单位px  0
    private int mLeftEdge;

    private int[] mBorders;

    //当前显示item的索引
    private int mCurrentItemIndex = 0;

    //第一个item的View，scroll的过程中动态改变该view的leftMargin
    private View mFirstItemView;
    private MarginLayoutParams mFirstItemLp;

    /**
     * 事件处理用的几个变量
     */
    private int mDownX;
    private int mLastX;
    private int mLastDeltaX;
    private int mDistance;
    private int mTouchSlop;

    private VelocityTracker mTracker;

    //单位是dp
    private static final int MIN_SCROLL_VELOCITY = 200;
    private int mMinScrollVelocity;

    //标志位，在scrolling的过程中，屏蔽用户touch
    private boolean isScrolling;
    private AutoScrollRunnable mAutoScrollRunnable;

    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onClick(View view, int index);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    public SlidingSwitcherView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        //200dp转换成像素
        float density = context.getResources().getDisplayMetrics().density;
        mMinScrollVelocity = (int) (density * MIN_SCROLL_VELOCITY);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mAutoScrollRunnable = new AutoScrollRunnable();

        startAutoScroll();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);

        if (changed)
        {
            initSwitcherLayout();
            initDotsLayout();
        }
    }

    private void initDotsLayout()
    {
        mDotsLayout = (LinearLayout) getChildAt(1);
        refreshDotsLayout();
    }

    private void initSwitcherLayout()
    {
        mSwitcherLayout = (LinearLayout) getChildAt(0);

        mSwitcerViewWidth = mSwitcherLayout.getWidth();
        mItemCount = mSwitcherLayout.getChildCount();

        mBorders = new int[mItemCount];

        for (int i = 0; i < mItemCount; i++)
        {
            final int index = i;
            mBorders[i] = -i * mSwitcerViewWidth;
            View item = mSwitcherLayout.getChildAt(i);
            item.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mListener != null)
                    {
                        mListener.onClick(v, index);
                    }
                }
            });

            //确定子view的宽度为父view的宽度
            MarginLayoutParams lp = (MarginLayoutParams) item.getLayoutParams();
            lp.width = mSwitcerViewWidth;
            item.setLayoutParams(lp);
        }

        mFirstItemView = mSwitcherLayout.getChildAt(0);
        mRightEdge = 0;
        mLeftEdge = mBorders[mItemCount - 1];
        mFirstItemLp = (MarginLayoutParams) mFirstItemView.getLayoutParams();
    }

    /**
     * 刷新，标签布局，根据mCurrentItemIndex决定高亮哪一个
     */
    private void refreshDotsLayout()
    {
        mDotsLayout.removeAllViews();

        for (int i = 0; i < mItemCount; i++)
        {
            ImageView imageView = new ImageView(getContext());
            if (i == mCurrentItemIndex)
            {
                //imageView.setImageResource(R.mipmap.dot_selected);
            } else
            {
                //imageView.setImageResource(R.mipmap.dot_unselected);
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = 1;

            mDotsLayout.addView(imageView, lp);
        }
    }

    /**
     * 用户如果滑动，则拦截事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        int x = (int) ev.getX();
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                stopAutoScroll();
                mDownX = x;
                mLastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                mDistance = x - mDownX;
                mLastDeltaX = x - mLastX;
                mLastX = x;
                //有Move就拦截
                if (Math.abs(mDistance) > mTouchSlop)
                    return true;
                break;
            case MotionEvent.ACTION_UP:
                startAutoScroll();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (isScrolling)
        {
            return false;
        }

        createVelocityTracker(event);

        int x = (int) event.getX();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                stopAutoScroll();
                mDownX = x;
                mLastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                mDistance = x - mDownX;
                mLastDeltaX = x - mLastX;

                int leftMargin = -mSwitcerViewWidth * mCurrentItemIndex + mDistance;
                mFirstItemLp.leftMargin = leftMargin;

                //边界检查
                if (mFirstItemLp.leftMargin > mRightEdge)
                {
                    mFirstItemLp.leftMargin = mRightEdge;
                }
                if (mFirstItemLp.leftMargin < mLeftEdge)
                {
                    mFirstItemLp.leftMargin = mLeftEdge;
                }

                mFirstItemView.setLayoutParams(mFirstItemLp);
                mLastX = x;
                break;
            case MotionEvent.ACTION_UP:

                if (mFirstItemLp.leftMargin > mRightEdge)
                {
                    mFirstItemLp.leftMargin = mRightEdge;
                    mFirstItemView.setLayoutParams(mFirstItemLp);
                }
                if (mFirstItemLp.leftMargin < mLeftEdge)
                {
                    mFirstItemLp.leftMargin = mLeftEdge;
                    mFirstItemView.setLayoutParams(mFirstItemLp);
                }

                if (canScroll())
                {
                    if (wantToPrevious() && shouldScrollPrevious())
                    {
                        mCurrentItemIndex--;
                        scrollToPrevious(20);
                    } else if (wantToNext() && shouldScrollNext())
                    {
                        mCurrentItemIndex++;
                        scrollToNext(20);
                    } else
                    {
                        if (mDistance > 0)
                        {
                            scrollToNext(20);
                        } else
                        {
                            scrollToPrevious(20);
                        }
                    }
                }
                recycleVelocityTracker();
                startAutoScroll();
                break;
        }
        return true;
    }

    public static final String DEFAULT_KEY = "default_key";
    public static final String RESTORE_KEY = "restore_key";

    /**
     * 边界检查，第一张图片不能向右移，最后一张图片不能向左移
     *
     * @return
     */
    private boolean canScroll()
    {
        return mFirstItemLp.leftMargin < mRightEdge && mFirstItemLp.leftMargin > mLeftEdge;
    }

    /**
     * 用户手指移动距离为负，表示下一页
     *
     * @return
     */
    private boolean wantToNext()
    {
        return mDistance < 0;
    }

    /**
     * 用户手指移动距离为正，表示上一页
     *
     * @return
     */
    private boolean wantToPrevious()
    {
        return mDistance >= 0;
    }

    /**
     * 根据移动距离，velocity以及加速度方向确定是否可以scroll
     *
     * @return
     */
    private boolean shouldScrollNext()
    {
        return Math.abs(mDistance) > mSwitcerViewWidth / 2 ||
                Math.abs(getScrollXVelocity()) > mMinScrollVelocity && getScrollXVelocity() < 0;
    }

    private boolean shouldScrollPrevious()
    {
        return Math.abs(mDistance) > mSwitcerViewWidth / 2 ||
                Math.abs(getScrollXVelocity()) > mMinScrollVelocity && getScrollXVelocity() > 0;
    }

    private void createVelocityTracker(MotionEvent event)
    {
        if (mTracker == null)
        {
            mTracker = VelocityTracker.obtain();
        }
        mTracker.addMovement(event);
    }

    private void recycleVelocityTracker()
    {
        if (mTracker != null)
        {
            mTracker.recycle();
            mTracker = null;
        }
    }

    private int getScrollXVelocity()
    {
        mTracker.computeCurrentVelocity(1000);
        int velocity = (int) mTracker.getXVelocity();
        return velocity;
    }

    /**
     * @param speed 必须是负数
     */
    private void scrollToNext(int speed)
    {
        new ScrollTask().execute(-Math.abs(speed));
    }

    /**
     * @param speed 必须是正数
     */
    private void scrollToPrevious(int speed)
    {
        new ScrollTask().execute(Math.abs(speed));
    }

    /**
     * @param speed 必须是正数
     */
    private void scrollToFirst(int speed)
    {
        new ScrollToFirstTask().execute(Math.abs(speed * mCurrentItemIndex));
    }

    /**
     * 发送一个消息，实现自动scroll功能
     */
    public void startAutoScroll()
    {
        postDelayed(mAutoScrollRunnable, 3000);
    }

    /**
     * 移除自动scroll的Runnable对象
     */
    public void stopAutoScroll()
    {
        removeCallbacks(mAutoScrollRunnable);
    }

    /**
     * 异步任务，MotionEvent.UP的时候调用，实现scroll
     */
    public class ScrollTask extends AsyncTask<Integer, Integer, Integer>
    {
        @SuppressLint("WrongThread")
        @Override
        protected Integer doInBackground(Integer... params)
        {
            //在scrolling的时候，屏蔽用户touch
            isScrolling = true;

            int speed = params[0];
            int leftMargin = mFirstItemLp.leftMargin;

            while (true)
            {
                if (iscloseToBorder(leftMargin, speed))
                {
                    leftMargin = mBorders[mCurrentItemIndex];
                    break;
                }

                leftMargin += speed;

                sleep(10);
                publishProgress(leftMargin);
            }

            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);

            mFirstItemLp.leftMargin = values[0];
            mFirstItemView.setLayoutParams(mFirstItemLp);
        }

        @Override
        protected void onPostExecute(Integer leftMargin)
        {
            super.onPostExecute(leftMargin);

            //更新leftMargin，刷新DotsLayout
            mFirstItemLp.leftMargin = leftMargin;
            mFirstItemView.setLayoutParams(mFirstItemLp);
            refreshDotsLayout();
            isScrolling = false;
        }
    }

    /**
     * 异步任务：scroll到第一个itemview
     */
    public class ScrollToFirstTask extends AsyncTask<Integer, Integer, Integer>
    {
        @SuppressLint("WrongThread")
        @Override
        protected Integer doInBackground(Integer... params)
        {
            int speed = params[0];
            int leftMargin = mFirstItemLp.leftMargin;

            while (true)
            {
                if (leftMargin <= 0 && leftMargin + speed >= 0)
                {
                    leftMargin = 0;
                    break;
                }

                leftMargin += speed;
                sleep(10);

                publishProgress(leftMargin);
            }

            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);

            int leftMargin = values[0];
            mFirstItemLp.leftMargin = leftMargin;
            mFirstItemView.setLayoutParams(mFirstItemLp);
        }

        @Override
        protected void onPostExecute(Integer leftMargin)
        {
            super.onPostExecute(leftMargin);

            mFirstItemLp.leftMargin = leftMargin;
            mFirstItemView.setLayoutParams(mFirstItemLp);

            mCurrentItemIndex = 0;
            refreshDotsLayout();
        }
    }

    private void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * speed的正负用来判断是右滑，还是左滑
     *
     * @param leftMargin
     * @param speed
     * @return
     */
    private boolean iscloseToBorder(int leftMargin, int speed)
    {
        int border = mBorders[mCurrentItemIndex];
        if (speed > 0)
        {
            if (leftMargin <= border && leftMargin + speed >= border)
                return true;
        } else
        {
            if (leftMargin >= border && leftMargin + speed <= border)
                return true;
        }
        return false;
    }

    /**
     * Runnable对象，由于实现自动scroll
     */
    public class AutoScrollRunnable implements Runnable
    {
        @Override
        public void run()
        {
            if (mCurrentItemIndex == mItemCount - 1)
            {
                scrollToFirst(20);
                mCurrentItemIndex = 0;
            } else
            {
                mCurrentItemIndex++;
                scrollToNext(20);
            }
            postDelayed(this, 3000);
        }
    }

}
