package controllers;

import models.News;
import enums.NewsTopic;
import models.NewsService;

public class NewsController {

    public static void publishNews(String content, NewsTopic topic) {
        News news = new News(content, topic);
        
        NewsService.getInstance().addNews(news);
        
        core.DBContext.save();
    }
}