package BookPick.mvp.domain.ReadingPreference.dto.ETC.Delete;

import java.time.LocalDateTime;

public record ReadingPreferenceDeleteRes(
        Long preferenceId,
        //            boolean deleted,        // 추후 soft 삭제 시 사용
        LocalDateTime deletedAt) {
    public static ReadingPreferenceDeleteRes from(Long preferenceId, LocalDateTime deletedAt) {
        return new ReadingPreferenceDeleteRes(preferenceId, deletedAt);
    }
}
