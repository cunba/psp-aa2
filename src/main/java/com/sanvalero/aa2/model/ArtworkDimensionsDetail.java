package com.sanvalero.aa2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkDimensionsDetail {
    float depth_cm;
    float depth_in;
    float width_cm;
    float width_in;
    float height_cm;
    float height_in;
    float diameter_cm;
    float diameter_in;
    String clarification;
}
