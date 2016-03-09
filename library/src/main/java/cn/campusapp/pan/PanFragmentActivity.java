package cn.campusapp.pan;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import cn.campusapp.pan.interaction.OnBackPressed;
import cn.campusapp.pan.lifecycle.LifecycleObserved;
import cn.campusapp.pan.lifecycle.OnActivityResult;
import cn.campusapp.pan.lifecycle.OnConfigurationChanged;
import cn.campusapp.pan.lifecycle.OnDestroy;
import cn.campusapp.pan.lifecycle.OnNewIntent;
import cn.campusapp.pan.lifecycle.OnPause;
import cn.campusapp.pan.lifecycle.OnPostCreate;
import cn.campusapp.pan.permissions.OnRequestPermissionsResult;
import cn.campusapp.pan.lifecycle.OnRestart;
import cn.campusapp.pan.lifecycle.OnRestoreInstanceState;
import cn.campusapp.pan.lifecycle.OnResume;
import cn.campusapp.pan.lifecycle.OnSaveInstanceState;
import cn.campusapp.pan.lifecycle.OnStart;
import cn.campusapp.pan.lifecycle.OnStop;


/**
 * 用于监控生命周期
 * Created by nius on 7/22/15.
 */
public class PanFragmentActivity extends FragmentActivity implements LifecycleObserved {

    public static final String TAG_PAN = "Pan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Pan.call(this, OnStart.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Pan.call(this, OnResume.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Pan.call(this, OnPause.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Pan.call(this, OnStop.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Pan.call(this, OnDestroy.class);
    }


    @Override
    public void onBackPressed() {
        if (Pan.call(this, OnBackPressed.class)) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Pan.call(this, OnActivityResult.class, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (Pan.call(this, OnSaveInstanceState.class, outState)) {
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (Pan.call(this, OnRestoreInstanceState.class, savedInstanceState)) {
            super.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (Pan.call(this, OnConfigurationChanged.class, newConfig)) {
            super.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Pan.call(this, OnRestart.class);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Pan.call(this, OnNewIntent.class);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Pan.call(this, OnPostCreate.class, savedInstanceState);
    }

    @CallSuper
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Pan.call(this, OnRequestPermissionsResult.class, requestCode, permissions, grantResults);
    }
}
