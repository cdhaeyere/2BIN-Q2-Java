package be.vinci.services;

import be.vinci.domain.Page;
import be.vinci.domain.User;
import be.vinci.services.utils.Json;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;

public class PageDataServiceImpl implements PageDataService {

    static final String COLLECTION_NAME = "pages";
    static Json<Page> jsonDB = new Json<>(Page.class);

    @Override
    public List<Page> getAll() {
        var pages = jsonDB.parse(COLLECTION_NAME);
        return pages.stream().filter(page -> page.getStatus().equals("published")).toList();
    }

    @Override
    public List<Page> getAll(User authenticatedUser) {
        var pages = jsonDB.parse(COLLECTION_NAME);
        return pages.stream().filter(page -> page.getStatus().equals("published") || page.getAuthorId() == authenticatedUser.getId()).toList();
    }

    @Override
    public Page getOne(int id) {
        var pages = jsonDB.parse(COLLECTION_NAME);
        return pages.stream().filter(page -> page.getId() == id && page.getStatus().equals("published")).findAny().orElse(null);
    }

    @Override
    public Page getOne(int id, User authenticatedUser) {
        var pages = jsonDB.parse(COLLECTION_NAME);
        return pages.stream().filter(page -> page.getId() == id && page.getStatus().equals("published") || page.getAuthorId() == authenticatedUser.getId()).findAny().orElse(null);
    }

    @Override
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

    @Override
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

    @Override
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
