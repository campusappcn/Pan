package cn.campusapp.sample_databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.campusapp.pan.Pan;
import cn.campusapp.pan.PanActivity;
import cn.campusapp.sample_databinding.databinding.ActivityMainBinding;

public class MainActivity extends PanActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);

        Pan.with(this, MainDataBindingViewModel.class)
                .viewFactory(DataBindingViewFactory.INSTANCE)//如果自行实例化View就需要这个
                .controlledBy(MainDataBindingController.class)
                .getViewModel()
                .setData(new User("hello data binding!"))
                .render();
    }

    public static class MainDataBindingViewModel extends DataBindingViewModel<User, ActivityMainBinding> {

        @Override
        protected void bindData(User data) {
            mViewDataBinding.setUser(data);
        }

        @Override
        public MainDataBindingViewModel render() {
            return this;
        }
    }

    public static class MainDataBindingController extends DataBindingController<MainDataBindingViewModel>{

        @Override
        protected void bindHandlers() {
            $vm.getViewDataBinding().setHandlers(this);
        }

        public void onclick(View v){
            Toast.makeText(getActivity(), "hello data binding click!", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void bindEvents() {

        }
    }
}
