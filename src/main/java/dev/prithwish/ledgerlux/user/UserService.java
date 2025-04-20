package dev.prithwish.ledgerlux.user;

public interface UserService {
    UserProfileDto getProfile(String userId);

    UserProfileDto updateProfile(String userId, UserProfileDto dto);
}
