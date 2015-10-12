package cn.campusapp.pan.lifecycle;

import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * 观察Activity的持久化
 *
 * Created by nius on 8/28/15.
 */
public interface OnSaveOnRestoreActivity extends  ActivityLifecycleObserver {

    /**
     *
     * @param outState outState
     * @return should call super
     */
    boolean onSaveInstanceState(Bundle outState);

    /**
     *
     * @param savedInstanceState instance state
     * @return should call super
     */
    boolean onRestoreInstanceState(@NonNull Bundle savedInstanceState);

}
