package swyp.team5.greening.image.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class NCPImageUploadClient {

    private final S3Client s3Client;

    @Value("${ncp.storage.bucket}")
    private String BUCKET;

    @Value("${ncp.storage.base-url}")
    private String BASE_URL;

    public List<String> uploadImages(List<MultipartFile> images) {

        List<String> result = new ArrayList<>();

        for (MultipartFile image : images) {
            String imageUrl = uploadImage(image);
            result.add(BASE_URL + "/" + imageUrl);
        }

        return result;
    }

    private String uploadImage(MultipartFile image) {

        if (Objects.isNull(image)) {
            return "";
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(image.getOriginalFilename())
                .contentType(image.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        try {
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(image.getInputStream(), image.getSize())
            );
        } catch (Exception e) {
            log.error("{} 이미지 업로드 실패", image.getOriginalFilename());
            log.error(e.getMessage());
        }

        return image.getOriginalFilename();
    }


}
