package cn.campusapp.sample_github_users.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 事件实体类。
 * <p>
 * Created by nius on 4/12/16.
 */
public class GithubEvent {

    public static final String TYPE_PUSH = "PushEvent";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

    String type;

    Repo repo;

    Actor actor;

    Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Repo getRepo() {
        return repo;
    }

    public void setRepo(Repo repo) {
        this.repo = repo;
    }

    public Map<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("event",
                DATE_FORMAT.format(createdAt) + "  " +
//                        (actor.getLogin() == null ? "anonymous" : actor.getLogin()) +
                        "推送" +
                        (repo.getName() == null ? "anonymous repo" : repo.getName())
        );

        return map;
    }

    public static class Actor {
        String login;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }
    }

    public static class Repo {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
