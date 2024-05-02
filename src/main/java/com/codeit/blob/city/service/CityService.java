package com.codeit.blob.city.service;

import com.codeit.blob.city.domain.City;
import com.codeit.blob.city.domain.Country;
import com.codeit.blob.city.repository.CityJpaRepository;
import com.codeit.blob.global.domain.Coordinate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityJpaRepository cityJpaRepository;

    @Transactional
    public City createCity(Country country, String cityName, Coordinate coordinate){
        City city = new City(country, cityName, coordinate);
        return cityJpaRepository.save(city);
    }

    public City findCityByCoordinate(Coordinate coordinate){
        return cityJpaRepository.findByCoordinate(coordinate).orElse(null);
    }
}
