package se2.BookNetwork.services;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import se2.BookNetwork.interfaces.IBookTransactionService;
import se2.BookNetwork.repositories.BookTransactionRepository;

@Service
@RequiredArgsConstructor
public class BookTransactionService implements IBookTransactionService {

    private final BookTransactionRepository bookTransactionRepository;

    @Override
    public boolean isBookBorrowed(Integer bookId) {
        return bookTransactionRepository.isAlreadyBorrowed(bookId);
    }

    @Override
    public boolean isBookUnavailable(Integer bookId) {
        return bookTransactionRepository.isBookCurrentlyUnavailable(bookId);
    }

}
