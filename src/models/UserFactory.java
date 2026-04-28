package models;

public interface UserFactory {
    User createUser(String type, String id, String name, String login, String password);
}