package models;

import java.io.Serializable;

public abstract class UserDecorator implements UserComponent, Serializable {
    private static final long serialVersionUID = 1L;
    
    protected UserComponent component;

    public UserDecorator(UserComponent user) {
        this.component = user;
    }

    public UserComponent getComponent() {
        return component;
    }

    @Override
    public void viewNews() {
        component.viewNews();
    } 

    @Override
    public void updateNews(News n) {
        component.updateNews(n);
    }
    
    
    @Override
    public String getName() {
        return component.getName();
    }
    
}