package se2.BookNetwork.core.mappers;

import org.springframework.stereotype.Service;

import se2.BookNetwork.core.requests.BookRequest;
import se2.BookNetwork.core.responses.BookResponse;
import se2.BookNetwork.core.responses.BorrowedBookResponse;
import se2.BookNetwork.models.common.Book;
import se2.BookNetwork.models.common.BookTransaction;
import se2.BookNetwork.utils.FileUtils;

@Service
public class BookMapper {

    public Book toBook(BookRequest bookRequest) {
        return Book.builder()
                .title(bookRequest.title())
                .authorName(bookRequest.authorName())
                .isbn(bookRequest.isbn())
                .synopsis(bookRequest.synopsis())
                .archived(false)
                .shareable(bookRequest.shareable())
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
                .cover(FileUtils.readFileFromLocation(book.getBookCover()))
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