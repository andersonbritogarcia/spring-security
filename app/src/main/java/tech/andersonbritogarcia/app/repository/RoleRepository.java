package tech.andersonbritogarcia.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tech.andersonbritogarcia.app.model.Role;
import tech.andersonbritogarcia.app.model.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {


    Optional<Role> findByName(RoleName name);
}
