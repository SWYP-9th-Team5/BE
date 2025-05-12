package swyp.team5.greening.image.config;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class NCPStorageConfig {

    @Value("${ncp.storage.region}")
    private String REGION;

    @Value("${ncp.storage.end-point}")
    private String END_POINT;

    @Value("${ncp.storage.access-key}")
    private String ACCESS_KEY;

    @Value("${ncp.storage.secret-key}")
    private String SECRET_KEY;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(END_POINT))
                .region(Region.of(REGION))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)
                ))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true) // NCP는 경로 방식 필요
                        .build())
                .build();
    }
}
