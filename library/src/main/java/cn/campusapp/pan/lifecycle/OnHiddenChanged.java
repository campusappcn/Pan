package cn.campusapp.pan.lifecycle;

/**
 * Created by nius on 10/12/15.
 */
public interface OnHiddenChanged extends LifecycleObserver, LifecycleObserver.FragmentOnly{

    void onHiddenChanged(boolean hidden);
}
