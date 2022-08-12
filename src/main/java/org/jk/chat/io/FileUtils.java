package org.jk.chat.io;

import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.jk.chat.common.Configuration.JOINING_DELIMITER;


@Log
public final class FileUtils {

    private static final String SERVER_FILES_DIRECTORY = "/chatServerFiles/";

    public static String getFilesToDownload() {

        String filesToDownload = "";

        try {
            filesToDownload = Files.list(Paths.get(SERVER_FILES_DIRECTORY))
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.joining(JOINING_DELIMITER));

        } catch (IOException e) {
            log.severe("error fetching file names to download " + e);
            // throw new RuntimeException("error fetching file names", e);
        }
        return filesToDownload;
    }


    public static File getFileByName(String fileName) {

        return new File(SERVER_FILES_DIRECTORY + fileName);
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


    public static void saveFile(File file, byte[] fileContent) {

        String fileName = SERVER_FILES_DIRECTORY + file.getName();
        log.info("fileName: " + fileName);
        try {
            Files.write(new File(fileName).toPath(), fileContent);
        } catch (IOException e) {
            log.severe("can not save file: " + fileName + " error: " + e);
        }
    }


}
