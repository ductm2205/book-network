package se2.BookNetwork.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.PageResponse;
import se2.BookNetwork.interfaces.IFavouriteBookService;
import se2.BookNetwork.models.common.FavouriteBook;
import se2.BookNetwork.repositories.BookRepository;
import se2.BookNetwork.repositories.FavouriteBookRepository;
import se2.BookNetwork.repositories.FavouriteRepository;

@Service
@RequiredArgsConstructor
public class FavouriteBookService implements IFavouriteBookService {

        private final FavouriteBookRepository favouriteBookRepository;
        private final FavouriteRepository favouriteRepository;
        private final BookRepository bookRepository;

        @Override
        public Integer addFavouriteBook(Integer bookId, Integer favouriteId) {
                var favourite = favouriteRepository.findById(favouriteId)
                                .orElseThrow(() -> new EntityNotFoundException("Favourite not found!"));

                var book = bookRepository.findById(bookId)
                                .orElseThrow(() -> new EntityNotFoundException("Book not found!"));

                if (favouriteBookRepository.isBookFavoured(favouriteId, bookId)) {
                        var favour = favouriteBookRepository.findByFavouriteIdAndBookId(favouriteId, bookId)
                                        .orElseThrow(() -> new EntityNotFoundException("Favourite book not found!"));
                        favour.setDeletedAt(null);
                        favouriteBookRepository.save(favour);
                        return favour.getId();
                }

                var favouriteBook = FavouriteBook.builder()
                                .book(book)
                                .favourite(favourite)
                                .build();

                return favouriteBookRepository.save(favouriteBook).getId();
        }

        @Override
        public Integer removeFavouriteBook(Integer bookId, Integer favouriteId) {
                var favourite = favouriteRepository.findById(favouriteId)
                                .orElseThrow(() -> new EntityNotFoundException("Favourite not found!"));

                var book = bookRepository.findById(bookId)
                                .orElseThrow(() -> new EntityNotFoundException("Book not found!"));

                var favour = favouriteBookRepository.findByFavouriteIdAndBookId(favourite.getId(), book.getId())
                                .orElseThrow(() -> new EntityNotFoundException("Favourite book not found!"));

                favour.setDeletedAt(LocalDateTime.now());
                favouriteBookRepository.save(favour);
                return 1;
        }

        @Override
        public PageResponse<FavouriteBook> getAllFavouriteBooks(int pageNumber, int pageSize, Integer favouriteId) {

                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

                Page<FavouriteBook> books = favouriteBookRepository.findAllByFavouriteId(favouriteId, pageable);

                List<FavouriteBook> bookResponse = books.toList();

                return new PageResponse<>(
                                bookResponse,
                                books.getNumber(),
                                books.getSize(),
                                books.getTotalElements(),
                                books.getTotalPages(),
                                books.isFirst(),
                                books.isLast());
        }

}
