package se2.BookNetwork.interfaces;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import se2.BookNetwork.core.PageResponse;
import se2.BookNetwork.core.requests.BookRequest;
import se2.BookNetwork.core.responses.BookResponse;
import se2.BookNetwork.core.responses.BorrowedBookResponse;
import se2.BookNetwork.models.common.Book;

public interface IBookService {
        public Integer save(BookRequest request, Authentication connectedUser);

        public Integer update(Integer id, BookRequest request, Authentication connectedUser);

        public BookResponse getBookById(Integer id);

        public List<Book> getAllBooks();

        public PageResponse<BookResponse> findAllBooks(int pageNumber, int pageSize, Authentication connectedUser);

        public PageResponse<BookResponse> findAllBooksOwnedByUser(int pageNumber, int pageSize,
                        Authentication connectedUser);

        public PageResponse<BorrowedBookResponse> findAllBorrowedBooksByUser(int pageNumber, int pageSize,
                        Authentication connectedUser);

        public PageResponse<BorrowedBookResponse> findAllReturnedBooksByUser(int pageNumber, int pageSize,
                        Authentication connectedUser);

        public Integer updateShareableStatus(Integer bookId, Authentication connectedUser);

        public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser);

        public Integer borrowBook(Integer bookId, Authentication connectedUser);

        public Integer returnBook(Integer bookId, Authentication connectedUser);

        public Integer approveReturn(Integer bookId, Authentication connectedUser);

        public void uploadBookCover(MultipartFile file, Integer bookId, Authentication connectedUser);
}
