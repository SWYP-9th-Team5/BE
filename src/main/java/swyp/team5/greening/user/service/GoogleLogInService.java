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
import swyp.team5.greening.user.infrastructure.GoogleLoginClient;

@Service
@RequiredArgsConstructor
public class GoogleLogInService {

    private final TokenProvider tokenProvider;
    private final GoogleLoginClient googleLoginClient;
    private final UserRepository userRepository;

    public LoginResponseDto googleLogin(String code) {
        //인가 코드를 통해 카카오 서버 접근 액세스 코드 발급
        Map<String, Object> mapWithToken = googleLoginClient.generateToken(code);
        String token = (String) mapWithToken.get("access_token");

        //액세스 코드를 통해 사용자 정보 조회
        Map<String, Object> userInfo = googleLoginClient.getUserInfo(token);

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        //유저 조회
        User loginUser = userRepository.findByEmail(email)
                .orElse(User.builder()
                        .email(email)
                        .loginType(LoginType.KAKAO)
                        .userName(name)
                        .nickname(name)
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
