package android.cesi.todo.model;

public class Task {

    private String id;
    private String user;
    private long date;
    private String note;
    private boolean done;

    public Task(String id, String user, long date, String note, boolean done) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.note = note;
        this.done = done;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return user;
    }

    public long getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public boolean getDone() {
        return done;
    }
}
