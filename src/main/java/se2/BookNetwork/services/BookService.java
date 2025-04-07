package se2.BookNetwork.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.PageResponse;
import se2.BookNetwork.core.mappers.BookMapper;
import se2.BookNetwork.core.requests.BookRequest;
import se2.BookNetwork.core.responses.BookResponse;
import se2.BookNetwork.core.responses.BorrowedBookResponse;
import se2.BookNetwork.exceptions.UnauthorizedOperationException;
import se2.BookNetwork.interfaces.IBookService;
import se2.BookNetwork.models.common.Book;
import se2.BookNetwork.models.common.BookTransaction;
import se2.BookNetwork.models.common.User;
import se2.BookNetwork.repositories.BookRepository;
import se2.BookNetwork.repositories.BookTransactionRepository;
import se2.BookNetwork.utils.BookHelper;
import se2.BookNetwork.utils.BookSpecification;

@Service
@RequiredArgsConstructor
public class BookService implements IBookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionRepository bookTransactionRepository;

    @Override
    public Integer save(BookRequest request, Authentication connectedUser) {
        Object principal = connectedUser.getPrincipal();
        System.out.println("Principal class: " + principal.getClass().getName());
        if (principal instanceof User user) {
            var book = bookMapper.toBook(request);
            book.setOwner(user);
            Book savedBook = bookRepository.save(book);
            System.out.println(
                    "Saved book ID: " + savedBook.getId() + ", type: " + savedBook.getId().getClass().getName());
            return savedBook.getId();
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
        }
    }

    @Override
    public BookResponse getBookById(Integer id) {
        return bookRepository.findById(id).map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public PageResponse<BookResponse> findAllBooks(int pageNumber, int pageSize, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());

        List<BookResponse> bookResponse = books.stream().map(bookMapper::toBookResponse).toList();

        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast());
    }

    @Override
    public PageResponse<BookResponse> findAllBooksOwnedByUser(int pageNumber, int pageSize,
            Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);

        List<BookResponse> bookResponse = books.stream().map(bookMapper::toBookResponse).toList();

        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast());
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllBorrowedBooksByUser(int pageNumber, int pageSize,
            Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<BookTransaction> borrowedBooks = bookTransactionRepository.findAllBorrowedBooks(user.getId(), pageable);

        List<BorrowedBookResponse> borrowedBooksResponse = borrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                borrowedBooksResponse,
                borrowedBooks.getNumber(),
                borrowedBooks.getSize(),
                borrowedBooks.getTotalElements(),
                borrowedBooks.getTotalPages(),
                borrowedBooks.isFirst(),
                borrowedBooks.isLast());
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllReturnedBooksByUser(int pageNumber, int pageSize,
            Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<BookTransaction> returneds = bookTransactionRepository.findAllReturnedBooks(user.getId(), pageable);

        List<BorrowedBookResponse> responses = returneds.stream().map(bookMapper::toBorrowedBookResponse).toList();
        return new PageResponse<>(
                responses,
                returneds.getNumber(),
                returneds.getSize(),
                returneds.getTotalElements(),
                returneds.getTotalPages(),
                returneds.isFirst(),
                returneds.isLast());
    }

    @Override
    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException());
        User user = (User) connectedUser.getPrincipal();

        if (!BookHelper.isOwnedByThisUser(book, user)) {
            throw new UnauthorizedOperationException("You are not the owner of this book");
        }

        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return book.getId();
    }

    @Override
    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        throw new UnsupportedOperationException("Unimplemented method 'updateArchivedStatus'");
    }

    @Override
    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        throw new UnsupportedOperationException("Unimplemented method 'borrowBook'");
    }

    @Override
    public Integer returnBook(Integer bookId, Authentication connectedUser) {
        throw new UnsupportedOperationException("Unimplemented method 'returnBook'");
    }

    @Override
    public Integer approveReturn(Integer bookId, Authentication connectedUser) {
        throw new UnsupportedOperationException("Unimplemented method 'approveReturn'");
    }

    @Override
    public void uploadBookCover(MultipartFile file, Integer bookId, Authentication connectedUser) {
        throw new UnsupportedOperationException("Unimplemented method 'uploadBookCover'");
    }

}
