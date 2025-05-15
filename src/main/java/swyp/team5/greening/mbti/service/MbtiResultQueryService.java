package swyp.team5.greening.mbti.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.mbti.domain.entity.Mbti;
import swyp.team5.greening.mbti.domain.entity.MbtiPlant;
import swyp.team5.greening.mbti.domain.entity.MbtiType;
import swyp.team5.greening.mbti.domain.entity.PlantImage;
import swyp.team5.greening.mbti.domain.repository.MbtiRepository;
import swyp.team5.greening.mbti.dto.response.FindMbtiResultResponseDto;

@Service
@RequiredArgsConstructor
public class MbtiResultQueryService {

    private final MbtiRepository mbtiRepository;

    @Transactional(readOnly = true)
    public FindMbtiResultResponseDto findMbtiResult(String mbti) {
        MbtiType mbtiType = MbtiType.of(mbti);

        Mbti resultMbti = mbtiRepository.findByMbtiType(mbtiType)
                .orElseThrow();
        MbtiPlant resultPlant = resultMbti.getPlant();
        PlantImage plantImage = resultPlant.getPlantImages().get(0);

        return new FindMbtiResultResponseDto(
                resultMbti.getId(),
                resultPlant.getPlantName(),
                resultPlant.getPlantDescription(),
                resultPlant.getPlantPersonality(),
                resultPlant.getSuitablePlant(),
                resultPlant.getUnsuitablePlant(),
                resultPlant.getRecommendedPlant(),
                plantImage.getPlantImageUrl()
                );
    }

}
