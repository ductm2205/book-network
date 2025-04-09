package se2.BookNetwork.interfaces;

import org.springframework.security.core.Authentication;

import se2.BookNetwork.core.PageResponse;
import se2.BookNetwork.core.requests.FeedbackRequest;
import se2.BookNetwork.core.responses.FeedbackResponse;

public interface IFeedbackService {
    Integer saveFeedback(FeedbackRequest request, Authentication connectedUser);

    PageResponse<FeedbackResponse> findAllFeedbackByBookId(
            Integer bookId,
            Authentication connectedUser,
            int page,
            int size);
}
