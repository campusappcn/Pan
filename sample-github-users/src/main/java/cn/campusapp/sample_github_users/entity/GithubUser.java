package cn.campusapp.sample_github_users.entity;

/**
 * ref: https://developer.github.com/v3/users/
 * <p>
 * Created by nius on 4/12/16.
 */
public class GithubUser {

    String login;

    String avatarUrl;

    String name;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GithubUser() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public GithubUser(String login) {
        this.login = login;
    }
}
