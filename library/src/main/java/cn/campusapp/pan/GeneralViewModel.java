package cn.campusapp.pan;

import android.app.Activity;
import android.view.View;

import butterknife.ButterKnife;
import cn.campusapp.pan.lifecycle.LifecycleObserved;


/**
 * 兼具ViewModel和ViewHolder功能
 * <p/>
 * 需要一个无参构造方法
 * <p/>
 * Created by nius on 7/17/15.
 */
public abstract class GeneralViewModel implements FactoryViewModel {

    transient public View mRootView = null;
    transient protected Activity mActivity;
    transient protected PanFragment mFragment;
    protected GeneralController mController;

    public GeneralViewModel() {

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
    public void setActivity(Activity activity) {
        mActivity = activity;
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
}
