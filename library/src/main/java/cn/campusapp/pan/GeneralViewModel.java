package cn.campusapp.pan;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;
import cn.campusapp.pan.lifecycle.LifecycleObserved;


/**
 * 兼具ViewModel和ViewHolder功能
 * <p>
 * 需要一个无参构造方法
 * <p>
 * Created by nius on 7/17/15.
 */
public abstract class GeneralViewModel implements FactoryViewModel {

    transient public View mRootView = null;
    transient protected Activity mActivity;
    transient protected Fragment mFragment;
    protected GeneralController mController;

    public GeneralViewModel() {

    }

    @Override
    public GeneralController getController() {
        return mController;
    }

    @Override
    public final void setController(GeneralController c) {
        mController = c;
    }

    @Override
    public final Activity getActivity() {
        return mActivity;
    }

    @Override
    public final void setActivity(Activity activity) {
        mActivity = activity;
    }

    @Override
    public final Fragment getFragment() {
        return mFragment;
    }

    @Override
    public final void setFragment(Fragment fragment) {
        mFragment = fragment;
    }

    /**
     * return the observing Activity/Fragment
     * handy for nesting use of ViewModel
     *
     * @return Activity/Fragment currently observing
     */
    public final LifecycleObserved getObserving() {
        if (mFragment != null) {
            return (LifecycleObserved) mFragment;
        }
        if (mActivity != null) {
            return (LifecycleObserved) mActivity;
        }
        throw new RuntimeException("wtf");
    }

    /**
     * 用于在整体绑定事件
     *
     * @return the root view of the view model binds to
     */
    public final View getRootView() {
        return mRootView;
    }

    public final void setRootView(View rootView) {
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
