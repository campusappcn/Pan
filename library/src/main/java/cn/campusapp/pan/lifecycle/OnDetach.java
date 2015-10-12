package cn.campusapp.pan.lifecycle;

/**
 * Created by nius on 10/12/15.
 */
public interface OnDetach extends LifecycleObserver, LifecycleObserver.FragmentOnly{

    void onDetach();
}
