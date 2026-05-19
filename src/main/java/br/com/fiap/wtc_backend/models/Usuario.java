package br.com.fiap.wtc_backend.models; // Ajuste se seu pacote for diferente

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Document(collection = "usuarios")
public class Usuario implements UserDetails {

    @Id
    private String id;
    private String email;
    private String senha;
    private boolean isOperador;
    private String fcmToken;

    // --- Construtores, Getters e Setters ---
    public Usuario() {}

    public Usuario(String email, String senha, boolean isOperador) {
        this.email = email;
        this.senha = senha;
        this.isOperador = isOperador;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public boolean isOperador() { return isOperador; }
    public void setOperador(boolean operador) { isOperador = operador; }
    public String getFcmToken() { return fcmToken; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }

    // --- Métodos do UserDetails (Obrigatórios do Spring Security) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Se for operador, tem a role ADMIN. Se for cliente, tem a role USER.
        if (this.isOperador) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getPassword() { return this.senha; }
    @Override
    public String getUsername() { return this.email; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}