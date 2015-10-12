package cn.campusapp.pan;

import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * 子类可以在Activity销毁后后重建，可以看RegisterModel写的例子
 * Created by nius on 9/14/15.
 */
public interface Restorable {

    void onRestoreInstanceState(@NonNull Bundle savedInstanceState);

    void onSaveInstanceState(Bundle outState);
}
