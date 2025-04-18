package se2.BookNetwork.controllers;

import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.PageResponse;
import se2.BookNetwork.core.constants.PaginateConstants;
import se2.BookNetwork.core.requests.BookRequest;
import se2.BookNetwork.core.responses.BookResponse;
import se2.BookNetwork.core.responses.BorrowedBookResponse;
import se2.BookNetwork.exceptions.UnauthorizedOperationException;
import se2.BookNetwork.interfaces.IBookService;
import se2.BookNetwork.interfaces.IFavouriteBookService;
import se2.BookNetwork.interfaces.IFavouriteService;
import se2.BookNetwork.interfaces.IFeedbackService;
import se2.BookNetwork.interfaces.IFileService;
import se2.BookNetwork.models.common.User;

@Controller
@RequestMapping(value = "/books")
@RequiredArgsConstructor
public class BookController {
    private final IBookService bookService;
    private final IFeedbackService feedbackService;
    private final IFileService fileService;
    private final IFavouriteBookService favouriteBookService;
    private final IFavouriteService favouriteService;

    @GetMapping()
    public String getAllBooks(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = PaginateConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            Authentication connectedUser,
            Model model) {
        PageResponse<BookResponse> pageResponse;
        if (connectedUser != null) {
            pageResponse = bookService.findAllBooks(pageNumber, pageSize, connectedUser);
        } else {
            pageResponse = bookService.getAllPaginatedBooks(pageNumber, pageSize);
        }

        model.addAttribute("books", pageResponse.getElements());
        model.addAttribute("currentPage", pageResponse.getPageNumber());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalItems", pageResponse.getTotalElements());
        model.addAttribute("pageSize", pageSize);

        model.addAttribute("activeTab", "books");
        model.addAttribute("title", "Books");

        return "book/index";
    }

