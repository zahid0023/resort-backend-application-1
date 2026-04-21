package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.auth.service.UserService;
import com.example.resortbackendapplication1.commons.dto.request.ImageRequest;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.service.ImageUploadService;
import com.example.resortbackendapplication1.dto.request.resorts.CreateResortRequest;
import com.example.resortbackendapplication1.dto.request.resorts.UpdateResortRequest;
import com.example.resortbackendapplication1.model.entity.CityEntity;
import com.example.resortbackendapplication1.model.entity.CountryEntity;
import com.example.resortbackendapplication1.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.service.*;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/resorts")
public class ResortController {

    private final ResortService resortService;
    private final UserService userService;
    private final ResortAccessTypeService resortAccessTypeService;
    private final CountryService countryService;
    private final CityService cityService;
    private final ImageUploadService imageUploadService;

    public ResortController(
            ResortService resortService,
            UserService userService,
            ResortAccessTypeService resortAccessTypeService,
            CountryService countryService,
            CityService cityService,
            ImageUploadService imageUploadService
    ) {
        this.resortService = resortService;
        this.userService = userService;
        this.resortAccessTypeService = resortAccessTypeService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createResort(@RequestPart("data") CreateResortRequest request,
                                          @RequestPart("images") List<MultipartFile> images) {
        UserEntity userEntity = userService.getAuthenticatedUserEntity();
        ResortAccessTypeEntity accessTypeEntity = resortAccessTypeService.getResortAccessTypeByCode("OWNER");
        CountryEntity countryEntity = countryService.getCountryEntity(request.getCountryId());
        CityEntity cityEntity = cityService.getCityEntity(request.getCityId());

        List<ImageRequest> imageRequests = imageUploadService.uploadAll(
                images,
                request.getImages(),
                request.getConfigRequest().getProvider(),
                request.getConfigRequest().getConfig()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortService.createResort(request, userEntity, accessTypeEntity, countryEntity, cityEntity, request.getConfigRequest(), imageRequests));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResort(@PathVariable Long id) {
        return ResponseEntity.ok(resortService.getResort(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> getAllResorts(@ParameterObject PaginatedRequest request) {
        UserEntity userEntity = userService.getAuthenticatedUserEntity();
        Pageable pageable = request.toPageable(Set.of("id", "name"));
        return ResponseEntity.ok(resortService.getAllResorts(userEntity, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResort(@PathVariable Long id, @RequestBody UpdateResortRequest request) {
        return ResponseEntity.ok(resortService.updateResort(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResort(@PathVariable Long id) {
        return ResponseEntity.ok(resortService.deleteResort(id));
    }
}
