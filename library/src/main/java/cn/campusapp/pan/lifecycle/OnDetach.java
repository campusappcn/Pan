package cn.campusapp.pan.lifecycle;

public interface OnDetach extends LifecycleObserver, LifecycleObserver.ForFragment {

    void onDetach();
}
