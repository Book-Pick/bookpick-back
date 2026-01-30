package BookPick.mvp.domain.auth.dto;

import BookPick.mvp.domain.auth.service.CustomUserDetails;

public record LoginRes(
        long userId,
        String email,
        String nickname,
        String bio,
        String profileImageUrl,
        boolean isFirstLogin,
        String accessToken,
        String refreshToken) {

    public static LoginRes from(
            CustomUserDetails customUserDetails, String accessToken, String refreshToken) {
        return new LoginRes(
                customUserDetails.getId(),
                customUserDetails.getUsername(), // username = email
                customUserDetails.getNickname(),
                customUserDetails.getBio(),
                customUserDetails.getProfileImageUrl(),
                customUserDetails.isFirstLogin(),
                accessToken,
                refreshToken);
    }

    public static LoginRes fromWithoutRefreshToken(LoginRes res, String accessToken) {
        return new LoginRes(
                res.userId(),
                res.email(),
                res.nickname(),
                res.bio(),
                res.profileImageUrl(),
                res.isFirstLogin(),
                accessToken,
                null);
    }
}
