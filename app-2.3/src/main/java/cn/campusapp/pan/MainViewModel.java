package cn.campusapp.pan;

import android.widget.Button;

import butterknife.Bind;
import cn.campusapp.pan.annotaions.Xml;

/**
 * Created by nius on 10/13/15.
 */
@Xml(R.layout.view_component)
public class MainViewModel extends GeneralViewModel {

    @Bind(R.id.hello)
    Button vHelloTv;

    String mHelloString;

    @Override
    public ViewModel render() {

        vHelloTv.setText(mHelloString);

        return this;
    }

    public MainViewModel setHelloString(String string) {
        mHelloString = string;
        return this;
    }
}
