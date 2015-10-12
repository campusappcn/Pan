package cn.campusapp.pan.lifecycle;

import android.content.Intent;

/**
 * Created by nius on 10/12/15.
 */
public interface OnNewIntent extends LifecycleObserver, LifecycleObserver.ActivityOnly{

    void onNewIntent(Intent intent);
}
