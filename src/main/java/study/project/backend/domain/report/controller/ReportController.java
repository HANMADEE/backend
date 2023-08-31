package study.project.backend.domain.report.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.project.backend.domain.report.request.ReportRequest;
import study.project.backend.domain.report.service.ReportService;
import study.project.backend.global.common.CustomResponseEntity;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/report")
    public CustomResponseEntity<Void> reportUser(
            @RequestBody @Valid ReportRequest.Report request,
            @AuthenticationPrincipal Long userId
    ) {
        return CustomResponseEntity.success(reportService.reportUser(request.toServiceRequest(), userId));
    }
}
