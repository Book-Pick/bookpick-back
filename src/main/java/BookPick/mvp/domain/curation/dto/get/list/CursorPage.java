package BookPick.mvp.domain.curation.dto.get.list;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CursorPage<T> {
    private final List<T> content;
    private final boolean hasNext;
    private final Long nextCursor;
}