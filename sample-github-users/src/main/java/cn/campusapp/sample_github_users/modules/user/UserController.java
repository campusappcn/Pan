package cn.campusapp.sample_github_users.modules.user;

import android.util.Log;
import android.widget.Toast;

import cn.campusapp.pan.GeneralController;
import cn.campusapp.sample_github_users.entity.GithubUser;
import cn.campusapp.sample_github_users.rest.GitHubService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nius on 4/12/16.
 */
public class UserController extends GeneralController<UserViewModel> {


    @Override
    protected void onBindViewModel() {
        super.onBindViewModel();
    }

    @Override
    protected void bindEvents() {
    }

    public void updateInfoIfNecessary() {
        if ($vm.mGithubUser == null || $vm.mGithubUser.getAvatarUrl() != null) {
            return;
        }

        final GithubUser bindedUser = $vm.mGithubUser;

        GitHubService.Impl.getUser($vm.mGithubUser.getLogin()).enqueue(new Callback<GithubUser>() {

            @Override
            public void onResponse(Call<GithubUser> call, Response<GithubUser> response) {
                try {
                    bindedUser.setAvatarUrl(response.body().getAvatarUrl());
                    bindedUser.setName(response.body().getName());
                    $vm.render();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Github limit 60 requests per hour!", Toast.LENGTH_LONG).show();
                    Log.e("wtf", "wtf", e);
                }
            }

            @Override
            public void onFailure(Call<GithubUser> call, Throwable t) {
                Toast.makeText(getActivity(), "network is gone!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
