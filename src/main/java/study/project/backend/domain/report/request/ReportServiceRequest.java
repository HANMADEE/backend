package study.project.backend.domain.report.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReportServiceRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Report {
        private Long reportedUserId;
        private Long commentId;
        private String reportedContent;
    }

}
