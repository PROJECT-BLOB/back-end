package com.codeit.blob.city.repository;

import com.codeit.blob.city.domain.City;
import com.codeit.blob.city.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityJpaRepository extends JpaRepository<City, Long> {

    Optional<City> findByCountryAndName(Country country, String name);

}
