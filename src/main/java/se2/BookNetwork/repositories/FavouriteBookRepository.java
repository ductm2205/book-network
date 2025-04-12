package se2.BookNetwork.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import se2.BookNetwork.models.common.FavouriteBook;

public interface FavouriteBookRepository extends JpaRepository<FavouriteBook, Integer> {
        @Query("""
                        SELECT favouriteBook
                        FROM FavouriteBook favouriteBook
                        WHERE favouriteBook.favourite.id = :favouriteId
                        AND favouriteBook.deletedAt is null
                        """)
        Page<FavouriteBook> findAllByFavouriteId(Integer favouriteId, Pageable pageable);

        @Query("""
                        SELECT favourite
                        FROM FavouriteBook favourite
                        WHERE favourite.favourite.id = :favouriteId
                        AND favourite.book.id = :bookId
                        """)
        Optional<FavouriteBook> findByFavouriteIdAndBookId(@Param(value = "favouriteId") Integer favouriteId,
                        @Param(value = "bookId") Integer bookId);

        @Query("""
                        SELECT (COUNT (*) > 0) AS isFavoured
                        FROM FavouriteBook favourite
                        WHERE favourite.favourite.id = :favouriteId
                        AND favourite.book.id = :bookId
                        """)
        boolean isBookFavoured(Integer favouriteId, Integer bookId);
}
