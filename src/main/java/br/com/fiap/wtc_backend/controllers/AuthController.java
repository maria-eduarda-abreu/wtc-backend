package br.com.fiap.wtc_backend.controllers;

import br.com.fiap.wtc_backend.dto.DadosAutenticacao;
import br.com.fiap.wtc_backend.dto.DadosTokenJWT;
import br.com.fiap.wtc_backend.models.Usuario;
import br.com.fiap.wtc_backend.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid DadosAutenticacao dados) {
        try {
            // 1. Transforma o email e senha recebidos em um token de autenticação interno do Spring
            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());

            // 2. O Spring Security vai usar o AutenticacaoService para buscar o usuário no Mongo e validar a senha criptografada
            var authentication = manager.authenticate(authenticationToken);

            // 3. Se passou pela validação, pegamos o usuário logado e geramos o Token JWT real
            var usuarioLogado = (Usuario) authentication.getPrincipal();
            var tokenJWT = tokenService.gerarToken(usuarioLogado.getEmail());

            // 4. Retorna os dados com o status 200 OK para o Android Studio
            return ResponseEntity.ok(new DadosTokenJWT(
                    tokenJWT,
                    usuarioLogado.getId(),
                    usuarioLogado.getEmail(),
                    usuarioLogado.isOperador()
            ));
        } catch (Exception e) {
            // Se o email ou senha estiverem errados, retorna 401 Unauthorized
            return ResponseEntity.status(401).body("Credenciais inválidas: " + e.getMessage());
        }
    }
}