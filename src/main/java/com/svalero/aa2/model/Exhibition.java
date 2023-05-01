package com.svalero.aa2.model;

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
    String aic_start_at;
    String aid_end_at;
    int gallery_id;
    String gallery_title;
    List<Integer> artwork_ids;
    List<String> artwork_titles;
    List<Integer> artist_ids;
    List<Integer> site_ids;
    UUID image_id;
    List<UUID> alt_image_ids;
    List<Integer> document_ids;
    String source_updated_at;
    String updated_at;
    String timestamp;
}
