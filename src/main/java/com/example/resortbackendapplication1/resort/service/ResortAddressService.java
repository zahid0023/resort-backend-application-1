package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.dto.request.resortaddress.UpdateResortAddressRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortaddresses.ResortAddressResponse;
import com.example.resortbackendapplication1.resort.model.entity.ResortAddressEntity;

public interface ResortAddressService {

    ResortAddressEntity getEntityByResortId(Long resortId);

    ResortAddressResponse getByResortId(Long resortId);

    SuccessResponse update(ResortAddressEntity entity,
                           UpdateResortAddressRequest request,
                           CountryEntity countryEntity,
                           CityEntity cityEntity);
}
