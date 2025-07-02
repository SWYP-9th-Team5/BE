package swyp.team5.greening.image.infrastructure;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3ImageUploadClient {

    @Value("${s3.storage.bucket}")
    private String BUCKET;

    @Value("${s3.storage.base-url}")
    private String BASE_URL;

    private final AmazonS3Client amazonS3Client;

    public List<String> uploadImages(List<MultipartFile> images) {

        List<String> result = new ArrayList<>();

        for (MultipartFile image : images) {
            String uploadedImageUrl = uploadImage(image);
            result.add(BASE_URL + uploadedImageUrl);
        }

        return result;
    }

    private String uploadImage(MultipartFile image) {
        try {
            String uploadImageFileName = makeUploadImageFileName(image.getOriginalFilename());

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());
            amazonS3Client.putObject(BUCKET, uploadImageFileName,
                    image.getInputStream(), metadata);

            return uploadImageFileName;
        } catch (IOException e) {
            return null;
        }
    }

    private String makeUploadImageFileName(String originalFilename) {

        int pos = originalFilename.lastIndexOf(".");

        String extension = originalFilename.substring(pos + 1);

        return UUID.randomUUID().toString() + "." + extension;
    }

}
