package tech.andersonbritogarcia.app.controller.dto;

import tech.andersonbritogarcia.app.model.Role;
import tech.andersonbritogarcia.app.model.RoleName;
import tech.andersonbritogarcia.app.model.User;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserResponse(UUID id, String username, String password, Set<RoleName> roles) {

    public UserResponse(User user) {
        this(user.getId(),
             user.getUsername(),
             user.getPassword(),
             user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    }
}
