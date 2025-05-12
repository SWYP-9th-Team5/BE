package swyp.team5.greening.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import swyp.team5.greening.common.dto.response.ApiResponseDto;
import swyp.team5.greening.common.resolver.LogIn;
import swyp.team5.greening.image.infrastructure.NCPImageUploadClient;

@Tag(name = "이미지 업로드")
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageUploadController {

    private final NCPImageUploadClient imageUploadClient;

    @Operation(summary = "이미지 업로드 API")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<List<String>> uploadImages(
            @LogIn Long userId,
            @RequestPart(value = "image", required = false) List<MultipartFile> images
    ) {
        return ApiResponseDto.of(imageUploadClient.uploadImages(images));
    }

}
