package com.example.auctionapp.accessors;

import com.amazonaws.services.s3.AmazonS3;
import com.example.auctionapp.config.S3Config;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.UUID;

@Component
public class S3Accessor {
    @Autowired
    private S3Config s3Config;

    @Autowired
    private AmazonS3 amazonS3;

    private final String TEMP_STORAGE_DIRECTORY = "/tmp/images";
    private final String S3_STORAGE_URL_FORMAT  = "https://{0}.s3-{1}.amazonaws.com/{2}";


    // TODO: Verify if file is actually an image
    public String putS3Object(@NonNull final MultipartFile requestFile,
                              @NonNull final String directoryName) throws IOException {
        final Path completeDirectory = Paths.get(TEMP_STORAGE_DIRECTORY, directoryName);
        checkIfDirectoryExistsElseCreate(completeDirectory.toString());

        final Path filePath = Paths.get(completeDirectory.toString(), requestFile.getOriginalFilename());
        final File file = multipartFileToFile(requestFile, filePath);

        final String s3Key = directoryName + File.separator + requestFile.getOriginalFilename() + '-' + UUID.randomUUID();
        amazonS3.putObject(s3Config.getBucketName(),
                s3Key,
                file);
        return constructS3UploadedUrl(s3Key);
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
            dir.mkdirs();
        }
    }

    private String constructS3UploadedUrl(final String key) {
        return MessageFormat.format(S3_STORAGE_URL_FORMAT,
                s3Config.getBucketName(),
                s3Config.getBucketRegion(),
                key);
    }
}
