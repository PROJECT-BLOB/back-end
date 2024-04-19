package com.codeit.blob.global.domain.test;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Test {
    @Id
    @GeneratedValue
    private Long id;
    private String test;
}
