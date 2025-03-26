package tech.andersonbritogarcia.app.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.andersonbritogarcia.app.controller.dto.LoginRequest;
import tech.andersonbritogarcia.app.controller.dto.LoginResponse;
import tech.andersonbritogarcia.app.service.UserService;

import java.time.Instant;

@RestController
public class loginController {

    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;

    public loginController(JwtEncoder jwtEncoder, BCryptPasswordEncoder passwordEncoder, UserService userService) {
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        var user = userService.findByUsername(request.username()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        validatePassword(request.password(), user.getPassword());
        var now = Instant.now();
        var expiresIn = 7200L;
        var roles = user.getRoles().stream().map(r -> r.getName().name()).toList();
        var scopes = String.join(" ", roles);

        var claims = JwtClaimsSet.builder()
                                 .subject(user.getId().toString())
                                 .issuedAt(now)
                                 .expiresAt(now.plusSeconds(expiresIn))
                                 .claim("scope", scopes)
                                 .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new LoginResponse(jwtValue, expiresIn);
    }

    private void validatePassword(String password, String encodedPassword) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new BadCredentialsException("Username or password is incorrect");
        }
    }
}
