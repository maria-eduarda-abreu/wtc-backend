package br.com.fiap.wtc_backend.config;

import br.com.fiap.wtc_backend.models.Usuario;
import br.com.fiap.wtc_backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DbSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se o banco já tem usuários cadastrados
        if (usuarioRepository.count() == 0) {
            System.out.println("Banco de dados vazio! Criando usuários iniciais para teste...");

            // Cria o mesmo operador padrão que o seu app Android Studio já usa para simulação
            String emailOperador = "operador@wtc.com";
            // Criptografa a senha "123456" usando o BCrypt antes de salvar
            String senhaCriptografada = passwordEncoder.encode("123456");

            Usuario operador = new Usuario(emailOperador, senhaCriptografada, true);
            usuarioRepository.save(operador);

            System.out.println("Usuário de teste criado com sucesso!");
            System.out.println("Email: " + emailOperador + " | Senha: 123456");
        }
    }
}
