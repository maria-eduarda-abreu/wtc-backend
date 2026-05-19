package br.com.fiap.wtc_backend.repositories;

import br.com.fiap.wtc_backend.models.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    // Método que o Spring Security vai usar para buscar o usuário pelo email
    UserDetails findByEmail(String email);
}