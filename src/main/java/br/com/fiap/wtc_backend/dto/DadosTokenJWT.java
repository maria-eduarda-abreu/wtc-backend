package br.com.fiap.wtc_backend.dto;

public record DadosTokenJWT(
        String token,
        String uid,
        String email,
        boolean isOperador
) {}
