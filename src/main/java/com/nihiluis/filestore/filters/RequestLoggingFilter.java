package com.nihiluis.filestore.filters;

import io.vertx.core.http.HttpServerRequest;
import org.jboss.logging.Logger;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import jakarta.annotation.Priority;

@Provider
@Priority(1)
public class RequestLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOG = Logger.getLogger(RequestLoggingFilter.class);

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext context) {
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        var method = requestContext.getMethod();
        var path = requestContext.getUriInfo().getPath();
        var address = request.remoteAddress().toString();

        var status = responseContext.getStatus();

        LOG.infof("%d %s %s (%s)", status, method, path, address);
    }
} 