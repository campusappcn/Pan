package cn.campusapp.pan.lifecycle;

import android.content.Intent;

public interface OnNewIntent extends LifecycleObserver, LifecycleObserver.ForActivity {

    void onNewIntent(Intent intent);
}
