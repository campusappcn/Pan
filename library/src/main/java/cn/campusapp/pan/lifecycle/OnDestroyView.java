package cn.campusapp.pan.lifecycle;

/**
 * Created by nius on 7/22/15.
 */
public interface OnDestroyView extends LifecycleObserver, LifecycleObserver.FragmentOnly{

    void onDestroyView();
}
