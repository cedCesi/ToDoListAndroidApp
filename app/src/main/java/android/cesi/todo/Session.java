package android.cesi.todo;

public class Session {

    static Session session = null;

    String token = null;

    public static Session getInstance() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
