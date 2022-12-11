package com.example.auctionapp.accessors;

import com.amazonaws.services.s3.AmazonS3;
import com.example.auctionapp.config.S3Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class S3Accessor {
    @Autowired
    private S3Config s3Config;

    @Autowired
    private AmazonS3 amazonS3;

    private final String TEMP_STORAGE_DIRECTORY = "/tmp/images";


    // TODO: Verify if file is actually an image
    public void putS3Object(final MultipartFile requestFile, final String directoryName) throws IOException {
        final Path completeDirectory = Paths.get(TEMP_STORAGE_DIRECTORY, directoryName);
        checkIfDirectoryExistsElseCreate(completeDirectory.toString());

        final Path filePath = Paths.get(completeDirectory.toString(), requestFile.getOriginalFilename());
        final File file = multipartFileToFile(requestFile, filePath);

        amazonS3.putObject(s3Config.getBucketName(),
                directoryName + File.separator + requestFile.getOriginalFilename(),
                file);
    }

    private File multipartFileToFile(final MultipartFile multipart, final Path filePath) throws IOException {
        final File file = filePath.toFile();
        file.createNewFile();
        multipart.transferTo(filePath);
        return file;
    }

    private void checkIfDirectoryExistsElseCreate(final String directory) {
        final File dir = new File(directory);
        if (!dir.exists()) {
            System.out.println("Creating temporary directory: " + directory);
            dir.mkdir();
        }
    }
}
