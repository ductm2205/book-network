package se2.BookNetwork.utils;
import org.springframework.data.jpa.domain.Specification;

import se2.BookNetwork.models.common.Book;

public class BookSpecification {
    public static Specification<Book> withOwnerId(Integer id) {
        return (root, query, criteriaBuilder) -> (criteriaBuilder.equal(root.get("owner").get("id"), id));
    }
}