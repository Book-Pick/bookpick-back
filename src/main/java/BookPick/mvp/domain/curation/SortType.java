// SortType.java
package BookPick.mvp.domain.curation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortType {
    SORT_LATEST("latest", "최신순 정렬"),
    SORT_POPULAR("popular", "인기순 정렬"),
    SORT_SIMILARITY("similarity", "취향 유사도순 정렬");

    private final String value;
    private final String description;


    public static SortType fromValue(String value) {
        for (SortType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return SORT_LATEST; // 기본값
    }
}