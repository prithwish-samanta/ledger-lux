package dev.prithwish.ledgerlux.auth;

import dev.prithwish.ledgerlux.common.exception.AuthTokenExpiredException;
import dev.prithwish.ledgerlux.common.exception.EmailAlreadyPresentException;
import dev.prithwish.ledgerlux.user.NotificationPreferences;
import dev.prithwish.ledgerlux.user.User;
import dev.prithwish.ledgerlux.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(
            UserRepository userRepo, UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtProvider jwtProvider) {
        this.userRepo = userRepo;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public SignUpResponse register(RegisterRequest req) {
        // Check if email already exists
        if (userRepo.findByEmail(req.email()).isPresent()) {
            throw new EmailAlreadyPresentException("Email already in use");
        }
        // Create a new user entity
        User user = new User();
        user.setEmail(req.email());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setDisplayName(req.displayName());
        user.setCurrency(req.currency());
        user.setTimezone(req.timezone());
        user.setRoles(new String[]{"USER"});

        // Initialize notification preferences
        NotificationPreferences prefs = new NotificationPreferences();
        prefs.setBudgetAlerts(true);
        prefs.setGoalReminders(true);
        prefs.setBillReminders(true);
        prefs.setEmailNotifications(true);
        user.setNotificationPreferences(prefs);

        user.setCreatedAt(new Date());
        // Save to DB
        User saved = userRepo.save(user);
        // Map to DTO
        return new SignUpResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getDisplayName(),
                saved.getCurrency(),
                saved.getTimezone(),
                saved.getCreatedAt()
        );
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String jwtToken = jwtProvider.generateAccessToken(authenticate);
        String refreshToken = jwtProvider.generateRefreshToken(req.email());
        return new AuthResponse(jwtToken, refreshToken, jwtProvider.getAccessTokenExpirationTime());
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (jwtProvider.isTokenExpired(refreshToken)) {
            throw new AuthTokenExpiredException("Refresh token has expired");
        }
        String username = jwtProvider.extractSubject(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        String newAccessToken = jwtProvider.generateAccessToken(authentication);
        String newRefreshToken = jwtProvider.generateRefreshToken(username);

        return new AuthResponse(newAccessToken, newRefreshToken, jwtProvider.getAccessTokenExpirationTime());
    }
}
