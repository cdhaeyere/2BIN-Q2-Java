package be.vinci.services;

import be.vinci.domain.News;
import be.vinci.domain.Page;
import be.vinci.domain.User;
import be.vinci.services.utils.Json;
import jakarta.inject.Inject;
import org.apache.commons.text.StringEscapeUtils;

import java.util.List;

public class NewsDataServiceImpl implements NewsDataService {

  private static final String COLLECTION_NAME = "news";
  private static Json<News> jsonDB = new Json<>(News.class);
  @Inject
  private PageDataService myPageDataService;

  @Override
  public List<News> getAll() {
    var news = jsonDB.parse(COLLECTION_NAME);
    return jsonDB.filterPublicJsonViewAsList(news.stream().filter(item ->
            item.getPublicationStatus().contentEquals("published"))
        .toList());

  }

  @Override
  public List<News> getAll(int pageId) {
    var news = jsonDB.parse(COLLECTION_NAME);
    return jsonDB.filterPublicJsonViewAsList(news.stream().filter(item ->
            item.getPublicationStatus().contentEquals("published")
                && item.getAssociatedPage() != null && item.getAssociatedPage().getId() == pageId)
        .toList());

  }

  @Override
  public List<News> getAll(User authenticatedUser) {
    var news = jsonDB.parse(COLLECTION_NAME);
    return jsonDB.filterPublicJsonViewAsList(
        news.stream().filter(item -> item.getPublicationStatus().contentEquals("published")
            || item.getAuthor().equals(authenticatedUser)).toList());

  }

  @Override
  public List<News> getAll(User authenticatedUser, int pageId) {
    var news = jsonDB.parse(COLLECTION_NAME);
    return jsonDB.filterPublicJsonViewAsList(
        news.stream().filter(item -> (item.getPublicationStatus().contentEquals("published")
                || item.getAuthor().equals(authenticatedUser))
                && item.getAssociatedPage() != null && item.getAssociatedPage().getId() == pageId)
            .toList());

  }

  @Override
  public News getOne(int id) {
    var news = jsonDB.parse(COLLECTION_NAME);
    return jsonDB.filterPublicJsonView(news.stream().filter(item -> (item.getId() == id)
            && (item.getPublicationStatus().contentEquals("published")))
        .findAny().orElse(null));
  }

  @Override
  public News getOne(int id, User authenticatedUser) {
    var news = jsonDB.parse(COLLECTION_NAME);
    return jsonDB.filterPublicJsonView(news.stream().filter(item -> (item.getId() == id)
            && (item.getPublicationStatus().contentEquals("published")
            || item.getAuthor().equals(authenticatedUser)))
        .findAny().orElse(null));
  }

  @Override
  public News createOne(News newsItem, User authenticatedUser) {
    var allNews = jsonDB.parse(COLLECTION_NAME);
    newsItem.setId(nextNewsItemId());
    // escape dangerous chars to protect against XSS attacks
    newsItem.setTitle(StringEscapeUtils.escapeHtml4(newsItem.getTitle()));
    newsItem.setShortDescription(StringEscapeUtils.escapeHtml4(newsItem.getShortDescription()));
    newsItem.setContent(StringEscapeUtils.escapeHtml4(newsItem.getContent()));
    // if an associated page is provided to be added
    if (newsItem.getAssociatedPage() != null) {
      Page associatedPage = myPageDataService.getOne(newsItem.getAssociatedPage().getId(),
          authenticatedUser);
      if (associatedPage != null) {
        newsItem.setAssociatedPage(associatedPage);
      }
    }
    newsItem.setAuthor(authenticatedUser);
    allNews.add(newsItem);

    jsonDB.serializePublicInfoOnly(allNews,
        COLLECTION_NAME); // Don't serialise internal fields (only public)
    return jsonDB.filterPublicJsonView(newsItem); // Set all internal fields to default values
  }


  @Override
  public News deleteOne(int id, User authenticatedUser) {
    News newsItemToDelete = getOne(id, authenticatedUser);
    var news = jsonDB.parse(COLLECTION_NAME);
    if (newsItemToDelete == null) {
      return null;
    }
    if (!newsItemToDelete.getAuthor().equals(authenticatedUser)) {
      throw new IllegalStateException("Forbidden");
    }
    news.remove(newsItemToDelete);
    jsonDB.serializePublicInfoOnly(news, COLLECTION_NAME);
    return jsonDB.filterPublicJsonView(newsItemToDelete);
  }

  @Override
  public News updateOne(News newsItem, int id, User authenticatedUser) {
    News newsItemToUpdate = getOne(id, authenticatedUser);
    if (newsItemToUpdate == null) {
      return null;
    }
    if (!newsItemToUpdate.getAuthor().equals(authenticatedUser)) {
      throw new IllegalStateException("Forbidden");
    }

    var news = jsonDB.parse(COLLECTION_NAME);
    newsItemToUpdate.setId(id);
    // escape dangerous chars to protect against XSS attacks
    if (newsItem.getTitle() != null) {
      newsItemToUpdate.setTitle(StringEscapeUtils.escapeHtml4(newsItem.getTitle()));
    }
    if (newsItem.getShortDescription() != null) {
      newsItemToUpdate.setShortDescription(
          StringEscapeUtils.escapeHtml4(newsItem.getShortDescription()));
    }
    if (newsItem.getContent() != null) {
      newsItemToUpdate.setContent(StringEscapeUtils.escapeHtml4(newsItem.getContent()));
    }

    if (newsItem.getPublicationStatus() != null) {
      newsItemToUpdate.setPublicationStatus(newsItem.getPublicationStatus());
    }
    // if an associated page is provided to be updated
    if (newsItem.getAssociatedPage() != null) {
      Page associatedPage = myPageDataService.getOne(newsItem.getAssociatedPage().getId(),
          authenticatedUser);
      if (associatedPage != null) {
        newsItem.setAssociatedPage(associatedPage);
      }
    }

    if (newsItem.getPositionInPage() != 0) {
      newsItemToUpdate.setPositionInPage(newsItem.getPositionInPage());
    }

    if (newsItem.getEndDateTime() != null) {
      newsItemToUpdate.setEndDateTime(newsItem.getEndDateTime());
    }
    news.remove(newsItemToUpdate); // thanks to equals(), pages is found via its id
    news.add(newsItemToUpdate);
    jsonDB.serializePublicInfoOnly(news, COLLECTION_NAME);
    return jsonDB.filterPublicJsonView(newsItemToUpdate);
  }

  @Override
  public int nextNewsItemId() {
    var pages = jsonDB.parse(COLLECTION_NAME);
    if (pages.size() == 0) {
      return 1;
    }
    return pages.get(pages.size() - 1).getId() + 1;
  }
}

