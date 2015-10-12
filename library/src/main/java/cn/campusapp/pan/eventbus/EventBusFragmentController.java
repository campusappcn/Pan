package cn.campusapp.pan.eventbus;


import cn.campusapp.pan.lifecycle.OnDestroyViewFragment;
import cn.campusapp.pan.lifecycle.OnViewCreatedFragment;

/**
 * 这个现有实现不好，需要在Activity的onCreate和onDestory中绑定
 * 这样页面在背后的情况下，也是能够渲染页面的
 *
 * Created by nius on 7/29/15.
 */
public interface EventBusFragmentController extends EventBusController, OnViewCreatedFragment, OnDestroyViewFragment {
}
