package dev.prithwish.ledgerlux.user;

import dev.prithwish.ledgerlux.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserProfileDto getProfile(String username) {
        User user = userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserProfileDto(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getCurrency(),
                user.getTimezone(),
                user.getCreatedAt()
        );
    }

    @Override
    public UserProfileDto updateProfile(String username, UserProfileDto dto) {
        // Find existing user
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // Update fields
        user.setDisplayName(dto.displayName());
        user.setCurrency(dto.currency());
        user.setTimezone(dto.timezone());
        user.setUpdatedAt(new Date());
        // Save changes
        User updated = userRepo.save(user);
        // Map to DTO
        return new UserProfileDto(
                updated.getId(),
                updated.getEmail(),
                updated.getDisplayName(),
                updated.getCurrency(),
                updated.getTimezone(),
                updated.getCreatedAt()
        );
    }
}
