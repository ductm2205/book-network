package se2.BookNetwork.models.common;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import se2.BookNetwork.models.Item;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class BookTransaction extends Item {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "book_id")
    private Book book;

    private boolean isReturned;
    private boolean isReturnApproved;
}