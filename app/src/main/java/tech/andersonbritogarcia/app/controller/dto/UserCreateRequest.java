package tech.andersonbritogarcia.app.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tech.andersonbritogarcia.app.model.RoleName;

public record UserCreateRequest(@NotBlank String username, @NotBlank String password, @NotNull RoleName role) {

}
