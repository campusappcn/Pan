package cn.campusapp.pan.lifecycle;

import android.content.Context;

/**
 * Created by nius on 10/12/15.
 */
public interface OnAttach extends LifecycleObserver, LifecycleObserver.ForFragment {

    void onAttach(Context context);
}
