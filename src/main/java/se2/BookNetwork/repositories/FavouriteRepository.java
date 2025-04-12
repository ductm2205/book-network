package se2.BookNetwork.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import se2.BookNetwork.models.common.Favourite;

public interface FavouriteRepository extends JpaRepository<Favourite, Integer> {
    Optional<Favourite> findByOwnerId(Integer userId);
}
