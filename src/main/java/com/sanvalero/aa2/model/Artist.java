package com.sanvalero.aa2.model;

import java.time.LocalDateTime;
import java.util.List;

public class Artist {
    int id;
    String api_model;
    String api_link;
    String title;
    String sort_title;
    List<String> alt_titles;
    Boolean is_artist;
    LocalDateTime birth_date;
    LocalDateTime death_date;
    String description;
    int ulan_id;
    Object suggest_autocomplete_all;
    LocalDateTime source_updated_at;
    LocalDateTime updated_at;
    LocalDateTime timestamp;
}
