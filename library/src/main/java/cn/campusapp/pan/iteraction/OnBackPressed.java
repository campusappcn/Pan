package cn.campusapp.pan.iteraction;


import cn.campusapp.pan.lifecycle.ActivityLifecycleObserver;

/**
 *
 * Created by nius on 7/31/15.
 */
public interface OnBackPressed extends ActivityLifecycleObserver {

    void onBackPressed();
}
