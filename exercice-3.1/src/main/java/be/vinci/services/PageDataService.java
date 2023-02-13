package be.vinci.services;

import be.vinci.domain.Page;
import be.vinci.domain.User;

import java.util.List;

public interface PageDataService {
    List<Page> getAll();

    List<Page> getAll(User authenticatedUser);

    Page getOne(int id);

    Page getOne(int id, User authenticatedUser);

    Page createOne(Page page, User authenticatedUser);

    default int nextPageId() {
        var pages = PageDataServiceImpl.jsonDB.parse(PageDataServiceImpl.COLLECTION_NAME);
        if (pages.size() == 0) {
            return 1;
        }
        return pages.get(pages.size() - 1).getId() + 1;
    }

    Page deleteOne(int id, User authenticatedUser);

    Page updateOne(Page page, int id, User authenticatedUser);
}
