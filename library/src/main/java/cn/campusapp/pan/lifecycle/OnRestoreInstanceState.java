package cn.campusapp.pan.lifecycle;

import android.os.Bundle;

public interface OnRestoreInstanceState extends LifecycleObserver, LifecycleObserver.ForActivity {

    /**
     *
     * @param savedInstance saved instance bundle
     * @return should Pan call super method.
     */
    boolean onRestoreInstanceState(Bundle savedInstance);
}
