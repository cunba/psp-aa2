package com.svalero.aa2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gallery {
    int id;
    String api_model;
    String api_link;
    String title;
    float latitude;
    float longitude;
    int tgn_id;
    boolean is_closed;
    String number;
    String floor;
    String latlon;
    String source_updated_at;
    String updated_at;
    String timestamp;

    @Override
    public String toString() {
        return title;
    }
}
