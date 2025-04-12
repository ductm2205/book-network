package se2.BookNetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import se2.BookNetwork.models.common.FavouriteBook;

public interface FavouriteBookRepository extends JpaRepository<FavouriteBook, Integer> {

}
