package cn.campusapp.pan.lifecycle;

import android.os.Bundle;

/**
 *
 * Created by nius on 10/13/15.
 */
public interface OnPostCreate extends LifecycleObserver, LifecycleObserver.ActivityOnly{

    void onPostCreate(Bundle savedInstanceState);
}
