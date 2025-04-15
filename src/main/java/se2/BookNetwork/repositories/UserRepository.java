package se2.BookNetwork.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import se2.BookNetwork.models.common.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("""
            SELECT user
            FROM User user
            WHERE user.deletedAt IS null
            """)
    Page<User> getPaginatedUsersList(Pageable pageable);

    @Query("""
                SELECT u FROM User u
                WHERE u.deletedAt IS null AND (
                    LOWER(u.firstname) LIKE LOWER(CONCAT('%', :query, '%')) OR
                    LOWER(u.lastname) LIKE LOWER(CONCAT('%', :query, '%')) OR
                    LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) OR
                    LOWER(CONCAT(u.firstname, ' ', u.lastname)) LIKE LOWER(CONCAT('%', :query, '%'))
                )
            """)

    Page<User> searchUsers(@Param("query") String query, Pageable pageable);
}
