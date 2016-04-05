package cn.campusapp.pan.lifecycle;

import android.os.Bundle;

/**
 * Fragment's corresponding OnRestoreInstanceState
 *
 * Created by nius on 10/12/15.
 */
public interface OnActivityCreated extends LifecycleObserver, LifecycleObserver.ForFragment {

    boolean onActivityCreated(Bundle savedInstanceState);
}
