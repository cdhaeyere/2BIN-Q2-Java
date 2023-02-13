package be.vinci.services;

import be.vinci.domain.User;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public interface UserDataService {
    List<User> getAll();

    User getOne(int id);

    User getOne(String login);

    User createOne(User item);

    int nextItemId();

    ObjectNode login(String login, String password);

    ObjectNode register(String login, String password);
}
