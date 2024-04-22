package com.codeit.blob.domain.post.repository;

import com.codeit.blob.domain.post.domain.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityJpaRepository extends JpaRepository<City, Long> {
}
