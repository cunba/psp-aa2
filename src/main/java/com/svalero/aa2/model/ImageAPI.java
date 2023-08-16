package com.svalero.aa2.model;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageAPI {
    UUID id;
    UUID lake_guid;
    String api_model;
    String api_link;
    String title;
    String type;
    List<String> alt_text;
    String content;
    Boolean is_multimedia_resource;
    Boolean is_educational_resource;
    Boolean is_teacher_resource;
    String credit_line;
    String content_e_tag;
    String iiif_url;
    float width;
    float height;
    String lqip;
    float colorfulness;
    Color color;
    ImageFingerprint fingerprint;
    List<Integer> artwork_ids;
    List<String> artwork_titles;
    String source_updated_at;
    String updated_at;
    String timestamp;
}
