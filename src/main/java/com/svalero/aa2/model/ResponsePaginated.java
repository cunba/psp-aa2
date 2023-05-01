package com.svalero.aa2.model;

import java.util.List;

import javafx.scene.control.Pagination;

public class ResponsePaginated<T> {
    Pagination pagination;
    List<T> data;
    ResponseInfo info;
    ResponseConfig config;
}
