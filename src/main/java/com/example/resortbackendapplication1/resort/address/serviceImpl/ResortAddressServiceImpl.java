package com.example.resortbackendapplication1.resort.address.serviceImpl;

import com.example.resortbackendapplication1.address.model.dto.CityDto;
import com.example.resortbackendapplication1.address.model.dto.CountryDto;
import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.address.model.mapper.CityMapper;
import com.example.resortbackendapplication1.address.model.mapper.CountryMapper;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.address.dto.request.resortaddress.UpdateResortAddressRequest;
import com.example.resortbackendapplication1.resort.address.dto.response.resortaddresses.ResortAddressResponse;
import com.example.resortbackendapplication1.resort.address.model.dto.ResortAddressDto;
import com.example.resortbackendapplication1.resort.address.model.entity.ResortAddressEntity;
import com.example.resortbackendapplication1.resort.address.model.mapper.ResortAddressMapper;
import com.example.resortbackendapplication1.resort.address.repository.ResortAddressRepository;
import com.example.resortbackendapplication1.resort.address.service.ResortAddressService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResortAddressServiceImpl implements ResortAddressService {

    private final ResortAddressRepository resortAddressRepository;

    public ResortAddressServiceImpl(ResortAddressRepository resortAddressRepository) {
        this.resortAddressRepository = resortAddressRepository;
    }

    @Override
    public ResortAddressEntity getEntityByResortId(Long resortId) {
        return resortAddressRepository.findByResortEntity_IdAndIsActiveAndIsDeleted(resortId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortAddress not found for resort id: " + resortId));
    }

    @Override
    public ResortAddressResponse getByResortId(Long resortId) {
        ResortAddressEntity entity = getEntityByResortId(resortId);
        CountryDto country = CountryMapper.toDto(entity.getCountryEntity()).build();
        CityDto city = CityMapper.toDto(entity.getCityEntity()).build();
        ResortAddressDto dto = ResortAddressMapper.toDto(entity)
                .country(country)
                .city(city)
                .build();
        return new ResortAddressResponse(dto);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortAddressEntity entity,
                                  UpdateResortAddressRequest request,
                                  CountryEntity countryEntity,
                                  CityEntity cityEntity) {
        ResortAddressMapper.update(entity, request);
        entity.setCountryEntity(countryEntity);
        entity.setCityEntity(cityEntity);
        resortAddressRepository.save(entity);
        log.info("ResortAddress updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
