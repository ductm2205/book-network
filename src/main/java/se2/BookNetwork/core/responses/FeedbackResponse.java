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
public class FeedbackResponse {

    private Integer id;

    private Double rate;

    private String comment;

    private String owner;

    private Integer bookId;

    private boolean isOwnFeedback;
}