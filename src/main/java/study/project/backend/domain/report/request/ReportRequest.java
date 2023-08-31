package study.project.backend.domain.report.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReportRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Report {
        @NotNull(message = "신고 유저 ID는 필수 값 입니다.")
        private Long reportedUserId;

        @NotNull(message = "신고 할 한마디 ID는 필수 값 입니다.")
        private Long commentId;

        @NotNull(message = "신고 내용은 필수 값 입니다.")
        private String reportedContent;

        public ReportServiceRequest.Report toServiceRequest() {
            return ReportServiceRequest.Report.builder()
                    .reportedUserId(reportedUserId)
                    .commentId(commentId)
                    .reportedContent(reportedContent)
                    .build();
        }
    }
}
