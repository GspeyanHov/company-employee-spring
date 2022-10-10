package am.solidoGroup.companyemployeespring;

import am.solidoGroup.companyemployeespring.entity.Role;
import am.solidoGroup.companyemployeespring.entity.User;
import am.solidoGroup.companyemployeespring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
@RequiredArgsConstructor
public class CompanyEmployeeSpringApplication implements CommandLineRunner {


    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(CompanyEmployeeSpringApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) {
        Optional<User> userByEmail = userRepository.findUserByEmail("admin@mail.com");
        if (userByEmail.isEmpty()) {
            userRepository.save(User.builder()
                    .name("admin")
                    .surname("admin")
                    .email("admin@mail.com")
                    .role(Role.ADMIN)
                    .password(passwordEncoder().encode("admin"))
                    .build());
        }
    }
}
