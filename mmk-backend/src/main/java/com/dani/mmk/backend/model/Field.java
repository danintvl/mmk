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

    @Column(nullable=false, unique=false)
    private String name;

    @Column(nullable = false, unique = false)
    private String location;

    public Field(){}

    public Field(Long id, String name, String location){
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Long getId(){return this.id;}
    public String getName(){return this.name;}
    public String getLocation(){return this.location;}

    public void setId(Long id){this.id = id;}
    public void setName(String name){this.name = name;}
    public void setLocation(String location){this.location = location;}
}
