package se2.BookNetwork.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import se2.BookNetwork.models.common.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByEmail(String email);
}
