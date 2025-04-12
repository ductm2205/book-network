package se2.BookNetwork.interfaces;

public interface IBookTransactionService {
    boolean isBookBorrowed(Integer bookId);

    boolean isBookUnavailable(Integer bookId);
}
