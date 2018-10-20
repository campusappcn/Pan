package cn.campusapp.pan.lifecycle;

import android.os.Bundle;
import android.view.View;

/**
 * Fragment的View刚被创建完
 * <p>
 * 一般来讲，在Fragment.onCreateView中绑定Controller
 * 此方法在其后被调用，可以用来触发Controller
 * <p>
 * Created by nius on 7/29/15.
 */
public interface OnViewCreated extends LifecycleObserver, LifecycleObserver.ForFragment {

    void onViewCreated(View view, Bundle savedInstanceState);
}
