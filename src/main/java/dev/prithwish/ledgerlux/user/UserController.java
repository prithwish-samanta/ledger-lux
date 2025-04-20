package dev.prithwish.ledgerlux.user;

import dev.prithwish.ledgerlux.common.annotation.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserProfileDto> getProfile(@CurrentUser String userId) {
        UserProfileDto res = userService.getProfile(userId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserProfileDto> updateProfile(@CurrentUser String userId, @Valid @RequestBody UserProfileDto dto) {
        UserProfileDto res = userService.updateProfile(userId, dto);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
