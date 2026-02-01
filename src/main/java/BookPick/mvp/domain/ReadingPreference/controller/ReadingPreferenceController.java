package BookPick.mvp.domain.ReadingPreference.controller;

import BookPick.mvp.domain.ReadingPreference.dto.ETC.Delete.ReadingPreferenceDeleteRes;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceReq;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceRes;
import BookPick.mvp.domain.ReadingPreference.service.ReadingPreferenceService;
import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reading-preference")
@RequiredArgsConstructor
public class ReadingPreferenceController {

    private final ReadingPreferenceService readingPreferenceService;
    private final CurrentUserCheck currentUserCheck;

    @Operation(
            summary = "독서 취향 생성",
            description = "사용자의 독서 취향을 등록합니다",
            tags = {"Reading Preference"})
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "독서 취향 등록 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 독서취향 요청값입니다", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요합니다", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 독서 취향이 존재합니다", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ReadingPreferenceRes>> create(
            @Valid @RequestBody ReadingPreferenceReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        ReadingPreferenceRes res =
                readingPreferenceService.addReadingPreference(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.READING_PREFERENCE_REGISTER_SUCCESS, res));
    }

    @Operation(
            summary = "독서 취향 조회",
            description = "사용자의 독서 취향 상세 조회",
            tags = {"Reading Preference"})
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "독서 취향 조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요합니다", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse<ReadingPreferenceRes>> getDetails(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        currentUserCheck.validateLoginUser(currentUser);

        ReadingPreferenceRes res =
                readingPreferenceService.findReadingPreference(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.READING_PREFERENCE_READ_SUCCESS, res));
    }

    @Operation(
            summary = "독서 취향 수정",
            description = "사용자의 독서 취향을 수정합니다",
            tags = {"Reading Preference"})
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "독서 취향 수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 독서취향 요청값입니다", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요합니다", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PatchMapping
    public ResponseEntity<ApiResponse<ReadingPreferenceRes>> update(
            @Valid @RequestBody ReadingPreferenceReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        ReadingPreferenceRes res =
                readingPreferenceService.modifyReadingPreference(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.READING_PREFERENCE_UPDATE_SUCCESS, res));
    }

    @Operation(
            summary = "독서 취향 삭제",
            description = "사용자의 독서 취향을 삭제합니다",
            tags = {"Reading Preference"})
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "독서 취향 삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요합니다", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<ApiResponse<ReadingPreferenceDeleteRes>> delete(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        ReadingPreferenceDeleteRes res =
                readingPreferenceService.removeReadingPreference(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.READING_PREFERENCE_DELETE_SUCCESS, res));
    }
}
