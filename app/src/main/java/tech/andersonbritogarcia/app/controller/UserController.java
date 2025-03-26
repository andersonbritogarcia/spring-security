package tech.andersonbritogarcia.app.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.andersonbritogarcia.app.controller.dto.UserCreateRequest;
import tech.andersonbritogarcia.app.controller.dto.UserResponse;
import tech.andersonbritogarcia.app.service.UserService;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody @Valid UserCreateRequest request) {
        var user = userService.createUser(request.username(), request.password(), request.role());
        return new UserResponse(user);
    }

    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.findAll()
                          .stream()
                          .map(UserResponse::new)
                          .toList();
    }
}
