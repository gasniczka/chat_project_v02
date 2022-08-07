package org.jk.chat.client.io;

import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Log
public final class FileUtils {

    private static final String CLIENT_FILES_DIRECTORY = "/chatClientFiles/";


    public static void saveFile(String clientId, File file, byte[] fileContent) {

        String fileName = CLIENT_FILES_DIRECTORY + clientId + "_" + file.getName();

        try {
            Files.write(new File(fileName).toPath(), fileContent);
        } catch (IOException e) {
            System.err.println("error: can not save file " + fileName + "error: " + e);
        }

        System.out.println("saved file: " + file.getName() + " as: " + fileName);
    }


    public static File getFileByName(String fileName) {

        return new File(CLIENT_FILES_DIRECTORY + fileName);
    }


    public static byte[] getFileContent(File file, String fileName) {

        byte[] fileContent = null;

        if (!file.exists() || !file.isFile()) {
            log.warning("plik nie istnieje: " + file.getAbsolutePath());
        } else {

            try {
                fileContent = Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                log.severe("could not read file: " + fileName + "error: " + e);
            }
        }

        return fileContent;
    }


}
