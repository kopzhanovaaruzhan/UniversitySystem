package models;

import java.util.*;
import java.io.Serializable;

public class NewsService implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final NewsService INSTANCE = new NewsService();
    private List<News> newsList = new ArrayList<>();

    private NewsService() {}

    public static NewsService getInstance() {
        return INSTANCE;
    }

    public void addNews(News news) {
        newsList.add(news);
        news.notifyObservers();
    }

    public void printNews(User currentUser) {
        newsList.sort((n1, n2) -> Boolean.compare(n2.isPinned(), n1.isPinned()));

        System.out.println(currentUser.getLanguageMessage(
                "News list:",
                "Список новостей:",
                "Жаңалықтар тізімі:"
        ));
        if (newsList.isEmpty()) {
            System.out.println("  " + currentUser.getLanguageMessage("No news yet.", "Новостей пока нет.", "Жаңалықтар жоқ."));
            return;
        }
        for (News news : newsList) {
            System.out.println(news);
        }
    }
}