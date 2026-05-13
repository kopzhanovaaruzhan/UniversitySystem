package models;

import enums.Language;

public interface UserComponent {
    void viewNews();
    void updateNews(News n);
    String getName(); 
    String getLanguageMessage(String en, String ru, String kz);
    void switchLanguage(Language lang);
    Language getCurrentLanguage();
}