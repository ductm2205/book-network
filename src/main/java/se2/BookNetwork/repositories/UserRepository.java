package se2.BookNetwork.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import se2.BookNetwork.models.common.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("""
            SELECT user
            FROM User user
            WHERE user.deletedAt IS null
            """)
    Page<User> getPaginatedUsersList(Pageable pageable);
}
