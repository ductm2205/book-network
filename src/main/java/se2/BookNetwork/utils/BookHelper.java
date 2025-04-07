package se2.BookNetwork.utils;

import java.util.Objects;

import se2.BookNetwork.models.common.Book;
import se2.BookNetwork.models.common.User;

public class BookHelper {
    public static boolean isLocked(Book book) {
        return book.isArchived() || !book.isShareable();
    }

    public static boolean isOwnedByThisUser(Book book, User user) {
        return Objects.equals(book.getCreatedBy(), user.getName());
    }
}