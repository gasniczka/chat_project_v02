package org.jk.chat.client.rest;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;


@ApplicationScoped
public class RestMultipartSendService {

    @Inject
    @RestClient
    MultipartService service;


    public String sendFile(String fileName, byte[] fileContent) throws Exception {

        MultipartBody body = new MultipartBody();
        body.fileName = fileName;
        body.file = new ByteArrayInputStream(fileContent);
        return service.sendMultipartData(body);
    }


    public MultipartBody downloadFile(String fileName) throws Exception {

        MultipartBody body = new MultipartBody();
        body.fileName = fileName;

        return service.receiveMultipartData(body);
    }

}