package se2.BookNetwork.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import se2.BookNetwork.models.common.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}