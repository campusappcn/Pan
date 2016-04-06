package cn.campusapp.pan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

public class MainActivity extends PanActivity {

    public static final String KEY_PROXY = "PROXY";
    MainViewModel mMainViewModel;
    AutoRenderTextViewModel mAutoRenderTextViewModel;

    public static Intent makeIntentForTest(OnCreateProxy proxy) {
        Intent intent = new Intent();
        intent.putExtra(KEY_PROXY, proxy);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("MainActivity", "Launching");

        OnCreateProxy proxy = (OnCreateProxy) getIntent().getSerializableExtra(KEY_PROXY);

        if (proxy != null) {
            proxy.onCreate(this, savedInstanceState);
            return;
        }

        //if no Proxy, use default
        Pan.LOG.info("seems no proxy for MainActivity");

        mMainViewModel = Pan.with(this, MainViewModel.class)
                .controlledBy(MainController.class)
                .getViewModel()
                .setHelloString("hello Pan!")
                .render();

        mAutoRenderTextViewModel = Pan.with(this, AutoRenderTextViewModel.class)
                .getViewModel()
                .autoRender();

    }


    public interface OnCreateProxy extends Serializable {
        void onCreate(MainActivity activity, Bundle savedInstanceState);
    }

}
