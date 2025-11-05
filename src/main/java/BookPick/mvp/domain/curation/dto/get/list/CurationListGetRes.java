// CurationListGetRes.java
package BookPick.mvp.domain.curation.dto.get.list;

import BookPick.mvp.domain.curation.enums.SortType;
import java.util.List;

public record CurationListGetRes(
    String sortType,
    String description,
    List<CurationContentRes> content,
    int size,
    boolean hasNext,
    Long nextCursor
) {
    public static CurationListGetRes from(SortType sortType, List<CurationContentRes> content,
                                          boolean hasNext, Long nextCursor) {
        return new CurationListGetRes(
                sortType.getValue(),
                sortType.getDescription(),
                content,
                content.size(),
                hasNext,
                nextCursor
        );
    }
}