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

import java.util.List;

import static study.project.backend.global.common.Result.NOT_FOUND_PAPER;
import static study.project.backend.global.common.Result.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaperService {

    private final UserRepository userRepository;
    private final PaperRepository paperRepository;

    // 롤링페이퍼 만들기 API
    @Transactional
    public PaperResponse.Create createRollingPaper(PaperServiceRequest.Create request, Long userId) {
        Users user = getUser(userId);
        Paper paper = paperRepository.save(toPaperEntity(request, user));

        return PaperResponse.Create.response(paper);
    }

    // 롤링페이퍼 선물하기 API
    @Transactional
    public PaperResponse.Create giftRollingPaper(Long paperId, Long giftedUserId, Long userId) {
        Paper paper = getMyPaperWithOne(paperId, userId);

        Users giftedUser = getUser(giftedUserId);
        paper.toGiftRollingPaper(giftedUser);

        return PaperResponse.Create.response(paper);
    }

    // 롤링페이퍼 조회 API
    public PaperResponse.Read readRollingPaper(Long paperId) {
        Paper paper = paperRepository.findByPaperWithFetchJoin(paperId).orElseThrow(
                () -> new CustomException(NOT_FOUND_PAPER)
        );

        return PaperResponse.Read.response(paper);
    }

    // 내 롤링페이퍼 조회 API
    public List<PaperResponse.SimpleRead> readMyRollingPaper(Long userId) {
        List<Paper> papers = paperRepository.findByMyPaperWithFetchJoin(userId);

        return papers.stream()
                .map(PaperResponse.SimpleRead::response)
                .toList();
    }

    // method
    private Paper getPaper(Long paperId) {
        return paperRepository.findById(paperId).orElseThrow(
                () -> new CustomException(NOT_FOUND_PAPER)
        );
    }

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
