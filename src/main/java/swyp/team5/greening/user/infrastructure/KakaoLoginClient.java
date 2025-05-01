package swyp.team5.greening.user.infrastructure;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import swyp.team5.greening.user.util.BodyConverter;

@Component
public class KakaoLoginClient {

    @Value(value = "${oauth.kakao.token_base_url}")
    private String GENERATE_TOKEN_BASE_URL;

    @Value(value = "${oauth.kakao.user_info_base_url}")
    private String GET_USER_INFO_BASE_URL;

    @Value(value = "${oauth.kakao.security.client_id}")
    private String CLIENT_ID;

    @Value(value = "${oauth.kakao.security.grant_type}")
    private String GRANT_TYPE;

    @Value(value = "${oauth.kakao.security.redirect_uri}")
    private String REDIRECT_URI;

    //인가 코드를 통해 토큰 발급
    public Map<String, Object> generateToken(String code) {
        Map<String, String> formData = new HashMap<>();
        formData.put("code", code);
        formData.put("client_id", CLIENT_ID);
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
                .post()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .body(Map.class);

    }

}
