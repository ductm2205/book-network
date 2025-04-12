package se2.BookNetwork.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import se2.BookNetwork.models.common.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Query("""
            SELECT feedback
            FROM Feedback feedback
            WHERE feedback.book.id = :bookId
            AND feedback.deletedAt = null
            """)
    Page<Feedback> findAllFeedbackByBookId(Integer bookId, Pageable pageable);

}