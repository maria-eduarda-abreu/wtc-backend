package br.com.fiap.wtc_backend.security;

import br.com.fiap.wtc_backend.repositories.UsuarioRepository;
import br.com.fiap.wtc_backend.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            // Se o app mandou um token, nós validamos e pegamos o email que está dentro dele
            var subject = tokenService.getSubject(tokenJWT);

            // Buscamos esse usuário no banco de dados MongoDB
            UserDetails usuario = repository.findByEmail(subject);

            // Avisamos ao Spring Security: "Este usuário está logado e verificado!"
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continua o fluxo da requisição
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            // Remove a palavra "Bearer " e pega só o token
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}