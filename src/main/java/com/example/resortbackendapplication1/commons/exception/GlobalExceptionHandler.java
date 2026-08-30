package com.example.resortbackendapplication1.commons.exception;

import com.example.resortbackendapplication1.commons.dto.response.ApiErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.warn("Validation failed: {}", message);

        ApiErrorResponse response = new ApiErrorResponse(
                request.getHeader("X-Request-Id"),
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_ARGUMENT",
                message
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {

        String rootMessage = extractRootCauseMessage(ex);

        log.warn("Data integrity violation: {}", rootMessage);

        String userMessage = resolveConstraintMessage(rootMessage);

        ApiErrorResponse response = new ApiErrorResponse(
                request.getHeader("X-Request-Id"),
                HttpStatus.CONFLICT.value(),
                "DATA_INTEGRITY_VIOLATION",
                userMessage
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Postgres trigger-raised business-rule violations (e.g. {@code fn_validate_resort_room_category_price_unit
     * _scope} rejecting a price unit outside the ROOM_CATEGORY scope, or {@code fn_validate_resort_room_category
     * _price_days_required} rejecting a price row for a resort with no weekly schedule days yet) come back as
     * {@link JpaSystemException} when the trigger fires synchronously on insert/update, or wrapped in a
     * {@link TransactionSystemException} when it's a deferred constraint trigger firing at commit time — neither
     * is a {@link DataIntegrityViolationException}, so without this handler both fell through to
     * {@link #handleUnexpected}, surfacing as a raw {@code 500 INTERNAL_SERVER_ERROR} for what is really a
     * client-fixable {@code 409}.
     */
    @ExceptionHandler({JpaSystemException.class, TransactionSystemException.class})
    public ResponseEntity<@NonNull ApiErrorResponse> handleDatabaseRuleViolation(
            Exception ex,
            HttpServletRequest request
    ) {
        String rootMessage = extractRootCauseMessage(ex);

        log.warn("Database rule violation: {}", rootMessage);

        String userMessage = resolveDatabaseRuleMessage(rootMessage);

        ApiErrorResponse response = new ApiErrorResponse(
                request.getHeader("X-Request-Id"),
                HttpStatus.CONFLICT.value(),
                "DATABASE_RULE_VIOLATION",
                userMessage
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    private static final Pattern PRICE_UNIT_SCOPE_PATTERN =
            Pattern.compile("price_unit_id (\\d+) is not assigned to the (\\w+) price scope");

    private static final Pattern DUPLICATE_ROOM_CODE_PATTERN =
            Pattern.compile("code (\\S+) is already used by another room in resort (\\d+)");

    /**
     * Translates raw Postgres trigger {@code RAISE EXCEPTION} text (see {@link #handleDatabaseRuleViolation})
     * into a message a client can act on without reading PL/pgSQL source. Falls back to the raw root message
     * for any trigger text not recognized here — add a new pattern below rather than changing the trigger's
     * wording, since the wording is also read by developers/DBAs directly in Postgres logs.
     */
    private String resolveDatabaseRuleMessage(String rootMessage) {
        if (rootMessage == null) return "A database business rule was violated.";

        Matcher scopeMatcher = PRICE_UNIT_SCOPE_PATTERN.matcher(rootMessage);
        if (scopeMatcher.find()) {
            String priceUnitId = scopeMatcher.group(1);
            String scope = scopeMatcher.group(2);
            String scopeLabel = scope.replace('_', ' ').toLowerCase(Locale.ROOT);
            return "The selected price unit (id " + priceUnitId + ") can't be used for " + scopeLabel
                    + " prices — pick a price unit that's assigned to the " + scope + " price scope.";
        }

        if (rootMessage.contains("requires the resort to have at least one weekly schedule day")) {
            return "This resort has no weekly schedule set up yet, so weekday/weekend prices can't be validated "
                    + "— set one via PUT /resorts/{resort-id}/weekly-schedule first, then retry.";
        }

        Matcher codeMatcher = DUPLICATE_ROOM_CODE_PATTERN.matcher(rootMessage);
        if (codeMatcher.find()) {
            return "Room code '" + codeMatcher.group(1) + "' is already used by another room in this resort — choose a different code.";
        }

        return rootMessage;
    }

    /**
     * Thrown by Hibernate's {@code @Version} check when two requests write the same row concurrently (e.g. one
     * updates a resort room category price row while another deletes/replaces it at the same time) — without
     * this handler it fell through to {@link #handleUnexpected}, surfacing as a raw {@code 500
     * INTERNAL_SERVER_ERROR} instead of a client-actionable {@code 409}.
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleOptimisticLockingFailure(
            OptimisticLockingFailureException ex,
            HttpServletRequest request
    ) {
        log.warn("Optimistic locking failure: {}", ex.getMessage());

        ApiErrorResponse response = new ApiErrorResponse(
                request.getHeader("X-Request-Id"),
                HttpStatus.CONFLICT.value(),
                "CONCURRENT_MODIFICATION",
                "This record was modified or deleted by another request — please refresh and try again."
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    private String resolveConstraintMessage(String rootMessage) {
        if (rootMessage == null) return "A data integrity constraint was violated.";
        if (rootMessage.contains("uq_resort_facility_group_platform")) {
            return "This facility group is already assigned to the resort.";
        }
        if (rootMessage.contains("uq_resort_room_category_facility_group_platform")) {
            return "This facility group is already assigned to the resort room category.";
        }
        if (rootMessage.contains("uq_resort_room_category_facility_group_code")) {
            return "A facility group with this code already exists for this resort room category.";
        }
        if (rootMessage.contains("facility_group_scope_assignments_pkey")) {
            return "This facility scope is already assigned to the facility group.";
        }
        if (rootMessage.contains("facility_scope_assignments_pkey")) {
            return "This facility scope is already assigned to the facility.";
        }
        if (rootMessage.contains("uq_resort_facility_group_code")) {
            return "A facility group with this code already exists for this resort.";
        }
        if (rootMessage.contains("uq_resort_facility_code")) {
            return "A facility with this code already exists for this resort.";
        }
        if (rootMessage.contains("uq_resort_room_category_facility_code")) {
            return "A facility with this code already exists for this resort room category.";
        }
        if (rootMessage.contains("uq_resort_room_category_main_price_active")) {
            return "This room category already has an active main price for this currency.";
        }
        if (rootMessage.contains("uq_resort_room_main_price_active")) {
            return "This room already has an active main price override for this currency.";
        }
        if (rootMessage.contains("excl_reservations_no_overlap")) {
            return "This room is already booked for an overlapping date range — choose different dates or a different room.";
        }
        return rootMessage;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleIllegalState(
            IllegalStateException ex,
            HttpServletRequest request
    ) {
        return build(
                ex,
                HttpStatus.CONFLICT,
                "CONFLICT",
                request
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        return build(
                ex,
                HttpStatus.BAD_REQUEST,
                "INVALID_ARGUMENT",
                request
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex,
            HttpServletRequest request
    ) {

        log.warn("Entity not found: {}", ex.getMessage());

        ApiErrorResponse response = new ApiErrorResponse(
                request.getHeader("X-Request-Id"),
                HttpStatus.NOT_FOUND.value(),
                "ENTITY_NOT_FOUND",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleUnauthorized(
            AuthorizationDeniedException ex,
            HttpServletRequest request
    ) {
        log.error("Unauthorized access attempt: {}", ex.getMessage());
        log.error("Unauthorized access attempt: {}", ex.getAuthorizationResult());

        return build(
                ex,
                HttpStatus.FORBIDDEN,
                "UNAUTHORIZED",
                request
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleUnexpected(
            Exception ex,
            HttpServletRequest request
    ) {

        log.error("Unexpected error", ex);

        return build(
                ex,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                request
        );
    }

    private ResponseEntity<@NonNull ApiErrorResponse> build(
            Exception ex,
            HttpStatus status,
            String code,
            HttpServletRequest request
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
                request.getHeader("X-Request-Id"),
                status.value(),
                code,
                ex.getMessage()
        );
        return ResponseEntity.status(status).body(response);
    }

    private String extractRootCauseMessage(Throwable ex) {
        Throwable root = ex;

        while (root.getCause() != null) {
            root = root.getCause();
        }

        return root.getMessage();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {
        return build(
                ex,
                HttpStatus.UNAUTHORIZED,
                "INVALID_CREDENTIALS",
                request
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleDisabledAccount(
            DisabledException ex,
            HttpServletRequest request
    ) {
        return build(
                ex,
                HttpStatus.FORBIDDEN,
                "ACCOUNT_DISABLED",
                request
        );
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleLockedAccount(
            LockedException ex,
            HttpServletRequest request
    ) {
        return build(
                ex,
                HttpStatus.LOCKED,
                "ACCOUNT_LOCKED",
                request
        );
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleExpiredAccount(
            AccountExpiredException ex,
            HttpServletRequest request
    ) {
        return build(
                ex,
                HttpStatus.FORBIDDEN,
                "ACCOUNT_EXPIRED",
                request
        );
    }

}
