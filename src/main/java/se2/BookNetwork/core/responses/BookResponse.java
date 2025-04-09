package se2.BookNetwork.core.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Integer id;

    private String title;

    private String authorName;

    private String isbn;

    private String synopsis;

    private String ownerName;

    // cover picture of the book
    private String cover;

    private double rate;

    private boolean archived;

    private boolean shareable;
}