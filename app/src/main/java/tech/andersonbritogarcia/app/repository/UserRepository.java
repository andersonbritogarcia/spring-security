package tech.andersonbritogarcia.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.andersonbritogarcia.app.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("""
           SELECT u
           FROM User u
           LEFT JOIN FETCH u.roles
           WHERE u.username = :username
           """)
    Optional<User> findByUsername(String username);
}
