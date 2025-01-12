package com.nihiluis.filestore.resource;

import com.nihiluis.filestore.minio.MinioService;
import com.nihiluis.filestore.db.RemoteStoredFile;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.UUID;

@Path("/api/v1/file")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RemoteStoredFileResource {
    @Inject
    private MinioService minioService;

    @GET
    @Path("/download/{objectName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getPresignedUrl(@PathParam("objectName") String objectName) {
        try {
            String url = minioService.getPresignedObjectUrl(objectName);
            return Response.ok(url).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to generate download URL: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response uploadFile(FileUpload fileUpload) throws Exception {
        var storedFilename = UUID.randomUUID().toString();
        
        // Store file in Minio
        minioService.putObject(storedFilename, fileUpload.uploadedFile().toFile());
        
        // Create database record
        var file = RemoteStoredFile.create(
            fileUpload.fileName(),
            storedFilename,
            fileUpload.contentType(),
            fileUpload.size(),
            minioService.getBucketName(),
            "/"
        );
        
        return Response.ok(file.storedFilename).build();
    }
}
