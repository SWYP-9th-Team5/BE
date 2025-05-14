package swyp.team5.greening.mbtiQnA.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.mbtiQnA.domain.entity.MbtiQuestion;
import swyp.team5.greening.mbtiQnA.domain.repository.MbtiQuestionRepository;
import swyp.team5.greening.mbtiQnA.dto.response.FindMbtiQuestionResponseDto;

@Service
@RequiredArgsConstructor
public class MbtiQnAQueryService {

    private final MbtiQuestionRepository mbtiQuestionRepository;

    @Transactional(readOnly = true)
    public List<FindMbtiQuestionResponseDto> findMbtiQnA() {
        List<MbtiQuestion> mbtiQuestions = mbtiQuestionRepository.findAllByOrderBySequenceAsc();

        return mbtiQuestions.stream()
                .map(FindMbtiQuestionResponseDto::of)
                .toList();
    }
}
