package se2.BookNetwork.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import se2.BookNetwork.models.common.Book;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
        @Query("""
                        SELECT book
                        FROM Book book
                        WHERE book.archived = false
                        AND book.shareable = true
                        """)
        Page<Book> findAllDisplayableBooksForHomePage(Pageable pageable);

        @Query("""
                        SELECT book
                        FROM Book book
                        WHERE book.archived = false
                        AND book.shareable = true
                        AND book.owner.id != :userId
                        """)
        Page<Book> findAllDisplayableBooks(Pageable pageable, @Param("userId") Integer userId);

        // Combined search
        @Query("SELECT b FROM Book b WHERE " +
                        "LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                        "LOWER(b.authorName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                        "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                        "LOWER(b.owner.firstname) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                        "LOWER(b.owner.lastname) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                        "LOWER(b.owner.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                        "LOWER(b.synopsis) LIKE LOWER(CONCAT('%', :query, '%'))")
        Page<Book> searchBooks(@Param("query") String query, Pageable pageable);
}