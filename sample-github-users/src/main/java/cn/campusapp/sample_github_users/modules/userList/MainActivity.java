package cn.campusapp.sample_github_users.modules.userList;

import android.os.Bundle;

import cn.campusapp.pan.Pan;
import cn.campusapp.pan.PanActivity;
import cn.campusapp.sample_github_users.R;
import cn.campusapp.sample_github_users.modules.userList.UserListController;
import cn.campusapp.sample_github_users.modules.userList.UserListViewModel;

public class MainActivity extends PanActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pan.with(this, UserListViewModel.class)
                .controlledBy(UserListController.class)
                .getViewModel()
                .render();
    }
}
