package se2.BookNetwork.interfaces;

import se2.BookNetwork.core.PageResponse;
import se2.BookNetwork.models.common.FavouriteBook;

public interface IFavouriteBookService {
    Integer addFavouriteBook(Integer bookId, Integer favouriteId);

    Integer removeFavouriteBook(Integer bookId, Integer favouriteId);

    PageResponse<FavouriteBook> getAllFavouriteBooks(int pageNumber, int pageSize, Integer favouriteId);
}
