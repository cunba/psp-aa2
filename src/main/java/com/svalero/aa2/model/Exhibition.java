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
    List<UUID> document_ids;
    String source_updated_at;
    String updated_at;
    String timestamp;

    public String[] exportToCSV() {
        return new String[] {
                String.valueOf(id),
                api_model,
                api_link,
                title,
                String.valueOf(is_featured),
                short_description,
                web_url,
                image_url,
                status,
                aic_start_at,
                aid_end_at,
                String.valueOf(gallery_id),
                gallery_title,
                artwork_ids != null ? artwork_ids.toString() : null,
                artwork_titles != null ? artwork_titles.toString() : null,
                artist_ids != null ? artist_ids.toString() : null,
                site_ids != null ? site_ids.toString() : null,
                String.valueOf(image_id),
                alt_image_ids != null ? alt_image_ids.toString() : null,
                document_ids != null ? document_ids.toString() : null,
                source_updated_at,
                updated_at,
                timestamp
        };
    }
}
