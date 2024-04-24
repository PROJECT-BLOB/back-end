package com.codeit.blob.city.service;

import com.codeit.blob.city.domain.City;
import com.codeit.blob.city.domain.Country;
import com.codeit.blob.city.repository.CityJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityJpaRepository cityJpaRepository;

    @Transactional
    public City createCity(Country country, String cityName){
        City city = City.builder()
                .country(country)
                .name(cityName)
                .build();
        cityJpaRepository.save(city);
        return city;
    }

    public City findCityByCountryAndName(Country country, String cityName){
        return cityJpaRepository.findByCountryAndName(country, cityName).orElse(null);
    }
}
