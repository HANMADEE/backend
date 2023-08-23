package study.project.backend.domain.paper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.backend.domain.paper.Repository.PaperRepository;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.paper.request.PaperServiceRequest;
import study.project.backend.domain.paper.response.PaperResponse;
import study.project.backend.domain.user.entity.Users;
import study.project.backend.domain.user.repository.UserRepository;
import study.project.backend.global.common.exception.CustomException;

import static study.project.backend.global.common.Result.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaperService {

    private final UserRepository userRepository;
    private final PaperRepository paperRepository;

    @Transactional
    public PaperResponse.Create createRollingPaper(PaperServiceRequest.Create request, Long userId) {
        Users user = getUser(userId);
        Paper paper = paperRepository.save(toPaperEntity(request, user));

        return PaperResponse.Create.response(paper);
    }


    public PaperResponse.Create giftRollingPaper(Long paperId, Long giftedUserId, Long userId) {
        Paper paper = getMyPaperWithOne(paperId, userId);

        Users giftedUser = getUser(giftedUserId);
        paper.toGiftRollingPaper(giftedUser);

        return PaperResponse.Create.response(paper);
    }

    // method
    private Paper getMyPaperWithOne(Long paperId, Long userId) {
        return paperRepository.findByIdAndUserId(paperId, userId).orElseThrow(
                () -> new CustomException(NOT_FOUND_PAPER)
        );
    }

    private static Paper toPaperEntity(PaperServiceRequest.Create request, Users user) {
        return Paper.builder()
                .user(user)
                .subject(request.getSubject())
                // TODO : 기본이미지 추가되면 해당 URL 변경
                .theme(request.getTheme().orElse("default.png"))
                .isOpen(request.getIsOpen())
                .isLikeOpen(request.getIsLikeOpen())
                .build();
    }

    private Users getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER)
        );
    }
}
