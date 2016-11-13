package android.cesi.todo.helper;

import android.cesi.todo.model.Task;
import android.cesi.todo.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class JsonParser {

    public static String getToken(String response) throws JSONException {
        return new JSONObject(response).optString("token");
    }

    public static List<Task> getTasks(String json) throws JSONException {
        List<Task> tasks = new LinkedList<>();
        JSONArray array = new JSONArray(json);
        JSONObject obj;
        Task task;
        for(int i=0; i < array.length(); i++){
            obj = array.getJSONObject(i);
            task = new Task(obj.optString("id"),
                    obj.optString("username"),
                    obj.optLong("date"),
                    obj.optString("note"),
                    obj.optBoolean("done")
            );
            tasks.add(task);
        }

        return tasks;
    }

    public static List<User> getUsers(String json) throws JSONException {
        List<User> users = new LinkedList<>();
        JSONArray array = new JSONArray(json);
        JSONObject obj;
        User user;
        for(int i=0; i < array.length(); i++){
            obj = array.getJSONObject(i);
            user = new User(obj.optString("username"), obj.optLong("date"));
            users.add(user);
        }

        return users;
    }
}