package be.vinci;

import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.text.StringEscapeUtils;

import java.util.List;

/**
 * Root resource (exposed at "myresource" path)
 */
@Singleton
@Path("texts")
public class TextResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Text> getAll(@DefaultValue("") @QueryParam("level") String level) {
        var texts = Json.parse();
        if (level.isBlank()) {
            return texts;
        } else {
            return texts.stream().filter(t -> t.getLevel().equals(level)).toList();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Text getText(@PathParam("id") int id) {
        var texts = Json.parse();
        Text textFound = texts.stream().filter(text -> text.getId() == id).findAny().orElse(null);
        if (textFound == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Ressource not found").type("text/plain").build());
        }
        return textFound;

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Text createOne(Text text) {
        if (text == null || text.getContent() == null || text.getLevel() == null || text.getContent().isBlank()) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("Lacks of mandatory info or unauthorized text level").type("text/plain").build());
        }
        var texts = Json.parse();
        text.setId(texts.size() + 1);
        text.setContent(StringEscapeUtils.escapeHtml4(text.getContent()));

        texts.add(text);
        Json.serialize(texts);
        return text;
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Text deleteText(@PathParam("id") int id) {
        if (id == 0) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("Lacks of mandatory id info").type("text/plain").build());
        }

        var texts = Json.parse();
        Text textFound = texts.stream().filter(t -> t.getId() == id).findAny().orElse(null);
        if (textFound == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Ressource not found").type("text/plain").build());
        }

        texts.remove(textFound);
        Json.serialize(texts);
        return textFound;
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Text updateOne(Text text, @PathParam("id") int id) {

        if (text == null || text.getContent() == null || text.getContent().isBlank() || text.getLevel() == null) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("Lacks of mandatory info or unauthorized text level").type("text/plain").build());
        }

        var texts = Json.parse();
        Text textFound = texts.stream().filter(t -> t.getId() == id).findAny().orElse(null);
        if (textFound == null) {throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Ressource not found").type("text/plain").build());
        }

        text.setId(texts.size() + 1);
        text.setContent(StringEscapeUtils.escapeHtml4(text.getContent()));
        texts.remove(text);
        texts.add(text);
        Json.serialize(texts);
        return text;
    }
}
