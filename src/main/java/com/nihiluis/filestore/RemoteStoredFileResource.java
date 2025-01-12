package com.nihiluis.filestore;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Path("/file")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RemoteStoredFileResource {
    @Inject
    private MinioService minioService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response uploadFile(FileUpload fileUpload) throws Exception {
        // Generate a unique filename
        String storedFilename = java.util.UUID.randomUUID().toString();
        
        // Store file in Minio
        minioService.putObject(storedFilename, fileUpload.uploadedFile().toFile());
        
        // Create database record
        RemoteStoredFile file = RemoteStoredFile.create(
            fileUpload.fileName(),
            storedFilename,
            fileUpload.contentType(),
            fileUpload.size(),
            minioService.getBucketName(),
            "/"
        );
        
        return Response.ok(file).build();
    }
}
