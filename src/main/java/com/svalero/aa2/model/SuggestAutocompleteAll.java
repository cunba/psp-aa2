package com.svalero.aa2.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestAutocompleteAll {
    List<String> input;
    int weight;
    Context context;
}
