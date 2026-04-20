package com.example.resortbackendapplication1.commons.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ImagesWrapperRequest {

    private List<ImageItemRequest> images = new ArrayList<>();
}