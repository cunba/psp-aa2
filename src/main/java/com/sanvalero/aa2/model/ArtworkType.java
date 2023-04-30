package com.sanvalero.aa2.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkType {
    int id;
    String api_model;
    String api_link;
    String title;
    int aat_id;
    LocalDateTime source_updated_at;
    LocalDateTime updated_at;
    LocalDateTime timestamp;
}