package cn.campusapp.pan;

import android.os.Bundle;

public class MainActivity extends PanFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pan.with(this, MainViewModel.class)
                .controlledBy(MainController.class)
                .getViewModel()
                .setHelloString("hello Pan!")
                .render();

        Pan.with(this, AutoRenderTextViewModel.class)
                .getViewModel()
                .autoRender();
    }

}