    @GetMapping("/search")
    public String searchBooks(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = PaginateConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "ajax", defaultValue = "false", required = false) boolean isAjaxRequest,
            Model model) {
        var books = bookService.searchBooks(query, pageNumber, pageSize);
        model.addAttribute("books", books.getElements());
        model.addAttribute("currentPage", books.getPageNumber());
        model.addAttribute("totalPages", books.getTotalPages());
        model.addAttribute("totalItems", books.getTotalElements());
        model.addAttribute("pageSize", pageSize);

        model.addAttribute("query", query);
        model.addAttribute("activeTab", "books");
        model.addAttribute("title", "Books");
        return "book/index";
    }

    @GetMapping("/{id}")
    public String getBookById(
            @PathVariable Integer id,
            Model model,
            Authentication currentUser,
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = PaginateConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            RedirectAttributes redirectAttributes) {
        BookResponse book = bookService.getBookById(id);
        model.addAttribute("book", book);

        var feedbacks = this.feedbackService.findAllFeedbackByBookId(id, currentUser, pageNumber, pageSize);
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", feedbacks.getTotalPages());

        model.addAttribute("title", "Book Detail");
        return "book/detail";
    }

    @GetMapping("/my-books")
    public String getMyBooks(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = PaginateConstants.DEFAULT_PAGE_SIZE) int pageSize,
            Authentication authentication,
            Model model) {
        PageResponse<BookResponse> pageResponse = bookService.findAllBooksOwnedByUser(pageNumber, pageSize,
                authentication);

        model.addAttribute("books", pageResponse.getElements());
        model.addAttribute("currentPage", pageResponse.getPageNumber());
        model.addAttribute("pageSize", pageResponse.getPageSize());
        model.addAttribute("totalItems", pageResponse.getTotalElements());
        model.addAttribute("totalPages", pageResponse.getTotalPages());

        model.addAttribute("title", "My Books");
        model.addAttribute("activeTab", "my-books");
        return "book/mine";
    }

    @GetMapping("/my-borrowed-books")
    public String getBorrowedBooks(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = PaginateConstants.DEFAULT_PAGE_SIZE) int pageSize,
            Model model,
            Authentication authentication) {

        PageResponse<BorrowedBookResponse> borrowedBooks = bookService.findAllBorrowedBooksByUser(pageNumber, pageSize,
                authentication);

        model.addAttribute("books", borrowedBooks.getElements());
        model.addAttribute("currentPage", borrowedBooks.getPageNumber());
        model.addAttribute("totalPages", borrowedBooks.getTotalPages());
        model.addAttribute("pageSize", borrowedBooks.getPageSize());
        model.addAttribute("totalItems", borrowedBooks.getTotalElements());

        model.addAttribute("activeTab", "my-borrowed-books");
        model.addAttribute("title", "My Borrowed Books");

        return "book/borrowed";
    }

    @GetMapping("/my-returned-books")
    public String getReturnedBooks(@RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = PaginateConstants.DEFAULT_PAGE_SIZE) int pageSize,
            Model model,
            Authentication authentication) {

        PageResponse<BorrowedBookResponse> returnedBooks = bookService.findAllReturnedBooksByUser(pageNumber, pageSize,
                authentication);

        model.addAttribute("books", returnedBooks.getElements());
        model.addAttribute("currentPage", returnedBooks.getPageNumber());
        model.addAttribute("totalPages", returnedBooks.getTotalPages());
        model.addAttribute("pageSize", returnedBooks.getPageSize());
        model.addAttribute("totalItems", returnedBooks.getTotalElements());

        model.addAttribute("activeTab", "my-returned-books");
        model.addAttribute("title", "My Returned Books");

        return "book/returned";
    }

    @GetMapping("/manage")
    public String showAddForm(Model model) {
        model.addAttribute("bookRequest", new BookRequest(null, "", "", "", "", false));
        model.addAttribute("title", "Add New Book");
        model.addAttribute("feedbacks", null);
        model.addAttribute("activeTab", "books");
        return "book/manage-book";
    }

    @PostMapping("/save")
    public String saveBook(
            @Valid BookRequest bookRequest,
            BindingResult bindingResult,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Authentication connectedUser,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList()));
            model.addAttribute("book", bookRequest); // Preserve form data
            model.addAttribute("title", bookRequest.getId() != null ? "Edit Book" : "Add New Book");
            model.addAttribute("activeTab", "books");
            return "book/manage-book";
        }

        // Save the book details
        Integer savedBookId = this.bookService.save(bookRequest, connectedUser);

        // Handle the cover upload if a file is provided
        if (file != null && !file.isEmpty()) {
            User user = (User) connectedUser.getPrincipal();
            String filePath = fileService.saveFile(file, user.getUsername());
            if (filePath != null) {
                bookService.uploadBookCover(file, savedBookId, connectedUser);
            } else {
                redirectAttributes.addFlashAttribute("message", "Book saved, but cover upload failed.");
                redirectAttributes.addFlashAttribute("level", "warning");
                return "redirect:/books/my-books";
            }
        }

        redirectAttributes.addFlashAttribute("message", "Book saved successfully!");
        redirectAttributes.addFlashAttribute("level", "success");
        return "redirect:/books/my-books";
    }

    @GetMapping("/{bookId}/manage")
    public String editBookPage(
            @PathVariable("bookId") Integer bookId,
            Model model,
            Authentication currentUser,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = PaginateConstants.DEFAULT_PAGE_SIZE) int pageSize) {
        var book = bookService.getBookById(bookId);
        if (book != null) {
            BookRequest bookRequest = new BookRequest(null, "", "", "", "", false);
            bookRequest.setId(book.getId());
            bookRequest.setTitle(book.getTitle());
            bookRequest.setAuthorName(book.getAuthorName());
            bookRequest.setIsbn(book.getIsbn());
            bookRequest.setSynopsis(book.getSynopsis());
            bookRequest.setShareable(book.isShareable());

            var feedbacks = this.feedbackService.findAllFeedbackByBookId(bookId, currentUser, pageNumber, pageSize);
            model.addAttribute("feedbacks", feedbacks);
            model.addAttribute("pageNumber", pageNumber);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("totalPages", feedbacks.getTotalPages());

            model.addAttribute("title", "Edit Book");
            model.addAttribute("activeTab", "books");
            model.addAttribute("bookRequest", bookRequest);
            model.addAttribute("book", book);

            if (model.containsAttribute("message")) {
                model.addAttribute("message", model.getAttribute("message"));
                model.addAttribute("level", model.getAttribute("level"));
            }
            return "book/manage-book";
        }
        return "redirect:/books/my-books";
    }

    @PostMapping("/{id}/update")
    public String updateBook(
            @PathVariable("id") Integer id,
            @Valid BookRequest bookRequest,
            BindingResult bindingResult,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Authentication connectedUser,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors()
                    .stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList()));
            model.addAttribute("bookRequest", bookRequest);
            model.addAttribute("title", "Edit Book");
            model.addAttribute("activeTab", "books");
            return "book/manage-book";
        }

        try {
            this.bookService.update(id, bookRequest, connectedUser);
        } catch (UnauthorizedOperationException | EntityNotFoundException ex) {
            System.out.println("Exception caught: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("message", ex.getMessage() + "!");
            redirectAttributes.addFlashAttribute("level", "error");
            return "redirect:/books/" + id + "/manage";
        }

        // Handle the cover upload if a file is provided
        if (file != null && !file.isEmpty()) {
            User user = (User) connectedUser.getPrincipal();
            String filePath = fileService.saveFile(file, user.getUsername());
            if (filePath != null) {
                bookService.uploadBookCover(file, id, connectedUser);
            } else {
                redirectAttributes.addFlashAttribute("message", "Book saved, but cover upload failed.");
                redirectAttributes.addFlashAttribute("level", "warning");
                return "redirect:/books/my-books";
            }
        }

        redirectAttributes.addFlashAttribute("message", "Book updated successfully!");
        redirectAttributes.addFlashAttribute("level", "success");
        return "redirect:/books/my-books";
    }

    @GetMapping("/{bookId}/share")
    public String toggleShareableStatus(
            @PathVariable("bookId") Integer bookId,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            Integer updatedBookId = bookService.updateShareableStatus(bookId, authentication);
            model.addAttribute("bookId", updatedBookId);
            redirectAttributes.addFlashAttribute("message", "Book's sharable status updated successfully!");
            redirectAttributes.addFlashAttribute("level", "success");
        } catch (UnauthorizedOperationException | EntityNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            redirectAttributes.addFlashAttribute("message", ex.getMessage() + "!");
            redirectAttributes.addFlashAttribute("level", "error");
        }
        return "redirect:/books/my-books";
    }

    @GetMapping("/{bookId}/archive")
    public String toggleArchieveStatus(@PathVariable("bookId") Integer bookId, Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            Integer updatedBookId = bookService.updateArchivedStatus(bookId, authentication);
            model.addAttribute("bookId", updatedBookId);
            redirectAttributes.addFlashAttribute("message", "Book's archived status updated successfully!");
            redirectAttributes.addFlashAttribute("level", "success");
        } catch (UnauthorizedOperationException | EntityNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            redirectAttributes.addFlashAttribute("message", ex.getMessage() + "!");
            redirectAttributes.addFlashAttribute("level", "error");
        }
        return "redirect:/books/my-books";
    }

    @SuppressWarnings("unused")
    @GetMapping("/{bookId}/borrow")
    public String borrowBook(
            @PathVariable("bookId") Integer bookId,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        try {
            Integer bookTransactionId = bookService.borrowBook(bookId, authentication);
            redirectAttributes.addFlashAttribute("message", "Book borrowed successfully!");
            redirectAttributes.addFlashAttribute("level", "success");
        } catch (UnauthorizedOperationException | EntityNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage() + "!");
            redirectAttributes.addFlashAttribute("level", "error");
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/approve-return/{bookId}")
    public String approveReturn(
            @PathVariable("bookId") Integer bookId,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        try {
            bookService.approveReturn(bookId, authentication);
            redirectAttributes.addFlashAttribute("message", "Book return approved successfully!");
            redirectAttributes.addFlashAttribute("level", "success");
        } catch (UnauthorizedOperationException | EntityNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage() + "!");
            redirectAttributes.addFlashAttribute("level", "error");
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/favourites")
    public String getAllFavouriteBooks(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = PaginateConstants.DEFAULT_PAGE_SIZE) int pageSize,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            Model model) {
        User user = (User) authentication.getPrincipal();
        var favourite = favouriteService.getById(user.getFavourite().getId());
        var books = favouriteBookService.getAllFavouriteBooks(pageNumber, pageSize, favourite.getId());

        model.addAttribute("books", books.getElements());
        model.addAttribute("currentPage", books.getPageNumber());
        model.addAttribute("totalPages", books.getTotalPages());
        model.addAttribute("pageSize", books.getPageSize());
        model.addAttribute("totalItems", books.getTotalElements());
        model.addAttribute("activeTab", "my-favourites");
        model.addAttribute("title", "My Favourite Books");
        return "book/favourite";
    }

    @GetMapping("/{bookId}/favourite/add")
    public String addToFavourite(
            @PathVariable("bookId") Integer bookId,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        User user = (User) authentication.getPrincipal();
        var favourite = favouriteService.getById(user.getFavourite().getId());
        try {
            favouriteBookService.addFavouriteBook(bookId, favourite.getId());
            redirectAttributes.addFlashAttribute("message", "Book added to favourite!");
            redirectAttributes.addFlashAttribute("level", "success");
        } catch (UnauthorizedOperationException | EntityNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage() + "!");
            redirectAttributes.addFlashAttribute("level", "error");
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/{bookId}/favourite/remove")
    public String removeFromFavourite(
            @PathVariable("bookId") Integer bookId,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        User user = (User) authentication.getPrincipal();
        var favourite = favouriteService.getById(user.getFavourite().getId());
        try {
            favouriteBookService.removeFavouriteBook(bookId, favourite.getId());
            redirectAttributes.addFlashAttribute("message", "Book removed from favourite!");
            redirectAttributes.addFlashAttribute("level", "success");
        } catch (UnauthorizedOperationException | EntityNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage() + "!");
            redirectAttributes.addFlashAttribute("level", "error");
        }
        return "redirect:" + request.getHeader("Referer");
    }
}
