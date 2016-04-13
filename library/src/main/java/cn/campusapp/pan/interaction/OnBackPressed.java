package cn.campusapp.pan.interaction;


import cn.campusapp.pan.lifecycle.LifecycleObserver;

/**
 * Back btn pressed
 * <p/>
 * Created by nius on 7/31/15.
 */
public interface OnBackPressed extends LifecycleObserver {

    boolean onBackPressed();
}
