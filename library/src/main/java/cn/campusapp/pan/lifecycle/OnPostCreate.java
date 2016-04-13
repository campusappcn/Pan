package cn.campusapp.pan.lifecycle;

import android.os.Bundle;

public interface OnPostCreate extends LifecycleObserver, LifecycleObserver.ForActivity {

    void onPostCreate(Bundle savedInstanceState);
}
