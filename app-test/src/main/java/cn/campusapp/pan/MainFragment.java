package cn.campusapp.pan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nius on 10/13/15.
 */
public class MainFragment extends PanFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = Pan.with(this, MainViewModel.class)
                .controlledBy(MainController.class)
                .getViewModel(container, null, false)
                .setHelloString("hello Pan Fragment!")
                .render()
                .getRootView();

        Pan.with(this, AutoRenderTextViewModel.class)
                .getViewModel(rootView)
                .autoRender();

        return rootView;
    }
}
