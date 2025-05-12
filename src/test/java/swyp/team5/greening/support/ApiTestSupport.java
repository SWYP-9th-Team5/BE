package swyp.team5.greening.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import swyp.team5.greening.auth.provider.TokenProvider;
import swyp.team5.greening.user.domain.entity.LoginType;
import swyp.team5.greening.user.domain.entity.User;
import swyp.team5.greening.user.domain.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ApiTestSupport extends TestContainerSupport{

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected MockMvc mockMvc;

    protected User loginUser;
    protected String accessToken;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @PostConstruct
    public void setUpUser() {
        if (loginUser != null) {
            return;
        }

        User user = userRepository.save(User.builder()
                .email("test@gmail.com")
                .loginType(LoginType.GOOGLE)
                .userName("테스트")
                .nickname("테스트 닉네임")
                .build());
        String token = tokenProvider.createToken(user.getId());

        loginUser = user;
        this.accessToken = token;
    }

    protected String toJson(Object object) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(object);
    }

}
