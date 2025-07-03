package swyp.team5.greening.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import swyp.team5.greening.common.dto.response.ApiResponseDto;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.common.resolver.LogIn;
import swyp.team5.greening.user.domain.entity.User;
import swyp.team5.greening.user.domain.repository.UserRepository;
import swyp.team5.greening.user.dto.response.MyInfoResponseDto;
import swyp.team5.greening.user.exception.UserExceptionMessage;

@Tag(name = "유저 정보 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @Operation(summary = "로그인 유저 이름 조회 API")
    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<MyInfoResponseDto> getMyInfo(
            @LogIn Long userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new GreeningGlobalException(UserExceptionMessage.NOT_FOUND_USER));

        return ApiResponseDto.of(new MyInfoResponseDto(user.getId(),user.getUserName()));
    }

}
