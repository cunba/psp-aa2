package com.svalero.aa2.model;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Artwork {
    int id;
    String api_model;
    String api_link;
    Boolean is_boosted;
    String title;
    List<String> alt_titles;
    ArtworkThumbnail thumbnail;
    String main_reference_number;
    Boolean has_not_been_viewed_much;
    float boost_rank;
    int date_start;
    int date_end;
    String date_display;
    String date_qualifier_title;
    int date_qualifier_id;
    String artist_display;
    String place_of_origin;
    String dimensions;
    List<ArtworkDimensionsDetail> dimenstions_detail;
    String medium_display;
    String inscriptions;
    String credit_line;
    String catalogue_display;
    String publication_history;
    String exhibition_history;
    String provenance_text;
    String edition;
    String publishing_verification_level;
    int internal_department_id;
    int fiscal_year;
    int fiscal_year_deaccession;
    Boolean is_public_domain;
    Boolean is_zoomable;
    float max_zoom_window_size;
    String copyright_notice;
    Boolean has_multimedia_resources;
    Boolean has_educational_resources;
    Boolean has_advanced_imaging;
    float colorfulness;
    Color color;
    float latitude;
    float longitude;
    String latlon;
    Boolean is_on_view;
    String on_loan_display;
    String gallery_title;
    int gallery_id;
    String artwork_type_title;
    int artwork_type_id;
    String department_title;
    String department_id;
    int artist_id;
    String artist_title;
    List<Integer> alt_artist_ids;
    List<Integer> artist_ids;
    List<String> artist_titles;
    List<String> category_ids;
    List<String> category_titles;
    List<String> term_titles;
    String style_id;
    String style_title;
    List<String> alt_style_ids;
    List<String> style_ids;
    List<String> style_titles;
    String classification_id;
    String classification_title;
    List<String> alt_classification_ids;
    List<String> classification_ids;
    List<String> classification_titles;
    String subject_id;
    List<String> alt_subject_ids;
    List<String> subjects_ids;
    List<String> subject_titles;
    String material_id;
    List<String> alt_material_ids;
    List<String> material_ids;
    List<String> material_titles;
    String technique_id;
    List<String> alt_technique_ids;
    List<String> technique_ids;
    List<String> technique_titles;
    List<String> theme_titles;
    UUID image_id;
    List<String> alt_image_ids;
    List<String> document_ids;
    List<UUID> sound_ids;
    List<UUID> video_ids;
    List<UUID> text_ids;
    List<Long> section_ids;
    List<String> section_titles;
    List<Integer> site_ids;
    String source_updated_at;
    String updated_at;
    String timestamp;

    public String[] exportToCSV() {
        return new String[] {
                String.valueOf(id),
                api_model,
                api_link,
                String.valueOf(is_boosted),
                title,
                alt_titles != null ? alt_titles.toString() : null,
                main_reference_number,
                String.valueOf(has_not_been_viewed_much),
                String.valueOf(boost_rank),
                String.valueOf(date_start),
                String.valueOf(date_end),
                date_display,
                date_qualifier_title,
                String.valueOf(date_qualifier_id),
                artist_display,
                place_of_origin,
                dimensions,
                medium_display,
                inscriptions,
                credit_line,
                catalogue_display,
                publication_history,
                exhibition_history,
                provenance_text,
                edition,
                publishing_verification_level,
                String.valueOf(internal_department_id),
                String.valueOf(fiscal_year),
                String.valueOf(fiscal_year_deaccession),
                String.valueOf(is_public_domain),
                String.valueOf(is_zoomable),
                String.valueOf(max_zoom_window_size),
                copyright_notice,
                String.valueOf(has_multimedia_resources),
                String.valueOf(has_educational_resources),
                String.valueOf(has_advanced_imaging),
                String.valueOf(colorfulness),
                String.valueOf(latitude),
                String.valueOf(longitude),
                latlon,
                String.valueOf(is_on_view),
                on_loan_display,
                gallery_title,
                String.valueOf(gallery_id),
                artwork_type_title,
                String.valueOf(artwork_type_id),
                department_title,
                department_id,
                String.valueOf(artist_id),
                artist_title,
                alt_artist_ids != null ? alt_artist_ids.toString() : null,
                artist_ids != null ? artist_ids.toString() : null,
                artist_titles != null ? artist_titles.toString() : null,
                category_ids != null ? category_ids.toString() : null,
                category_titles != null ? category_titles.toString() : null,
                term_titles != null ? term_titles.toString() : null,
                style_id,
                style_title,
                alt_style_ids != null ? alt_style_ids.toString() : null,
                style_ids != null ? style_ids.toString() : null,
                style_titles != null ? style_titles.toString() : null,
                classification_id,
                classification_title,
                alt_classification_ids != null ? alt_classification_ids.toString() : null,
                classification_ids != null ? classification_ids.toString() : null,
                classification_titles != null ? classification_titles.toString() : null,
                subject_id,
                alt_subject_ids != null ? alt_subject_ids.toString() : null,
                subjects_ids != null ? subjects_ids.toString() : null,
                subject_titles != null ? subject_titles.toString() : null,
                material_id,
                alt_material_ids != null ? alt_material_ids.toString() : null,
                material_ids != null ? material_ids.toString() : null,
                material_titles != null ? material_titles.toString() : null,
                technique_id,
                alt_technique_ids != null ? alt_technique_ids.toString() : null,
                technique_ids != null ? technique_ids.toString() : null,
                technique_titles != null ? technique_titles.toString() : null,
                theme_titles != null ? theme_titles.toString() : null,
                image_id != null ? image_id.toString() : null,
                alt_image_ids != null ? alt_image_ids.toString() : null,
                document_ids != null ? document_ids.toString() : null,
                sound_ids != null ? sound_ids.toString() : null,
                video_ids != null ? video_ids.toString() : null,
                text_ids != null ? text_ids.toString() : null,
                section_ids != null ? section_ids.toString() : null,
                section_titles != null ? section_titles.toString() : null,
                site_ids != null ? site_ids.toString() : null,
                source_updated_at,
                updated_at,
                timestamp
        };
    }
}