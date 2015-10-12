package cn.campusapp.pan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import cn.campusapp.pan.lifecycle.LifecycleObserved;


/**
 * 用于监控生命周期
 * 以及用于调用对应的controller
 *
 * Created by nius on 7/22/15.
 */
public class PanActivity extends Activity implements LifecycleObserved {

    public static final String TAG_PAN = "Pan";

    public enum Lifecycle {
        INIT,
        CREATED,
        STARTED,
        RESUMED,
        DESTROYED
    }

    private Lifecycle mLifeCycle = Lifecycle.INIT;

    public Lifecycle getLifeCycle(){
        return mLifeCycle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLifeCycle = Lifecycle.CREATED;

//        Timber.tag(TAG_PAN).i("Activity创建 %s", this.getClass().getSimpleName());
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLifeCycle = Lifecycle.STARTED;
        Pan.onStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLifeCycle = Lifecycle.RESUMED;
        Pan.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        mLifeCycle = Lifecycle.STARTED;
        Pan.onPause(this);

    }

    @Override
    protected void onStop() {
        super.onStop();

        mLifeCycle = Lifecycle.CREATED;
        Pan.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Timber.tag(TAG_PAN).i("Activity销毁 %s", this.getClass().getSimpleName());

        mLifeCycle = Lifecycle.DESTROYED;
        Pan.onDestroy(this);
    }

    /**
     * If setContentView is called passing in a view, and that view has setFocusable(true) called on it, all key events will bypass the activity and go straight into the view.
     * On the other hand, if your onKeyDown is in the view, and you haven't called setContentView on the Activity and setFocusable(true) on the view, then your Activity will get the key events and not the View.
     *
     * 注意！如果子类里面的view里面可以focus，这个地方是没有用的！
     *
     * @param keyCode key code
     * @param event event
     * @return boolean
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Pan.onKeyDown(this, keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    /**
     * If setContentView is called passing in a view, and that view has setFocusable(true) called on it, all key events will bypass the activity and go straight into the view.
     * On the other hand, if your onKeyDown is in the view, and you haven't called setContentView on the Activity and setFocusable(true) on the view, then your Activity will get the key events and not the View.
     *
     * 注意！如果子类里面的view里面可以focus，这个地方是没有用的！
     *
     * @param keyCode key code
     * @param event event
     * @return boolean
     */
    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        Pan.onKeyUp(this, keyCode, event);
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Pan.onBackPressed(this);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Timber.tag(TAG_PAN).i("onActivityResult %s, request %s , result %s", this.getClass().getSimpleName(), requestCode, resultCode);
        Pan.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if(Pan.onRestoreInstanceState(this, savedInstanceState)){
            super.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
//        if(intent.getComponent() != null) {
//            Timber.tag(TAG_PAN).i("%s启动%s以获得结果", this.getClass().getSimpleName(), intent.getComponent().getClassName());
//        }else{
//            Timber.tag(TAG_PAN).i("%s启动%s以获得结果，没有Component", this.getClass().getSimpleName(), intent.toString());
//        }
        super.startActivityForResult(intent, requestCode, options);
    }

    /**
     * 跟上面的方法似乎是互斥的，所以必须都要搞
     *
     * 妈蛋，在5.0一下机器会闪退 by nius 2015-8-29
     *
     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
//        if(Pan.onRestoreInstanceState(this, savedInstanceState)) {
//            super.onRestoreInstanceState(savedInstanceState, persistentState);
//        }
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(Pan.onSaveInstanceState(this, outState)){
            super.onSaveInstanceState(outState);
        }
    }
}
