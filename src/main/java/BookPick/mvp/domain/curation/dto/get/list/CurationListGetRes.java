package BookPick.mvp.domain.curation.dto.get.list;

import java.util.List;

public record CurationListGetRes(
    String sortType,
    String description,
    List<CurationContentRes> content,
    boolean hasNext,
    Long nextCursor
) {}