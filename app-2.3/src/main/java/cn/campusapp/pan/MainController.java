package cn.campusapp.pan;

import android.view.View;
import android.widget.Toast;

/**
 * Created by nius on 10/13/15.
 */
public class MainController extends GeneralController<MainViewModel> {
    @Override
    protected void bindEvents() {
        $vm.vHelloTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), $vm.mHelloString, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
