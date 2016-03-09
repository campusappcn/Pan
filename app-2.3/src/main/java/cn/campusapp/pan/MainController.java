package cn.campusapp.pan;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

import cn.campusapp.pan.permissions.OnRequestPermissionsResult;
import timber.log.Timber;

/**
 * Created by nius on 10/13/15.
 */
public class MainController extends GeneralController<MainViewModel> implements OnRequestPermissionsResult {
    public static final String[] USES_PERMISSIONS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void bindEvents() {
        $vm.vHelloTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), $vm.mHelloString, Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions($vm.getActivity(), USES_PERMISSIONS, 0x77);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0x77) {
            Timber.d("Request permissions: %s", Arrays.toString(permissions));
            Timber.d("Granted permissions: %s", Arrays.toString(grantResults));
        }
    }
}
