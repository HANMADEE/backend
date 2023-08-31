package study.project.backend.domain.paper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.backend.domain.paper.Repository.PaperLikeRepository;
import study.project.backend.domain.paper.Repository.PaperRepository;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.paper.entity.PaperLike;
import study.project.backend.domain.paper.entity.PaperSort;
import study.project.backend.domain.paper.request.PaperServiceRequest;
import study.project.backend.domain.paper.response.PaperResponse;
import study.project.backend.domain.user.entity.Users;
import study.project.backend.domain.user.repository.UserRepository;
import study.project.backend.global.common.Result;
import study.project.backend.global.common.exception.CustomException;

import java.util.List;
import java.util.Optional;

import static study.project.backend.global.common.Result.*;
import static study.project.backend.global.common.Result.NOT_FOUND_PAPER;
import static study.project.backend.global.common.Result.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaperService {

    private final UserRepository userRepository;
    private final PaperRepository paperRepository;
    private final PaperLikeRepository paperLikeRepository;

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

    // 롤링페이퍼 수정 API
    @Transactional
    public Void updateRollingPaper(PaperServiceRequest.Update request, Long userId) {
        Paper paper = getPaper(request.getPaperId());
        validateMyPaper(userId, paper);

        paper.toUpdateRollingPaper(
                request.getSubject(), request.getTheme(),
                request.getIsOpen(), request.getIsLikeOpen()
        );

        return null;
    }

    // 롤링페이퍼 삭제 API
    @Transactional
    public Void deleteRollingPaper(Long paperId, Long userId) {
        Paper paper = getPaper(paperId);
        validateMyPaper(userId, paper);

        paperRepository.delete(paper);
        return null;
    }

    // 롤링페이퍼 정렬 전체 조회 API
    public List<PaperResponse.ALL> readAllRollingPaper(PaperSort sort) {
        List<Paper> papers = paperRepository.findByAllPaperSortWithFetchJoin(sort);
        return papers.stream().map(PaperResponse.ALL::response).toList();
    }

    // 롤링페이퍼 좋아요 및 취소 토글 API
    @Transactional
    public PaperResponse.ToggleLike toggleLike(Long paperId, Long userId) {
        Users user = getUser(userId);
        Paper paper = getPaper(paperId);
        Optional<PaperLike> optionalPaperLike = paperLikeRepository.findByPaperAndUser(paper, user);

        boolean isAdded;
        // 이미 좋아요를 해놓은 상태면 취소
        if (optionalPaperLike.isPresent()) {
            paperLikeRepository.delete(optionalPaperLike.get());
            isAdded = false;
        } else {
            // 처음 좋아요를 하는 상태면 등록
            paperLikeRepository.save(PaperLike.builder()
                    .user(user)
                    .paper(paper)
                    .build());
            isAdded = true;
        }

        return PaperResponse.ToggleLike.response(isAdded);
    }

    // method

    private static void validateMyPaper(Long userId, Paper paper) {
        if (!paper.getUser().getId().equals(userId)) {
            throw new CustomException(NOT_MY_PAPER);
        }
    }

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
