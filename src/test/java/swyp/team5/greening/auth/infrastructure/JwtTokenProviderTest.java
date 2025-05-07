package swyp.team5.greening.auth.infrastructure;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import swyp.team5.greening.auth.property.JwtProperty;

@DisplayName("Jwt 토큰 생성 및 파싱 테스트")
@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @Mock
    private JwtProperty jwtProperty;

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    private final String tempClientSecret = "aa583c37e87dc22de563838c6ab1175ba8f253c87dfdf51c13cd3a4d28999825";
    private final long tempAccessExpiryTime  = 3000L;

    private Long id;

    @Test
    void jwtTokenCreateAndParsingTest() {
        //given
        id = 253L;

        given(jwtProperty.getClientSecret()).willReturn(tempClientSecret);
        given(jwtProperty.getAccessExpiryTime()).willReturn(tempAccessExpiryTime);

        String accessToken = jwtTokenProvider.createToken(id);

        //when
        Long payLoad = jwtTokenProvider.getPayLoad(accessToken);

        //then
        assertThat(payLoad).isEqualTo(id);
    }

}