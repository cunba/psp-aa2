package com.sanvalero.aa2.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exhibition {
    int id;
    String api_model;
    String api_link;
    String title;
    Boolean is_featured;
    String short_description;
    String web_url;
    String image_url;
    String status;
    LocalDateTime aic_start_at;
    LocalDateTime aid_end_at;
    int gallery_id;
    String gallery_title;
    List<Integer> artwork_ids;
    List<String> artwork_titles;
    List<Integer> artist_ids;
    List<Integer> site_ids;
    UUID image_id;
    List<UUID> alt_image_ids;
    List<Integer> document_ids;
    String suggest_autocomplete_boosted;
    Object suggest_autocomplete_all;
    LocalDateTime source_updated_at;
    LocalDateTime updated_at;
    LocalDateTime timestamp;
}
