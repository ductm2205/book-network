package se2.BookNetwork.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.PageResponse;
import se2.BookNetwork.core.requests.BookRequest;
import se2.BookNetwork.core.responses.BookResponse;
import se2.BookNetwork.core.responses.BorrowedBookResponse;
import se2.BookNetwork.exceptions.UnauthorizedOperationException;
import se2.BookNetwork.interfaces.IBookService;

@Controller
@RequestMapping(value = "/books")
@RequiredArgsConstructor
public class BookController {
    private final IBookService bookService;

    @GetMapping()
    public String getAllBooks(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            Authentication connectedUser,
            Model model) {

        PageResponse<BookResponse> pageResponse = bookService.findAllBooks(pageNumber, pageSize, connectedUser);

        model.addAttribute("books", pageResponse.getElements());
        model.addAttribute("currentPage", pageResponse.getPageNumber());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalItems", pageResponse.getTotalElements());
        model.addAttribute("pageSize", pageSize);

        return "book/index";
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable Integer id, Model model) {
        BookResponse book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "book/detail";
    }

    @GetMapping("/my-books")
    public String getMyBooks(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            Authentication authentication,
            Model model) {
        PageResponse<BookResponse> pageResponse = bookService.findAllBooksOwnedByUser(pageNumber, pageSize,
                authentication);

        model.addAttribute("books", pageResponse.getElements());
        model.addAttribute("currentPage", pageResponse.getPageNumber());
        model.addAttribute("pageSize", pageResponse.getPageSize());
        model.addAttribute("totalItems", pageResponse.getTotalElements());
        model.addAttribute("totalPages", pageResponse.getTotalPages());

        return "book/mine";
    }

    @GetMapping("/borrowed")
    public String getBorrowedBooks(@RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            Model model,
            Authentication authentication) {

        PageResponse<BorrowedBookResponse> borrowedBooks = bookService.findAllBorrowedBooksByUser(pageNumber, pageSize,
                authentication);

        model.addAttribute("books", borrowedBooks.getElements());
        model.addAttribute("currentPage", borrowedBooks.getPageNumber());
        model.addAttribute("totalPages", borrowedBooks.getTotalPages());
        model.addAttribute("pageSize", borrowedBooks.getPageSize());
        model.addAttribute("totalItems", borrowedBooks.getTotalElements());

        return "book/borrowed";
    }

    @GetMapping("/returned")
    public String getReturnedBooks(@RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            Model model,
            Authentication authentication) {

        PageResponse<BorrowedBookResponse> returnedBooks = bookService.findAllReturnedBooksByUser(pageNumber, pageSize,
                authentication);

        model.addAttribute("books", returnedBooks.getElements());
        model.addAttribute("currentPage", returnedBooks.getPageNumber());
        model.addAttribute("totalPages", returnedBooks.getTotalPages());
        model.addAttribute("pageSize", returnedBooks.getPageSize());
        model.addAttribute("totalItems", returnedBooks.getTotalElements());

        return "book/returned";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("bookRequest", new BookRequest(null, "", "", "", "", false));
        return "book/add";
    }

    @PostMapping("/save")
    public String saveBook(
            @Valid BookRequest bookRequest,
            BindingResult bindingResult,
            Authentication connectedUser,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "book/add";
        }

        Integer savedBookId = this.bookService.save(bookRequest, connectedUser);
        redirectAttributes.addFlashAttribute("successMessage", "Book saved successfully with ID: " + savedBookId);

        return "redirect:/books";
    }

    @GetMapping("/{bookId}/toggleShareable")
    public String toggleShareableStatus(@PathVariable("bookId") Integer bookId, Authentication authentication,
            Model model) {
        try {
            Integer updatedBookId = bookService.updateShareableStatus(bookId, authentication);
            model.addAttribute("bookId", updatedBookId);
            return "redirect:/books/" + updatedBookId;
        } catch (UnauthorizedOperationException | EntityNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error";
        }
    }

    @GetMapping("/{bookId}/toggleArchieve")
    public String toggleArchieveStatus(@PathVariable("bookId") Integer bookId, Authentication authentication,
            Model model) {
        try {
            Integer updatedBookId = bookService.updateArchivedStatus(bookId, authentication);
            model.addAttribute("bookId", updatedBookId);
            return "redirect:/books/" + updatedBookId;
        } catch (UnauthorizedOperationException | EntityNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error";
        }
    }
}
