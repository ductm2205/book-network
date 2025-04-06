package se2.BookNetwork.models.common;

import jakarta.persistence.Entity;
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
public class Feedback extends Item{
    private Double rate;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}