package cn.campusapp.pan.lifecycle;

public interface OnRestart extends LifecycleObserver, LifecycleObserver.ForActivity {

    void onRestart();
}
