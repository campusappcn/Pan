package cn.campusapp.pan.lifecycle;

import android.content.res.Configuration;

public interface OnConfigurationChanged extends LifecycleObserver{

    /**
     *
     * @param newConfig new configuration
     * @return should call super method
     */
    boolean onConfigurationChanged(Configuration newConfig);
}
