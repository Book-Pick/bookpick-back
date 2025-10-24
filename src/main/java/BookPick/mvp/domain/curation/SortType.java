package BookPick.mvp.domain.curation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortType {

    SORT_LATEST("latest");

    private final String value;
}
