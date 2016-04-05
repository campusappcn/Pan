package cn.campusapp.pan.lifecycle;

/**
 * Created by nius on 10/12/15.
 */
public interface OnRestart extends LifecycleObserver, LifecycleObserver.ForActivity {

    void onRestart();
}
