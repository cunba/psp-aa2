package com.svalero.aa2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePagination {
    int total;
    int limit;
    int offset;
    int total_pages;
    int current_page;
    String prev_url;
    String next_url;
}
