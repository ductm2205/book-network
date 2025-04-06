package se2.BookNetwork.core;

import java.util.List;

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
public class PageResponse<T> {
    private List<T> elements;

    private int pageNumber;

    private int pageSize;

    private long totalElements;

    private int totalPages;

    private boolean isFirst;

    private boolean isLast;
}