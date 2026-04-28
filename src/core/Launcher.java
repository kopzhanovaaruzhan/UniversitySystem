package core;

import views.MainView;

public class Launcher {
    public static void main(String[] args) {
        try {
            MainView.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}