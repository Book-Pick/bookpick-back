// CurationListController.java에 추가
package BookPick.mvp.domain.curation.controller.base;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.book.util.kakaoApi.BookSearchService;
import BookPick.mvp.domain.curation.dto.base.CurationReq;
import BookPick.mvp.domain.curation.dto.base.create.CurationCreateRes;
import BookPick.mvp.domain.curation.dto.base.create.CurationCreateResult;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateReq;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateRes;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateResult;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.exception.common.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.service.base.create.CurationCreateService;
import BookPick.mvp.domain.curation.service.base.update.CurationUpdateService;
import BookPick.mvp.domain.curation.service.csv.CurationCsvService;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/v1/curations")
@RequiredArgsConstructor
public class CurationController {

    private final CurationCreateService curationCreateService;
    private final CurationUpdateService curationUpdateService;
    private final CurationCsvService curationCsvService;
    private final BookSearchService bookSearchService;
    private final CurrentUserCheck currentUserCheck;

    @Operation(summary = "큐레이션 생성(일반 및 임시저장)", description = "새 큐레이션을 생성합니다 drafted가 true면 임시저장", tags = {"Curation"})
    @PostMapping
    public ResponseEntity<ApiResponse<CurationCreateRes>> createCuration(
            @Valid @RequestBody CurationReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        currentUserCheck.validateLoginUser(currentUser);

        CurationCreateResult result = curationCreateService.saveCuration(currentUser.getId(), req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(result.successCode(), result.curationCreateRes()));
    }


    @Operation(summary = "큐레이션 수정 (재발행 및 재 임시저장", description = "큐레이션 정보를 수정", tags = {"Curation"})
    @PatchMapping("/{curationId}")
    public ResponseEntity<ApiResponse<CurationUpdateRes>> updateCuration(
            @PathVariable Long curationId,
            @Valid @RequestBody CurationUpdateReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        currentUserCheck.validateLoginUser(currentUser);

        CurationUpdateResult curationUpdateResult = curationUpdateService.updateCuration(currentUser.getId(), curationId, req);

        return ResponseEntity.ok()
                .body(ApiResponse.success(curationUpdateResult.successCode(), curationUpdateResult.curationUpdateRes()));
    }

    @Operation(
            summary = "큐레이션의 책 구매 링크 제공",
            description = "큐레이션 ID로 조회하여 해당 큐레이션의 책 제목으로 카카오 API를 사용해 외부 서점 검색 링크를 제공합니다",
            tags = {"Curation"}
    )
    @GetMapping("/{curationId}/book-link")
    public ResponseEntity<ApiResponse<String>> getCurationBookPurchaseLink(
            @PathVariable Long curationId,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        currentUserCheck.validateLoginUser(currentUser);


        // 책 제목으로 카카오 API 호출하여 첫 번째 결과의 URL 반환
        String link = bookSearchService.getBookPurchaseLink(curationId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        SuccessCode.BOOK_LINK_READ_SUCCESS,
                        link
                ));
    }

    @Operation(
            summary = "CSV 파일로 큐레이션 일괄 생성",
            description = "CSV 파일을 업로드하여 여러 큐레이션을 한 번에 생성합니다. " +
                    "CSV 형식: title, bookTitle, bookAuthor, bookIsbn, bookImageUrl, review, moods, genres, keywords, styles, thumbnailUrl, thumbnailColor",
            tags = {"Curation"}
    )
    @PostMapping(value = "/upload-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<CurationCreateRes>>> uploadCsv(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        currentUserCheck.validateLoginUser(currentUser);

        // CSV 파일 처리 및 큐레이션 생성
        List<CurationCreateRes> results = curationCsvService.uploadCsvAndCreateCurations(file, currentUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        SuccessCode.CURATION_PUBLISH_SUCCESS,
                        results
                ));
    }


}



