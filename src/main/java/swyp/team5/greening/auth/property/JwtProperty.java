package swyp.team5.greening.auth.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {

    private String clientSecret;
    private long accessExpiryTime;

}
