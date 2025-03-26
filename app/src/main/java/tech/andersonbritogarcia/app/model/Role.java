package tech.andersonbritogarcia.app.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Role {

    @Id
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(10)")
    private RoleName name;

    public Role() {
    }

    public Role(Integer id, RoleName name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Role role)) {
            return false;
        }

        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
