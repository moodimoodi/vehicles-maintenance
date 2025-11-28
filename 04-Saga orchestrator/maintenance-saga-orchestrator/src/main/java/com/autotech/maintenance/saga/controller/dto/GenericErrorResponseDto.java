package com.autotech.maintenance.saga.controller.dto;

import lombok.Data;

@Data
public class GenericErrorResponseDto {
    private String message;
    private String details;
}
