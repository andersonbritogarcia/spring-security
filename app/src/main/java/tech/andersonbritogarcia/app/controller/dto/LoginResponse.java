package tech.andersonbritogarcia.app.controller.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
