package study.project.backend.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.backend.domain.report.entity.Report;
import study.project.backend.domain.report.repository.ReportRepository;
import study.project.backend.domain.report.request.ReportServiceRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;

    @Transactional
    public Void reportUser(ReportServiceRequest.Report request, Long userId) {
        reportRepository.save(
                Report.builder()
                        .userId(userId)
                        .reportedUserId(request.getReportedUserId())
                        .commentId(request.getCommentId())
                        .reportedContent(request.getReportedContent())
                        .build()
        );
        return null;
    }
}
