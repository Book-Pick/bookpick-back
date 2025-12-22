package BookPick.mvp.domain.curation.controller.base.read;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.base.get.one.field.CurationGetRes;
import BookPick.mvp.domain.curation.service.base.read.CurationReadService;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/curations")
@RequiredArgsConstructor
public class CurationReadController {

    private final CurationReadService curationReadService;
    private final CurrentUserCheck currentUserCheck;


    @Operation(summary = "큐레이션 일반 조회용 단건 조회", description = "큐레이션 ID로 단건 조회", tags = {"Curation"})
    @GetMapping("/{curationId}")
    public ResponseEntity<ApiResponse<CurationGetRes>> getCuration(
            @PathVariable Long curationId,
            HttpServletRequest req,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        currentUserCheck.validateLoginUser(currentUser);    // 미 로그인 사용자 접근 방어 로직


        CurationGetRes res = curationReadService.findCuration(curationId, currentUser, req,false);


        return ResponseEntity.ok()
                .body(ApiResponse.success(SuccessCode.CURATION_GET_SUCCESS, res));
    }


    @Operation(summary = "큐레이션 수정용 단건 조회", description = "큐레이션 ID로 단건 조회", tags = {"Curation"})
    @GetMapping("/{curationId}/edit")
    public ResponseEntity<ApiResponse<CurationGetRes>> getCurationForEdit(
            @PathVariable Long curationId,
            HttpServletRequest req,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        currentUserCheck.validateLoginUser(currentUser);    // 미 로그인 사용자 접근 방어 로직


        CurationGetRes res = curationReadService.findCuration(curationId, currentUser, req, true);


        return ResponseEntity.ok()
                .body(ApiResponse.success(SuccessCode.CURATION_GET_SUCCESS, res));
    }



}