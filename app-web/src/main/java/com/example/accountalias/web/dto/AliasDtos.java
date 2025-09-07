package com.example.accountalias.web.dto;

import jakarta.validation.constraints.NotBlank;

public class AliasDtos {
    public record CreateAliasRequest(@NotBlank String alias) {}
    public record UpdateAliasStatusRequest(boolean active) {}
    public record AliasResponse(Long id, String alias, boolean active) {}
}

