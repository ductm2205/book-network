package se2.BookNetwork.core.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class FeedbackRequest {
    @Positive(message = "200")
    @Size(min = 0, message = "201")
    @Size(max = 5, message = "202")
    private Double rate;

    @NotNull(message = "203")
    @NotEmpty(message = "203")
    @NotBlank(message = "203")
    private String comment;

    @NotNull(message = "204")
    private Integer bookId;
}