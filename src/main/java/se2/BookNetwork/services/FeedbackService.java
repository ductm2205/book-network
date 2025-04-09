package se2.BookNetwork.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.PageResponse;
import se2.BookNetwork.core.mappers.FeedbackMapper;
import se2.BookNetwork.core.requests.FeedbackRequest;
import se2.BookNetwork.core.responses.FeedbackResponse;
import se2.BookNetwork.exceptions.UnauthorizedOperationException;
import se2.BookNetwork.interfaces.IFeedbackService;
import se2.BookNetwork.models.common.Book;
import se2.BookNetwork.models.common.Feedback;
import se2.BookNetwork.models.common.User;
import se2.BookNetwork.repositories.BookRepository;
import se2.BookNetwork.repositories.FeedbackRepository;
import se2.BookNetwork.utils.BookHelper;

@Service
@RequiredArgsConstructor
public class FeedbackService implements IFeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    @Override
    public Integer saveFeedback(FeedbackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new EntityNotFoundException());

        User user = (User) connectedUser.getPrincipal();

        if (BookHelper.isLocked(book)) {
            throw new UnauthorizedOperationException("This book is currently locked!");
        }

        if (BookHelper.isOwnedByThisUser(book, user)) {
            throw new UnauthorizedOperationException("You cannot feedback to your own book!");
        }

        Feedback feedback = feedbackMapper.toFeedBack(request);

        return feedbackRepository.save(feedback).getId();
    }

    @Override
    public PageResponse<FeedbackResponse> findAllFeedbackByBookId(
            Integer bookId,
            Authentication connectedUser,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedbackRepository.findAllFeedbackByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(fb -> feedbackMapper.toFeedbackResponse(fb, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast());
    }

}
