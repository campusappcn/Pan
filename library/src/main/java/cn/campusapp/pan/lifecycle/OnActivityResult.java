package cn.campusapp.pan.lifecycle;

import android.content.Intent;

/**
 * 对于Framgent.onActivityResult，同样可以用这个接口，因为两个其实调用位置是一样的
 * 在Pan中，继承这个接口哪怕是在Fragment中绑定的，也是会被加入到观察者Map中，被调用
 * <p>
 * Created by nius on 7/22/15.
 */
public interface OnActivityResult extends LifecycleObserver {
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
