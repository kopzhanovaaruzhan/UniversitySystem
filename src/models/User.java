package models;
import java.io.Serializable;
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String name;
    protected String login;
    protected String password;
    protected Language language;
    public User(String id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.language = Language.ENG;
    }

    public boolean login(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }

    public void switchLanguage(Language language) {
        this.language = language;
    }

    public Language getCurrentLanguage() {
        return language;
    }

    public String getLanguageMessage(String eng, String rus, String kaz) {
        if (language == Language.RUS) return rus;
        if (language == Language.KZ) return kaz;
        return eng;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String toString() {
        return getClass().getSimpleName() + " | " + id + " | " + name + " | login: " + login;
    }
}