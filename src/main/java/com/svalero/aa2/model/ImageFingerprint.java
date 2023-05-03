package com.svalero.aa2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageFingerprint {
    String ahash;
    String dhash;
    String phash;
    String whash;
}
