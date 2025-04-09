package se2.BookNetwork.core.mappers;

import org.springframework.stereotype.Service;

import se2.BookNetwork.core.requests.BookRequest;
import se2.BookNetwork.core.responses.BookResponse;
import se2.BookNetwork.core.responses.BorrowedBookResponse;
import se2.BookNetwork.models.common.Book;
import se2.BookNetwork.models.common.BookTransaction;

@Service
public class BookMapper {

    public Book toBook(BookRequest bookRequest) {
        return Book.builder()
                .title(bookRequest.getTitle())
                .authorName(bookRequest.getAuthorName())
                .isbn(bookRequest.getIsbn())
                .synopsis(bookRequest.getSynopsis())
                .archived(false)
                .shareable(bookRequest.isShareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .ownerName(book.getOwner().getFullName())
                .cover(book.getBookCover())
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransaction transaction) {
        var book = transaction.getBook();
        return BorrowedBookResponse.builder()
                .id(transaction.getUser().getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .ownerName(book.getOwner().getFullName())
                .isReturned(transaction.isReturned())
                .isReturnApproved(transaction.isReturnApproved())
                .build();
    }
}