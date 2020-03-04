package cn.com.cretech.pullToRefresh;

import android.view.View;


/**
 * Created by laidayuan on 16/1/26.
 */
public interface IPullToRefresh<T extends View> {


    public void setPullRefreshEnabled(boolean pullRefreshEnabled);


    public void setPullLoadEnabled(boolean pullLoadEnabled);


    public void setScrollLoadEnabled(boolean scrollLoadEnabled);


    public boolean isPullRefreshEnabled();


    public boolean isPullLoadEnabled();


    public boolean isScrollLoadEnabled();


    public void setOnRefreshListener(PullToRefreshBase.OnRefreshListener<T> refreshListener);


    public void onPullDownRefreshComplete();


    public void onPullUpRefreshComplete();


    public T getRefreshableView();


    public LoadingLayout getHeaderLoadingLayout();


    public LoadingLayout getFooterLoadingLayout();


    public void setLastUpdatedLabel(CharSequence label);
}
