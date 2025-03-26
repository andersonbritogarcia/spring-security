package tech.andersonbritogarcia.app.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.andersonbritogarcia.app.model.RoleName;
import tech.andersonbritogarcia.app.model.User;
import tech.andersonbritogarcia.app.repository.RoleRepository;
import tech.andersonbritogarcia.app.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(String username, String password, RoleName roleName) {
        var role = roleRepository.findByName(roleName).orElseThrow(() -> new EntityNotFoundException("Role not found"));

        var user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(username.trim());
        user.setPassword(passwordEncoder.encode(password.trim()));
        user.setRoles(Set.of(role));

        return repository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public List<User> findAll() {
        return repository.findAll();
    }
}
