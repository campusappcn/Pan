package cn.campusapp.pan.eventbus;

import cn.campusapp.pan.lifecycle.OnStartActivity;
import cn.campusapp.pan.lifecycle.OnStopActivity;

/**
 * Created by nius on 7/29/15.
 */
public interface EventBusActivityController extends EventBusController, OnStartActivity, OnStopActivity {
}
