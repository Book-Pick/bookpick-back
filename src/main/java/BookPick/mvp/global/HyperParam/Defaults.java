package BookPick.mvp.global.HyperParam;

import BookPick.mvp.domain.curation.enums.common.SortType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Defaults {
    SORT_TYPE(SortType.SORT_LATEST),
    PAGE_SIZE(20);

    private Object value;
    public static final String SORT_TYPE_STRING = SORT_TYPE.getValueAsString(); // 상수 정의

    public Object getValue() {
        return value;
    }

    public String getValueAsString() { // 문자열로 변환해주는 메서드
        if (value instanceof Enum) {
            return ((Enum<?>) value).name(); // Enum이면 name() 사용
        }
        return String.valueOf(value); // 그 외에는 그냥 toString
    }
}
