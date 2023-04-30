package com.sanvalero.aa2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkThumbnail {
    String lqip;
    int width;
    int height;
    String alt_text;
}
