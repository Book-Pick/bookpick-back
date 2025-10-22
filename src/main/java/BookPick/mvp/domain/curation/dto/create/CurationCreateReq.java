package BookPick.mvp.domain.curation.dto.create;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public record CurationCreateReq(
        Thumbnail thumbnail,
        CustomBook customBook,
        String review,
        Recommend recommend
) {
}

@AllArgsConstructor
@Getter
@Setter
class Thumbnail{
    private String imageUrl;
    private String imageColor;
}

@AllArgsConstructor
@Getter
@Setter
class CustomBook {
    String title;
    String author;
    String isbn;
}

class Recommend{
}
//book": {
//    "title": "노르웨이의 숲",
//    "author": "무라카미 하루키,
//    "ISBN" : "123213"
//  },
//
//   "review": "외로움과 청춘의 쓸쓸함이 절묘하게 녹아든 작품이에요. 비오는 날 읽으면 감정선이 더 깊어져요.",
//
//
//  "recommend" : {
//     "mood": ["비오는 날", "퇴근 후", "밤늦게"],
//     "genres": ["소설", "에세이"],
//     "keywords": ["사랑", "고독", "성장"],
//     "style": ["한 번에 몰입해서 읽는 편", "독서 후 감상을 기록하는 편"],
//   }