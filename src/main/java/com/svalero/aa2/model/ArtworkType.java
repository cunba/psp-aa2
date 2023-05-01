package com.svalero.aa2.model;

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
    String source_updated_at;
    String updated_at;
    String timestamp;
}