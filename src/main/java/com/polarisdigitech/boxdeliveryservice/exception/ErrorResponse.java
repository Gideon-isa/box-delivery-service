package com.polarisdigitech.boxdeliveryservice.exception;

import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

public class ErrorResponse {

    // Private Method
    public static ResponseEntity<ProblemDetail> toErrorResponse(DomainError error) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(400);

        problemDetail.setTitle("Domain Error");
        problemDetail.setDetail(error.message());
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
