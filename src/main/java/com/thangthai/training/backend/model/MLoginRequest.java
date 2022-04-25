package com.thangthai.training.backend.model;

import lombok.Data;

@Data
public class MLoginRequest {
    private String email;

    private String password;
}
