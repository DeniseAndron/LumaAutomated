package com.luma.framework;


import com.luma.framework.PropertyManager;
import com.luma.framework.ThreadPackage;
import com.luma.utils.ElementReadyStatus;

import io.qameta.allure.Step;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.io.FileUtils.forceDelete;

public class FileProcessor {
    private PropertyManager propertyManager;
    protected ElementReadyStatus elementReadyStatus;
    private int timeOutInSeconds = 899;// 1 Second less to 15 Minute
    private int recursion;

    public FileProcessor(PropertyManager pm) {

        this.propertyManager = pm;

        this.timeOutInSeconds = (pm.hasProperty("file_download_timeout"))
                ? Integer.parseInt(pm.getProperty("file_download_timeout"))
                : timeOutInSeconds;
        elementReadyStatus = new ElementReadyStatus();
    }

    public static String getDownloadDir() {
        Path filePath = Paths.get(System.getProperty("user.dir"),"download");
        return filePath.toString();
    }

    public static String getFilePath(String folder,String fileName) {
        Path filePath = Paths.get(folder,fileName);
        return filePath.toString();
    }

    public static String getFileFromResources(String fileName) {
        Path filePath = Paths.get(System.getProperty("user.dir"),"src","test","resources",fileName);
        return filePath.toString();
    }

    public static String getFileFromResources(String subFolder,String fileName) {
        Path filePath = Paths.get(System.getProperty("user.dir"),"src","test","resources",subFolder,fileName);
        return filePath.toString();
    }

    @Step("Is file downloaded {expectedFileName}")
    public boolean isFileDownloaded(String expectedFileName) {
        Log.info("Is file downloaded " + expectedFileName);
// Array to Store List of Files in Directory
        File[] listOfFiles;

// Store File Name
        String fileName;

// Consider file is not downloaded
        boolean fileDownloaded = false;

// capture time before looking for files in directory
        // last modified time of previous files will always less than start time
        // this is basically to ignore previous downloaded files
        long startTime = Instant.now().toEpochMilli();

// Time to wait for download to finish
        long waitTime = startTime + (this.timeOutInSeconds * 1000);

// while current time is less than wait time
        while (Instant.now().toEpochMilli() < waitTime) {
            // get all the files of the folder
            listOfFiles = new File(getDownloadDir()).listFiles();
// iterate through each file
            for (File file : listOfFiles) {
                // get the name of the current file
                fileName = file.getName().toLowerCase();
// condition 1 - till the time file is completely downloaded extension will be
                // crdownload
                // Condition 2 - Current File name contains expected Text
                if (!fileName.contains("crdownload") && fileName.contains(expectedFileName.toLowerCase())) {
                    // File Found
                    fileDownloaded = true;
                    break;
                }
            }
            // File Found Break While Loop
            if (fileDownloaded) {
                break;
            }
            elementReadyStatus.pause(250);// 1/4 of second
        }
        // File Not Found
        return fileDownloaded;
    }

    public void removeFile() {
        // TO DO
        File directory = new File(getDownloadDir());
        File[] files = directory.listFiles();

        for (File f : files) {
            if (f.getName().contains("$")) {
                f.delete();
            }
        }
    }

    public static void cleanDirectory() throws IOException {
        File dir = new File(getDownloadDir());
        if (dir.listFiles() != null) {
            for (File file : Objects.requireNonNull(dir.listFiles()))
                if (!file.isDirectory()) {
                    System.gc();
                    forceDelete(file);
                }
        }
    }

    public static void cleanDirectory(String location) throws IOException {
        File dir = new File(location);
        for (File file : Objects.requireNonNull(dir.listFiles()))
            if (!file.isDirectory()) {
                System.gc();
                forceDelete(file);
            }
    }

    public String hasFileDownloaded(String fileName) {
        String downloadedFileName = null;
        boolean valid = true;
        boolean found = false;

        try {
            Path downloadFolderPath = Paths.get(getDownloadDir());
            WatchService watchService = FileSystems.getDefault().newWatchService();
            downloadFolderPath.register(watchService,StandardWatchEventKinds.ENTRY_CREATE);
            long startTime = System.currentTimeMillis();
            do {
                WatchKey watchKey;
                watchKey = watchService.poll(this.timeOutInSeconds,TimeUnit.SECONDS);
                long currentTime = (System.currentTimeMillis() - startTime) / 1000;
                if (currentTime > this.timeOutInSeconds) {
                    Log.debug("Download operation timed out.. Expected file was not downloaded");
                    return downloadedFileName;
                }

                for (WatchEvent event : watchKey.pollEvents()) {
                    WatchEvent.Kind kind = event.kind();
                    if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {

                        if (!fileName.contains("crdownload")) {
                            downloadedFileName = fileName;
                            Log.debug("Downloaded file found. File name is " + fileName);
                            Thread.sleep(500);
                            found = true;
                            break;
                        }
                    }
                }
                if (found) {
                    return downloadedFileName;
                } else {
                    currentTime = (System.currentTimeMillis() - startTime) / 1000;
                    if (currentTime > this.timeOutInSeconds) {
                        Log.debug("Failed to download expected file");
                        return downloadedFileName;
                    }
                    valid = watchKey.reset();
                }
            } while (valid);
        }

        catch (InterruptedException e) {
            Log.debug("Interrupted error - " + e.getMessage());
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.debug("Download operation timed out.. Expected file was not downloaded");
        } catch (Exception e) {
            Log.debug("Error occured - " + e.getMessage());
            e.printStackTrace();
        }
        return downloadedFileName;
    }

    public static void createDirectory(String directoryPath) throws Exception {
        Files.createDirectories(Paths.get(directoryPath));
    }

    public static XSSFWorkbook getExcelWorkbook(String fileName) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            return new XSSFWorkbook(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeWorkbook(XSSFWorkbook wb) {
        try {
            wb.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isDownloadReady(WebElement firstLinkOnPage,By downloadErrorMsg) {
        if (this.recursion < 10) {
            if (elementReadyStatus.isTextPresent(firstLinkOnPage,"Download")) {
                this.recursion = 0;
                return true;
            } else { // Not present in 30 sec.
                try {
                    long waitTime = Instant.now().toEpochMilli() + (this.timeOutInSeconds * 1000);// millisecond
                    while (Instant.now().toEpochMilli() < waitTime) {
                        elementReadyStatus.refresh();
                        if (elementReadyStatus.isElementExists(downloadErrorMsg)) { // Server error
                            Log.debug("Error occurred while downloading.");
                            this.recursion = 0;
                            return false;
                        } else {
                            if (elementReadyStatus.isTextPresent(firstLinkOnPage,"Download")) {
                                this.recursion = 0;
                                return true;
                            }
                        }
                        elementReadyStatus.pause(1000);// 1/4 of second
                    }
                } catch (org.openqa.selenium.NoSuchSessionException e) {
                    this.recursion++;
                    elementReadyStatus.pause(1000);
                    ThreadPackage.getInstance().getThreadDriver();
                    isDownloadReady(firstLinkOnPage,downloadErrorMsg);

                }
            }
            return false;
        }
        this.recursion = 0;
        return false;
    }
}