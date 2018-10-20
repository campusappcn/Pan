package cn.campusapp.pan;

import android.widget.Button;

import butterknife.BindView;
import cn.campusapp.pan.annotaions.Xml;

@Xml(R.layout.view_component)
public class MainViewModel extends GeneralViewModel {

    @BindView(R.id.hello)
    Button vHelloTv;

    String mHelloString;

    @Override
    public MainViewModel render() {

        vHelloTv.setText(mHelloString);

        return this;
    }

    public MainViewModel setHelloString(String string) {
        mHelloString = string;
        return this;
    }
}
