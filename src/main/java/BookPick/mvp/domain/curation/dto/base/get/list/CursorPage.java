package BookPick.mvp.domain.curation.dto.base.get.list;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CursorPage<T> {
    private final List<T> content;
    private final boolean hasNext;
    private final Long nextCursor;
}
