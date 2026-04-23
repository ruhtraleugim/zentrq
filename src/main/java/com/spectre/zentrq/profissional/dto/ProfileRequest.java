package com.spectre.zentrq.profissional.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record ProfileRequest(
    @NotBlank @Pattern(regexp = "\\d{5}-?\\d{3}") String cep,
    @NotEmpty List<@NotNull Long> cityIds,
    List<Long> skillIds
) {}
