package br.com.fiap.wtc_backend.services; // Ajuste o pacote se o seu for diferente

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Pega a chave secreta que configuramos no application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    // Função que cria o Token quando o usuário faz login no Android
    public String gerarToken(String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("wtc-backend")
                    .withSubject(email)
                    .withExpiresAt(dataExpiracao()) // Token expira em 2 horas
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    // Função que verifica se o Token enviado pelo Android é verdadeiro
    public String getSubject(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("wtc-backend")
                    .build()
                    .verify(tokenJWT)
                    .getSubject(); // Devolve o email do usuário dono do token
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    // Define a validade do Token (2 horas a partir de agora)
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}