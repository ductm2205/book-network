package se2.BookNetwork.core.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchRequest {
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
}
