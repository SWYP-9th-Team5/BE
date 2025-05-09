package swyp.team5.greening.user.infrastructure;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import swyp.team5.greening.user.util.BodyConverter;

@Component
public class GoogleLoginClient {

    @Value(value = "${oauth.google.token_base_url}")
    private String GENERATE_TOKEN_BASE_URL;

    @Value(value = "${oauth.google.user_info_base_url}")
    private String GET_USER_INFO_BASE_URL;

    @Value(value = "${oauth.google.security.client_id}")
    private String CLIENT_ID;

    @Value(value = "${oauth.google.security.client_secret}")
    private String CLIENT_SECRET;

    @Value(value = "${oauth.google.security.grant_type}")
    private String GRANT_TYPE;

    @Value(value = "${oauth.google.security.redirect_uri}")
    private String REDIRECT_URI;

    //인가 코드를 통해 토큰 발급
    public Map<String, Object> generateToken(String code) {
        //코드 디코딩
        String decodeCode = URLDecoder.decode(code, StandardCharsets.UTF_8);

        Map<String, String> formData = new HashMap<>();
        formData.put("code", decodeCode);
        formData.put("client_id", CLIENT_ID);
        formData.put("client_secret", CLIENT_SECRET);
        formData.put("grant_type", GRANT_TYPE);
        formData.put("redirect_uri", REDIRECT_URI);

        return RestClient.builder()
                .baseUrl(GENERATE_TOKEN_BASE_URL)
                .build()
                .post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyConverter.fromFormData(formData))
                .retrieve()
                .body(Map.class);
    }

    //토큰을 통해 사용자 정보 조회
    public Map<String, Object> getUserInfo(String accessToken) {

        return RestClient.builder()
                .baseUrl(GET_USER_INFO_BASE_URL)
                .build()
                .get()
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(Map.class);
    }

}
