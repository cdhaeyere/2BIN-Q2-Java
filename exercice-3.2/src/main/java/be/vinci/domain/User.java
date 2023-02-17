package be.vinci.domain;

public interface User {
    String getLogin();

    void setLogin(String login);

    String getPassword();

    void setPassword(String password);

    int getId();

    void setId(int id);

    boolean checkPassword(String password);

    String hashPassword(String password);

    @Override
    String toString();
}
