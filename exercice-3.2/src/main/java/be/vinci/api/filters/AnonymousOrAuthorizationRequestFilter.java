package be.vinci.api.filters;

import be.vinci.domain.User;
import be.vinci.services.UserDataService;
import be.vinci.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;


/**
 * @author e-Baron This filter allows anonymous requests
 */

@Singleton
@Provider
@AnonymousOrAuthorize
public class AnonymousOrAuthorizationRequestFilter implements ContainerRequestFilter {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0")
      .build();
  @Inject
  private UserDataService myUserDataService;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) // anonymous request
    {
      return;
    }

    DecodedJWT decodedToken = null;
    try {
      decodedToken = this.jwtVerifier.verify(token);
    } catch (Exception e) {
      throw new WebApplicationException("Malformed token : " + e.getMessage(), Status.UNAUTHORIZED);
    }
    User authenticatedUser = myUserDataService.getOne(decodedToken.getClaim("user").asInt());
    if (authenticatedUser == null) {
      throw new WebApplicationException("You are forbidden to access this resource",
          Status.FORBIDDEN);
    }

    requestContext.setProperty("user", authenticatedUser);

  }

}
