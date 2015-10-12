package cn.campusapp.pan.lifecycle;

import android.os.Bundle;

/**
 * Created by nius on 10/12/15.
 */
public interface OnRestoreInstanceState extends LifecycleObserver, LifecycleObserver.ActivityOnly {

    /**
     *
     * @param savedInstance saved instance bundle
     * @return should Pan call super method.
     */
    boolean onRestoreInstanceState(Bundle savedInstance);
}
