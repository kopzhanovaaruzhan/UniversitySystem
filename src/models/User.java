package models;

import java.io.Serializable;
import java.util.Objects;

import enums.Language;


public abstract class User implements UserComponent, Serializable, NewsObserver {
	private static final long serialVersionUID = 1L;

    private String id;
    private String login;
    private String password;
    private String name;
    
    private Language currentLanguage = Language.ENG;

    public User(String id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    @Override
    public void viewNews() {
        NewsService.getInstance().printNews(this);
    }

    @Override
    public void updateNews(News n) {
        System.out.println(getLanguageMessage(
            "New notification: ", "Новое уведомление: ", "Жаңа хабарлама: "
        ) + n.getTopic());
    }
    

    public void switchLanguage(Language lang) {
        this.currentLanguage = lang;
    }

    public boolean login(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }
    
    public String getLanguageMessage(String eng, String rus, String kaz) {
        switch (currentLanguage) {
            case RUS: return rus;
            case KAZ: return kaz;
            case ENG: default: return eng;
        }
    }
    
    @Override
    public String getName() { return name; }

    public String getId() { return id; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public Language getCurrentLanguage() { return currentLanguage; }
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }

    @Override
    public String toString() {
        return String.format("User[ID='%s', Name='%s']", id, name);
    }
}




