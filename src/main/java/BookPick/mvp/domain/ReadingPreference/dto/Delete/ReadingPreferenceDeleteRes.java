package BookPick.mvp.domain.ReadingPreference.dto.Delete;

import java.time.LocalDateTime;

public record ReadingPreferenceDeleteRes(
        Long preferenceId,
//            boolean deleted,        // 추후 soft 삭제 시 사용
        LocalDateTime deletedAt
) {
    static public ReadingPreferenceDeleteRes from(Long preferenceId, LocalDateTime deletedAt){
       return new ReadingPreferenceDeleteRes(preferenceId, deletedAt);
    }
}
