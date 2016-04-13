package cn.campusapp.pan.lifecycle;

public interface OnHiddenChanged extends LifecycleObserver, LifecycleObserver.ForFragment {

    void onHiddenChanged(boolean hidden);
}
