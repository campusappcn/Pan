package cn.campusapp.pan.lifecycle;

import android.os.Bundle;
import android.view.View;

/**
 * Fragment的View刚被创建完
 *
 * 一般来讲，在Fragment.onCreateView中绑定Controller
 * 此方法在其后被调用，可以用来出发Controller
 *
 * Created by nius on 7/29/15.
 */
public interface OnViewCreated extends LifecycleObserver, LifecycleObserver.FragmentOnly{

    void onViewCreated(View view, Bundle savedInstanceState);
}
