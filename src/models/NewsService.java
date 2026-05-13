package models;
import java.util.*;
public class NewsService {
    private List<News> newsList = new ArrayList<>();
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
        for (News news : newsList) {
            System.out.println(news);
        }
    }
}
