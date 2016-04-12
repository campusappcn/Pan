package cn.campusapp.pan;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cn.campusapp.pan.lifecycle.LifecycleObserver;
import cn.campusapp.pan.lifecycle.OnResume;
import cn.campusapp.pan.lifecycle.OnViewCreated;
import cn.campusapp.pan.lifecycle.PanLifecyclePlugin;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PanLifecyclePluginTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainRule = new ActivityTestRule<>(MainActivity.class, false, false);

    int count = 2;

    @Before
    public void before(){

        Pan.installPlugin(new PanLifecyclePlugin() {
            @Override
            public void onActivityLifecycle(Activity target, Class<? extends LifecycleObserver> lifecycle, Object... parameters) {
                if(OnResume.class.equals(lifecycle)){
                    count --;
                }
            }

            @Override
            public void onFragmentLifecycle(Fragment target, Class<? extends LifecycleObserver> lifecycle, Object... parameters) {

                if (OnViewCreated.class.equals(lifecycle)){
                    count --;
                }
            }
        });

    }

    @Test
    public void test(){
        mMainRule.launchActivity(new Intent());
        assertEquals(0, count);
    }


}
