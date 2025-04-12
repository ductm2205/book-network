package se2.BookNetwork.core.mappers;

import java.util.Objects;

import org.springframework.stereotype.Service;

import se2.BookNetwork.core.requests.FeedbackRequest;
import se2.BookNetwork.core.responses.FeedbackResponse;
import se2.BookNetwork.models.common.Book;
import se2.BookNetwork.models.common.Feedback;

@Service
public class FeedbackMapper {

    public Feedback toFeedBack(FeedbackRequest request) {
        return Feedback.builder()
                .id(request.getId())
                .rate(request.getRate())
                .comment(request.getComment())
                .book(Book.builder()
                        .id(request.getBookId())
                        .shareable(false)
                        .archived(false)
                        .build())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback fb, Integer id) {

        return FeedbackResponse.builder()
                .id(fb.getId())
                .rate(fb.getRate())
                .comment(fb.getComment())
                .owner(fb.getCreatedBy())
                .bookId(fb.getBook().getId())
                .isOwnFeedback(Objects.equals(fb.getBook().getOwner().getId(), id))
                .build();
    }

}