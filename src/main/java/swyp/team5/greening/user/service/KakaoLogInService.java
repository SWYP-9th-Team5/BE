package swyp.team5.greening.user.service;

import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swyp.team5.greening.auth.provider.TokenProvider;
import swyp.team5.greening.user.domain.entity.LoginType;
import swyp.team5.greening.user.domain.entity.User;
import swyp.team5.greening.user.domain.repository.UserRepository;
import swyp.team5.greening.user.dto.response.LoginResponseDto;
import swyp.team5.greening.user.infrastructure.KakaoLoginClient;

@Service
@RequiredArgsConstructor
public class KakaoLogInService {

    private final TokenProvider tokenProvider;
    private final KakaoLoginClient kakaoLoginClient;
    private final UserRepository userRepository;

    public LoginResponseDto kakaoLogin(String redirectURI, String code) {

        //인가 코드를 통해 카카오 서버 접근 액세스 코드 발급
        Map<String, Object> mapWithToken = kakaoLoginClient.generateToken(redirectURI, code);
        String token = (String) mapWithToken.get("access_token");

        //액세스 코드를 통해 사용자 정보 조회
        Map<String, Object> userInfo = kakaoLoginClient.getUserInfo(token);

        Map<String, String> properties = (Map) userInfo.get("properties");
        String nickname = properties.get("nickname");

        Map<String, String> kakaoAccount = (Map) userInfo.get("kakao_account");
        String email = kakaoAccount.get("email");

        //유저 조회
        User loginUser = userRepository.findByEmail(email)
                .orElse(User.builder()
                        .email(email)
                        .loginType(LoginType.KAKAO)
                        .userName(nickname)
                        .nickname(nickname)
                        .build());

        boolean newJoin = false;

        //처음 로그인 한 회원이라면, 회원가입
        if (Objects.isNull(loginUser.getId())) {
            userRepository.save(loginUser);
            newJoin = true;
        }

        //사용자 정보를 통해 액세스 토큰 발급
        String accessToken = tokenProvider.createToken(loginUser.getId());
        return new LoginResponseDto(accessToken, newJoin);
    }
}
