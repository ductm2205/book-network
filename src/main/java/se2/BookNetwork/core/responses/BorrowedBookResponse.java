package se2.BookNetwork.core.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BorrowedBookResponse {
    private Integer id;

    private String title;

    private String authorName;

    private String isbn;

    private String ownerName;

    private double rate;

    private boolean isReturned;

    private boolean isReturnApproved;
}