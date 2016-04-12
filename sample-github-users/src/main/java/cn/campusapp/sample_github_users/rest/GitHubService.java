package cn.campusapp.sample_github_users.rest;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import cn.campusapp.sample_github_users.entity.GithubUser;
import cn.campusapp.sample_github_users.entity.Repo;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by nius on 4/12/16.
 */
public interface GitHubService {

    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);

    @GET("users/{user}")
    Call<GithubUser> getUser(@Path("user") String user);

    Gson GsonObj= new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    //糟糕的写法，只是为了方便，核心在演示Pan
    GitHubService Impl = new Retrofit.Builder().baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonObj)).build().create(GitHubService.class);

}
