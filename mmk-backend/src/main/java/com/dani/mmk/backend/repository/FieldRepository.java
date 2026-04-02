package com.dani.mmk.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dani.mmk.backend.model.Field;

import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, Long>{

    Optional<Field> findByName(String name);

    Optional<Field> findByLocation(String location);
}