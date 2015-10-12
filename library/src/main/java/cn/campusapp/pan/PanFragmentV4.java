package cn.campusapp.pan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import cn.campusapp.pan.lifecycle.LifecycleObserved;


/**
 * Created by nius on 7/22/15.
 */
public class PanFragmentV4 extends Fragment implements LifecycleObserved {

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Pan.setUserVisibleHint(this, isVisibleToUser);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Pan.onViewCreated(this, view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Pan.onDestroyView(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        Pan.onResume(this);
    }
}
