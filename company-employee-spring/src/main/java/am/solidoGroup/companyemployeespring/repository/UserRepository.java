package am.solidoGroup.companyemployeespring.repository;

import am.solidoGroup.companyemployeespring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String username);
}
