package be.vinci.api;

import be.vinci.api.filters.AnonymousOrAuthorize;
import be.vinci.api.filters.Authorize;
import be.vinci.domain.News;
import be.vinci.domain.User;
import be.vinci.services.NewsDataService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ContainerRequest;

import java.util.List;

@Singleton
@Path("news")
public class NewsResource {

  @Inject
  private NewsDataService myNewsDataService;

  @AnonymousOrAuthorize
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<News> getAll(@DefaultValue("0") @QueryParam("page-id") int pageId,
      @Context ContainerRequest request) { // request is fed by AnonymousOrAuthorizeRequestFilter
    User authenticatedUser = (User) request.getProperty("user");
    if (authenticatedUser == null) // deals with anonymous requests
    {
      if (pageId == 0) {
        return myNewsDataService.getAll();
      } else {
        return myNewsDataService.getAll(pageId);
      }

    }

    // deals with authenticated requests
    if (pageId == 0) {
      return myNewsDataService.getAll(authenticatedUser);
    } else {
      return myNewsDataService.getAll(authenticatedUser, pageId);
    }
  }


  @AnonymousOrAuthorize
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public News getOne(@PathParam("id") int id, @Context ContainerRequest request) {
    User authenticatedUser = (User) request.getProperty("user");
    News itemFound = null;

    if (authenticatedUser == null) // deals with anonymous requests
    {
      itemFound = myNewsDataService.getOne(id);
    } else {
      itemFound = myNewsDataService.getOne(id, authenticatedUser);
    }

    if (itemFound == null) {
      throw new WebApplicationException("Ressource not found", Response.Status.NOT_FOUND);
    }

    return itemFound;


  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public News createOne(News newsItem, @Context ContainerRequest request) {

    if (newsItem == null || newsItem.getTitle() == null || newsItem.getTitle().isBlank()
        || newsItem.getContent() == null || newsItem.getContent().isBlank()
        || newsItem.getPublicationStatus() == null
        || newsItem.getShortDescription() == null || newsItem.getShortDescription().isBlank()) {
      throw new WebApplicationException("Lacks of mandatory info", Response.Status.BAD_REQUEST);
    }

    User authenticatedUser = (User) request.getProperty("user");
    return myNewsDataService.createOne(newsItem, authenticatedUser);
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public News deleteOne(@PathParam("id") int id, @Context ContainerRequest request) {
    if (id == 0) // default value of an integer => has not been initialized
    {
      throw new WebApplicationException("Lacks of mandatory id info",
          Response.Status.BAD_REQUEST);
    }
    User authenticatedUser = (User) request.getProperty("user");
    News deletedItem = null;
    deletedItem = myNewsDataService.deleteOne(id, authenticatedUser);
    if (deletedItem == null) {
      throw new WebApplicationException("Ressource not found", Response.Status.NOT_FOUND);
    }
    return deletedItem;
  }

  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public News updateOne(News newsItem, @PathParam("id") int id, @Context ContainerRequest request) {
    if (newsItem == null || newsItem.getTitle() == null || newsItem.getTitle().isBlank()
        || newsItem.getContent() == null || newsItem.getContent().isBlank()
        || newsItem.getPublicationStatus() == null
        || newsItem.getShortDescription() == null || newsItem.getShortDescription().isBlank()) {
      throw new WebApplicationException("Lacks of mandatory info", Response.Status.BAD_REQUEST);
    }
    User authenticatedUser = (User) request.getProperty("user");
    News updatedItem = null;
    updatedItem = myNewsDataService.updateOne(newsItem, id, authenticatedUser);

    // TODO deal later with specific error message if authenticatedUser is not the author of the page
    if (updatedItem == null) {
      throw new WebApplicationException("Ressource not found", Response.Status.NOT_FOUND);
    }
    return updatedItem;
  }
}
