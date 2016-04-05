package cn.campusapp.pan;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import cn.campusapp.pan.lifecycle.LifecycleObserved;

/**
 * For RecyclerView
 * Created by nius on 10/29/15.
 */
public abstract class RecyclerViewModel extends RecyclerView.ViewHolder implements FactoryViewModel {

    transient public View mRootView = null;
    transient protected Activity mActivity;
    transient protected PanFragment mFragment;
    protected GeneralController mController;

    public RecyclerViewModel(View rootView) {
        super(rootView);
        mRootView = rootView;
    }

    @Override
    public GeneralController getController() {
        return mController;
    }

    @Override
    public void setController(GeneralController c) {
        mController = c;
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    public PanFragment getFragment() {
        return mFragment;
    }

    /**
     * return the observing Activity/Fragment
     * handy for nesting use of ViewModel
     *
     * @return Activity/Fragment currently observing
     */
    public LifecycleObserved getObserving() {
        if (mFragment != null) {
            return mFragment;
        }
        if (mActivity != null) {
            return (LifecycleObserved) mActivity;
        }
        throw new RuntimeException("wtf");
    }

    @Override
    public void setFragment(PanFragment fragment) {
        mFragment = fragment;
    }

    /**
     * 在注入了views之后会被调用，允许子类进行一些初始化操作
     */
    protected void onInit() {

    }

    /**
     * 允许外部重新初始化控件状态
     */
    public void reInit() {
        onInit();
    }

    /**
     * 用于在整体绑定事件
     *
     * @return the root view of the view model binds to
     */
    public View getRootView() {
        return mRootView;
    }

    public void setRootView(View rootView) {
        mRootView = rootView;
    }

    public void bindViews() {
        ButterKnife.bind(this, mRootView);
        onInit();
    }

}
