package be.vinci.api;

import be.vinci.domain.Page;
import be.vinci.domain.User;
import be.vinci.services.PageDataService;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ContainerRequest;
import be.vinci.api.filters.Authorize;

import java.util.List;

@Singleton
@Path("pages")
public class PageResource {

    private PageDataService myPageDataService = new PageDataService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Page> getAll(@Context ContainerRequest request) {
        User authenticatedUser = (User) request.getProperty("user");
        if (authenticatedUser == null) {
            return myPageDataService.getAll();
        }

        return myPageDataService.getAll(authenticatedUser);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Page getOne(@PathParam("id") int id, @Context ContainerRequest request) {
        User authenticatedUser = (User) request.getProperty("user");
        Page pageFound = null;

        if (authenticatedUser == null) {
            pageFound = myPageDataService.getOne(id);
        } else {
            pageFound = myPageDataService.getOne(id, authenticatedUser);
        }

        if (pageFound == null) {
            throw new WebApplicationException("Ressource not found", Response.Status.NOT_FOUND);
        }

        return pageFound;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authorize
    public Page createOne(Page page, @Context ContainerRequest request) {

        if (page == null || page.getTitle() == null || page.getTitle().isBlank()
                || page.getContent() == null || page.getContent().isBlank()
                || page.getStatus() == null
                || page.getUri() == null || page.getUri().isBlank()) {
            throw new WebApplicationException("Lacks of mandatory info", Response.Status.BAD_REQUEST);
        }

        User authenticatedUser = (User) request.getProperty("user");
        return myPageDataService.createOne(page, authenticatedUser);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authorize
    public Page deleteOne(@PathParam("id") int id, @Context ContainerRequest request) {
        if (id == 0) // default value of an integer => has not been initialized
        {
            throw new WebApplicationException("Lacks of mandatory id info",
                    Response.Status.BAD_REQUEST);
        }
        User authenticatedUser = (User) request.getProperty("user");
        Page deletedPage = null;
        try {
            deletedPage = myPageDataService.deleteOne(id, authenticatedUser);
        } catch (Exception e) {
            throw new WebApplicationException("You are not the author", e, Response.Status.FORBIDDEN);

        }
        // TODO deal later with specific error message if authenticatedUser is not the author of the page
        if (deletedPage == null) {
            throw new WebApplicationException("Ressource not found", Response.Status.NOT_FOUND);
        }
        return deletedPage;
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authorize
    public Page updateOne(Page page, @PathParam("id") int id, @Context ContainerRequest request) {
        if (page == null || page.getTitle() == null || page.getTitle().isBlank()
                || page.getContent() == null || page.getContent().isBlank()
                || page.getStatus() == null
                || page.getUri() == null || page.getUri().isBlank()) {
            throw new WebApplicationException("Lacks of mandatory info", Response.Status.BAD_REQUEST);
        }
        User authenticatedUser = (User) request.getProperty("user");
        Page updatedPage = null;
        try {
            updatedPage = myPageDataService.updateOne(page, id, authenticatedUser);
        } catch (Exception e) {
            throw new WebApplicationException("You are not the author", Response.Status.FORBIDDEN);
        }
        // TODO deal later with specific error message if authenticatedUser is not the author of the page
        if (updatedPage == null) {
            throw new WebApplicationException("Ressource not found", Response.Status.NOT_FOUND);
        }
        return updatedPage;
    }
}

