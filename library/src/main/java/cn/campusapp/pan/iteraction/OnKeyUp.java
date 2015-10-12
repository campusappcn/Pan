package cn.campusapp.pan.iteraction;

import android.view.KeyEvent;

import cn.campusapp.pan.lifecycle.ActivityLifecycleObserver;


/**
 * 键盘按下
 * Created by nius on 7/31/15.
 */
public interface OnKeyUp extends ActivityLifecycleObserver {

    void onKeyUp(int keyCode, KeyEvent event);
}
