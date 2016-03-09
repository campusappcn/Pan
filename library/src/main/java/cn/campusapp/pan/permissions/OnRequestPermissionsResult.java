package cn.campusapp.pan.permissions;

import android.support.annotation.NonNull;

import cn.campusapp.pan.lifecycle.LifecycleObserver;

/**
 * 运行时请求权限的回调
 * Created by chen on 16/3/9.
 */
public interface OnRequestPermissionsResult extends LifecycleObserver {
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
