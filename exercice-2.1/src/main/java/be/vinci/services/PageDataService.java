package be.vinci.services;

import be.vinci.domain.Page;
import be.vinci.domain.User;
import org.apache.commons.text.StringEscapeUtils;

import java.util.List;

public class PageDataService {

    private static final String COLLECTION_NAME = "pages";
    private static Json<Page> jsonDB = new Json<>(Page.class);

    public List<Page> getAll() {
        var pages = jsonDB.parse(COLLECTION_NAME);
        return pages.stream().filter(page -> page.getStatus().equals("published")).toList();
    }

    public List<Page> getAll(User authenticatedUser) {
        var pages = jsonDB.parse(COLLECTION_NAME);
        return pages.stream().filter(page -> page.getStatus().equals("published") || page.getAuthorId() == authenticatedUser.getId()).toList();
    }

    public Page getOne(int id) {
        var pages = jsonDB.parse(COLLECTION_NAME);
        return pages.stream().filter(page -> page.getId() == id && page.getStatus().equals("published")).findAny().orElse(null);
    }

    public Page getOne(int id, User authenticatedUser) {
        var pages = jsonDB.parse(COLLECTION_NAME);
        return pages.stream().filter(page -> page.getId() == id && page.getStatus().equals("published") || page.getAuthorId() == authenticatedUser.getId()).findAny().orElse(null);
    }

    public Page createOne(Page page, User authenticatedUser) {
        var pages = jsonDB.parse(COLLECTION_NAME);
        page.setId(nextPageId());
        page.setTitle(StringEscapeUtils.escapeHtml4(page.getTitle()));
        page.setUri(StringEscapeUtils.escapeHtml4(page.getUri()));
        page.setContent(StringEscapeUtils.escapeHtml4(page.getContent()));
        page.setAuthorId(authenticatedUser.getId());
        pages.add(page);
        jsonDB.serialize(pages, COLLECTION_NAME);
        return page;
    }

    private int nextPageId() {
        var pages = jsonDB.parse(COLLECTION_NAME);
        if (pages.size() == 0) {
            return 1;
        }
        return pages.get(pages.size() - 1).getId() + 1;
    }

    public Page deleteOne(int id, User authenticatedUser) {
        var pageToDelete = getOne(id, authenticatedUser);
        var pages = jsonDB.parse(COLLECTION_NAME);

        if (pageToDelete == null) {
            return null;
        }

        pages.remove(pageToDelete);
        jsonDB.serialize(pages, COLLECTION_NAME);
        return pageToDelete;
    }

    public Page updateOne(Page page, int id, User authenticatedUser) {
        Page pageToUpdate = getOne(id, authenticatedUser);
        var pages = jsonDB.parse(COLLECTION_NAME);

        if (pageToUpdate == null) {
            return null;
        }
        if (pageToUpdate.getAuthorId() != authenticatedUser.getId()) {
            throw new IllegalStateException("Forbidden");
        }

        pageToUpdate.setId(id);

        if (page.getTitle() != null) {
            pageToUpdate.setTitle(StringEscapeUtils.escapeHtml4(page.getTitle()));
        }
        if (page.getUri() != null) {
            pageToUpdate.setUri(StringEscapeUtils.escapeHtml4(page.getUri()));
        }
        if (page.getContent() != null) {
            pageToUpdate.setContent(StringEscapeUtils.escapeHtml4(page.getContent()));
        }
        if (page.getAuthorId() != 0) {
            pageToUpdate.setAuthorId(page.getAuthorId());
        }
        if (page.getStatus() != null) {
            pageToUpdate.setStatus(page.getStatus());
        }
        pages.remove(page);
        pages.add(pageToUpdate);
        jsonDB.serialize(pages, COLLECTION_NAME);
        return pageToUpdate;
    }
}
