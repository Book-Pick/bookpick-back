package BookPick.mvp.domain.comment.dto;

/*
"{
  ""status"": 201,
  ""message"": ""댓글이 성공적으로 등록되었습니다."",
  ""data"": {
    ""commentId"": 71,
    ""postId"": 8,
    ""parentId"": null,
    ""nickname"": ""책읽는밤"",
    ""profileImageUrl"": ""https://example.com/profile/reader.png"",
    ""content"": ""이번 큐레이션 덕분에 좋은 책을 발견했어요!"",
    ""createdAt"": ""2025-10-10T20:15:00""
  }
}
"
*/

public record CreateCommentReq(
        Long commentId,
        Long postId,
        Long parentId,
        String nickName,
        String profileImageUrl,
        String content,

) {

}