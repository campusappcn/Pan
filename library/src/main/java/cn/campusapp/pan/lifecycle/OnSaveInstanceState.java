package cn.campusapp.pan.lifecycle;

import android.os.Bundle;

/**
 * Created by nius on 10/12/15.
 */
public interface OnSaveInstanceState extends LifecycleObserver {
    /**
     *
     * @param outState instance bundle to save
     * @return should Pan call super method.
     */
    boolean onSaveInstanceState(Bundle outState);
}
