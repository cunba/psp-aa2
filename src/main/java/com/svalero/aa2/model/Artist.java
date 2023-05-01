package com.svalero.aa2.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Artist {
    int id;
    String api_model;
    String api_link;
    String title;
    String sort_title;
    List<String> alt_titles;
    Boolean is_artist;
    String birth_date;
    String death_date;
    String description;
    int ulan_id;
    String source_updated_at;
    String updated_at;
    String timestamp;
}
