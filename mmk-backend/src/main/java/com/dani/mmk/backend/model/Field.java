package com.dani.mmk.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "fields")
public class Field{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
