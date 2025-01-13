package com.nihiluis.filestore.resource;

import com.nihiluis.filestore.dto.ErrorResponse;
import com.nihiluis.filestore.dto.FileUploadResponse;
import com.nihiluis.filestore.dto.PresignedUrlResponse;
import com.nihiluis.filestore.minio.MinioService;
import com.nihiluis.filestore.db.RemoteStoredFile;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.UUID;

@Path("/api/v1/file")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
public class RemoteStoredFileResource {
    @Inject
    private MinioService minioService;

    @GET
    @Path("/download/{objectName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPresignedUrl(@PathParam("objectName") String objectName) {
        try {
            String url = minioService.getPresignedObjectUrl(objectName);
            return Response.ok(new PresignedUrlResponse(url, objectName)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Failed to generate download URL: " + e.getMessage(), 
                            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))
                    .build();
        }
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response uploadFile(@RestForm("file") FileUpload fileUpload) throws Exception {
        if (fileUpload == null || fileUpload.contentType() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("File upload or content type is missing", 
                            Response.Status.BAD_REQUEST.getStatusCode()))
                    .build();
        }

        if (fileUpload.size() > 10 * 1024 * 1024) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("File size is too large", 
                            Response.Status.BAD_REQUEST.getStatusCode()))
                    .build();
        }

        var storedFilename = UUID.randomUUID().toString();
        
        minioService.putObject(storedFilename, fileUpload.uploadedFile().toFile(), fileUpload.contentType());
        
        // Create database record
        var file = RemoteStoredFile.create(
            fileUpload.fileName(),
            storedFilename,
            fileUpload.contentType(),
            fileUpload.size(),
            minioService.getBucketName(),
            "/"
        );
        
        return Response.ok(new FileUploadResponse(
            file.storedFilename,
            file.originalFilename,
            file.contentType,
            file.fileSize
        )).build();
    }
}
