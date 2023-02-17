package be.vinci.services;

import be.vinci.domain.News;
import be.vinci.domain.User;

import java.util.List;

public interface NewsDataService {
  List<News> getAll();

  List<News> getAll(int pageId);

  List<News> getAll(User authenticatedUser);

  List<News> getAll(User authenticatedUser, int pageId);

  News getOne(int id);

  News getOne(int id, User authenticatedUser);

  News createOne(News newsItem, User authenticatedUser);

  News deleteOne(int id, User authenticatedUser);

  News updateOne(News newsItem, int id, User authenticatedUser);

  int nextNewsItemId();
}
