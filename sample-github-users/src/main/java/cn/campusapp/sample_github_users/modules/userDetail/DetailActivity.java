package cn.campusapp.sample_github_users.modules.userDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.campusapp.pan.Pan;
import cn.campusapp.pan.PanActivity;
import cn.campusapp.sample_github_users.R;
import cn.campusapp.sample_github_users.entity.GithubUser;

public class DetailActivity extends PanActivity {

    public static final String EXTRA_KEY_LOGIN = "login";

    public static Intent makeIntent(Context context, String login) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_KEY_LOGIN, login);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String login = getIntent().getStringExtra(EXTRA_KEY_LOGIN);

        Pan.with(this, UserDetailViewModel.class)
                .controlledBy(UserDetailController.class)
                .getViewModel()
                .setUser(new GithubUser(login))
                .render();
    }
}
