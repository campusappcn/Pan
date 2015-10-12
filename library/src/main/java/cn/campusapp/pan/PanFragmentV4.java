package cn.campusapp.pan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;

import cn.campusapp.pan.interaction.OnBackPressed;
import cn.campusapp.pan.lifecycle.LifecycleObserved;
import cn.campusapp.pan.lifecycle.OnActivityCreated;
import cn.campusapp.pan.lifecycle.OnActivityResult;
import cn.campusapp.pan.lifecycle.OnAttach;
import cn.campusapp.pan.lifecycle.OnConfigurationChanged;
import cn.campusapp.pan.lifecycle.OnDestroy;
import cn.campusapp.pan.lifecycle.OnDestroyView;
import cn.campusapp.pan.lifecycle.OnDetach;
import cn.campusapp.pan.lifecycle.OnHiddenChanged;
import cn.campusapp.pan.lifecycle.OnPause;
import cn.campusapp.pan.lifecycle.OnResume;
import cn.campusapp.pan.lifecycle.OnSaveInstanceState;
import cn.campusapp.pan.lifecycle.OnStart;
import cn.campusapp.pan.lifecycle.OnStop;
import cn.campusapp.pan.lifecycle.OnViewCreated;
import cn.campusapp.pan.lifecycle.OnVisible;


/**
 * Created by nius on 7/22/15.
 */
public class PanFragmentV4 extends Fragment implements LifecycleObserved {

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Pan.call(this, OnVisible.class, isVisibleToUser);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Pan.call(this, OnViewCreated.class, view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Pan.call(this, OnDestroyView.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        Pan.call(this, OnStart.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        Pan.call(this, OnResume.class);
    }

    @Override
    public void onPause() {
        super.onPause();
        Pan.call(this, OnPause.class);
    }

    @Override
    public void onStop() {
        super.onStop();
        Pan.call(this, OnStop.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Pan.call(this, OnDestroy.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Pan.call(this, OnActivityResult.class, requestCode, resultCode, data);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (Pan.call(this, OnConfigurationChanged.class, newConfig)) {
            super.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (Pan.call(this, OnSaveInstanceState.class, outState)) {
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Pan.call(this, OnActivityCreated.class, savedInstanceState)){
            super.onActivityCreated(savedInstanceState);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Pan.call(this, OnHiddenChanged.class);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Pan.call(this, OnAttach.class);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Pan.call(this, OnDetach.class);
    }

}
