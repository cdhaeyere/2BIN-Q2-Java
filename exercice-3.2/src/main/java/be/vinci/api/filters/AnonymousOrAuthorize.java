package be.vinci.api.filters;

import jakarta.ws.rs.NameBinding;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NameBinding
@Retention(RUNTIME)
public @interface AnonymousOrAuthorize {

}
