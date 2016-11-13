package android.cesi.todo.model;

public class User {

    String user;

    private long date;

    public User(String user, long date) {
        this.user = user;
        this.date = date;
    }

    public String getUserName() {
        return user;
    }

    public long getDate() {
        return date;
    }

}