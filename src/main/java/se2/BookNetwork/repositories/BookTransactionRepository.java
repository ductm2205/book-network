package se2.BookNetwork.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import se2.BookNetwork.models.common.BookTransaction;

public interface BookTransactionRepository extends JpaRepository<BookTransaction, Integer> {

        @Query("""
                        SELECT history
                        FROM BookTransaction history
                        WHERE history.user.id = :userId
                        """)
        Page<BookTransaction> findAllBorrowedBooks(@Param("userId") Integer id, Pageable pageable);

        @Query("""
                        SELECT history
                        FROM BookTransaction history
                        WHERE history.book.owner.id = :userId
                        """)
        Page<BookTransaction> findAllReturnedBooks(@Param("userId") Integer id, Pageable pageable);

        @Query("""
                        SELECT (COUNT (*) > 0) AS isBorrowed
                        FROM BookTransaction history
                        WHERE history.book.id = :bookId
                        AND history.isReturnApproved = false
                        """)
        boolean isAlreadyBorrowed(@Param("bookId") Integer bookId);

        @Query("""
                        SELECT history
                        FROM BookTransaction history
                        WHERE history.user.id = :userId
                        AND history.book.id = :bookId
                        AND history.isReturnApproved = false
                        """)
        Optional<BookTransaction> findBorrowedBookByUser(@Param("userId") Integer userId,
                        @Param("bookId") Integer bookId);

        @Query("""
                        SELECT transaction
                        FROM BookTransaction transaction
                        WHERE transaction.book.createdBy = :userId
                        AND transaction.book.id = :bookId
                        AND transaction.isReturned = true
                        AND transaction.isReturnApproved = false
                        """)
        Optional<BookTransaction> findLentBookByUser(@Param("userId") Integer userId,
                        @Param("bookId") Integer bookId);

}