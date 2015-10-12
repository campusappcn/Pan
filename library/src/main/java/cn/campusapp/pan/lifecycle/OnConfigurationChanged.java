package cn.campusapp.pan.lifecycle;

import android.content.res.Configuration;

/**
 * e.g. device orientation changed
 *
 * Created by nius on 10/12/15.
 */
public interface OnConfigurationChanged extends LifecycleObserver{

    /**
     *
     * @param newConfig new configuration
     * @return should call super method
     */
    boolean onConfigurationChanged(Configuration newConfig);
}
