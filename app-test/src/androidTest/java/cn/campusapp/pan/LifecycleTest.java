package cn.campusapp.pan;

import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cn.campusapp.pan.lifecycle.OnDestroy;
import cn.campusapp.pan.lifecycle.OnPause;
import cn.campusapp.pan.lifecycle.OnPostCreate;
import cn.campusapp.pan.lifecycle.OnResume;
import cn.campusapp.pan.lifecycle.OnStart;
import cn.campusapp.pan.lifecycle.OnStop;
import timber.log.Timber;

import static android.support.test.espresso.Espresso.pressBack;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class LifecycleTest {

    public static final String TAG = "LifecyclTest";

    final static CountDownLatch countDownLatch = new CountDownLatch(6);

    public static void countDown(){
        Log.e(TAG, "count down");
        countDownLatch.countDown();
    }

    @Rule
    public ActivityTestRule<MainActivity> mMainRule
            = new ActivityTestRule<>(
            MainActivity.class, false, false);


    @Before
    public void plantLog() {
        Timber.plant(new Timber.DebugTree());
//        Pan.installPlugin(new EventBusLifecyclePlugin(EventBus.getDefault()));
    }

    protected void launch(MainActivity.OnCreateProxy proxy) {
        mMainRule.launchActivity(MainActivity.makeIntentForTest(proxy));
    }

    @Test
    public void test() throws InterruptedException {

        launch(new OnCreateProxyImpl());

        mMainRule.getActivity().finish();


        countDownLatch.await(5, TimeUnit.SECONDS);
        assertEquals(0, countDownLatch.getCount());
    }

    public static class OnCreateProxyImpl implements MainActivity.OnCreateProxy {

        @Override
        public void onCreate(MainActivity activity, Bundle savedInstanceState) {

            LifeCycleViewModel vm = Pan.with(activity, LifeCycleViewModel.class)
                    .controlledBy(LifeCycleController.class)
                    .getViewModel();

            

        }
    }

    public static class LifeCycleViewModel extends GeneralViewModel {

        @Override
        public LifeCycleViewModel render() {
            return this;
        }
    }

    public static class LifeCycleController extends GeneralController<LifeCycleViewModel>
            implements Serializable,
//            EventBusController,
            OnPostCreate, OnStart, OnResume, OnPause, OnStop, OnDestroy {

        @Override
        protected void bindEvents() {
        }

        private void countDown() {
            LifecycleTest.countDown();
        }

        @Override
        public void onPostCreate(Bundle savedInstanceState) {
            countDown();
        }


        @Override
        public void onResume() {
            countDown();
        }

        @Override
        public void onStart() {
            countDown();
        }


        @Override
        public void onDestroy() {
            countDown();
        }

        @Override
        public void onPause() {
            countDown();
        }

        @Override
        public void onStop() {
            countDown();
        }
    }


}