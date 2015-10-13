package cn.campusapp.pan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import cn.campusapp.pan.annotaions.Xml;

public class MainActivity extends PanActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pan.with(this, MainViewModel.class)
                .controlledBy(MainController.class)
                .getViewModel()
                .setHelloString("hello Pan!")
                .render();

    }

    @Xml(R.layout.activity_main)
    public static class MainViewModel extends GeneralViewModel {

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

    public static class MainController extends GeneralController<MainViewModel> {
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
}
