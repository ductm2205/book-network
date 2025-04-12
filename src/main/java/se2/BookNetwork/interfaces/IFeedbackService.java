package se2.BookNetwork.interfaces;

import org.springframework.security.core.Authentication;

import se2.BookNetwork.core.PageResponse;
import se2.BookNetwork.core.requests.FeedbackRequest;
import se2.BookNetwork.core.responses.FeedbackResponse;

public interface IFeedbackService {
    Integer saveFeedback(FeedbackRequest request, Authentication connectedUser);

    Integer updateFeedback(FeedbackRequest request, Authentication currentUser);

    Integer deleteFeedback(Integer feedbackId, Authentication currentUser);

    PageResponse<FeedbackResponse> findAllFeedbackByBookId(
            Integer bookId,
            Authentication connectedUser,
            int page,
            int size);

    FeedbackResponse getFeedbackById(Integer id, Authentication currentUser);
}
