package se2.BookNetwork.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.requests.FeedbackRequest;
import se2.BookNetwork.interfaces.IBookService;
import se2.BookNetwork.interfaces.IFeedbackService;

@Controller
@RequestMapping(value = "/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final IBookService bookService;
    private final IFeedbackService feedbackService;

    @GetMapping("/{bookId}")
    public String getReturnForm(
            @PathVariable Integer bookId,
            Authentication currentUser,
            Model model) {
        var book = bookService.getBookById(bookId);
        model.addAttribute("selectedBook", book);
        model.addAttribute("feedbackRequest", new FeedbackRequest());

        model.addAttribute("title", "Book Return");
        return "feedback/form";
    }

    @PostMapping(value = "/save")
    public String saveFeedback(
            @Valid FeedbackRequest request,
            Authentication currentUser,
            Model model) {
        bookService.returnBook(request.getBookId(), currentUser);
        feedbackService.saveFeedback(request, currentUser);
        return "redirect:/books/my-borrowed-books";
    }

    @GetMapping("/{feedbackId}/edit")
    public String getEditForm(
            @PathVariable Integer feedbackId,
            Authentication currentUser,
            Model model) {
        var feedback = feedbackService.getFeedbackById(feedbackId, currentUser);
        var book = bookService.getBookById(feedback.getBookId());
        var feedbackRequest = FeedbackRequest.builder()
                .bookId(feedback.getBookId())
                .id(feedbackId)
                .comment(feedback.getComment())
                .rate(feedback.getRate())
                .build();

        model.addAttribute("feedback", feedback);
        model.addAttribute("feedbackRequest", feedbackRequest);
        model.addAttribute("selectedBook", book);

        model.addAttribute("title", "Book Return");
        return "feedback/form";
    }

    @PostMapping(value = "/update")
    public String updateFeedback(
            @Valid FeedbackRequest request,
            Authentication currentUser) {
        feedbackService.updateFeedback(request, currentUser);
        return "redirect:/books/my-borrowed-books";
    }
}
