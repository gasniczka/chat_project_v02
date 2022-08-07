package org.jk.chat.rest;

import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jk.chat.io.FileUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.File;


@Log
@Path("/files")
public class FileController {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/upload")
    public String upload(@MultipartForm MultipartBody data) {

        System.out.println(" -> upload ");
        if (data == null || StringUtils.isBlank(data.fileName)) {

            System.out.println(" <- upload, pusty request ");
            return "no file attached";
        }

        File file = new File(data.fileName);

        ByteArrayInputStream fileInputStream = (ByteArrayInputStream) data.file;

        byte[] fileContent = fileInputStream.readAllBytes();

        FileUtils.saveFile(file, fileContent);

        System.out.println(" <- upload, plik: " + data.fileName);
        return "File saved: " + data.fileName;
    }


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @Path("/download")
    public @MultipartForm MultipartBody download(@MultipartForm MultipartBody data) {

        System.out.println(" -> download ");

        if (data == null || StringUtils.isBlank(data.fileName)) {

            System.out.println(" <- download, pusty request ");

            return new MultipartBody();
        }


        File file = FileUtils.getFileByName(data.fileName);
        byte[] fileContent = FileUtils.getFileContent(file, data.fileName);

        MultipartBody body = new MultipartBody();

        body.fileName = data.fileName;
        body.file = new ByteArrayInputStream(fileContent);

        System.out.println(" <- download, plik: " + data.fileName);

        return body;
    }

}