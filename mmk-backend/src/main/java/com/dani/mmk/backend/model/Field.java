package com.dani.mmk.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;import jakarta.validation.constraints.NotBlank;import jakarta.validation.constraints.Size;

@Entity
@Table(name = "fields")
public class Field{

    private static final int NAME_MAX_LENGTH = 200;
    private static final int LOCATION_MAX_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = NAME_MAX_LENGTH)
    @Column(nullable=false, length = NAME_MAX_LENGTH)
    private String name;

    @NotBlank
    @Size(max = LOCATION_MAX_LENGTH)
    @Column(nullable = false, length = LOCATION_MAX_LENGTH)
    private String location;

    public Field(){}

    public Field(String name, String location){
        this.name = name;
        this.location = location;
    }

    //Getters
    public Long getId(){return this.id;}
    public String getName(){return this.name;}
    public String getLocation(){return this.location;}

    //Setters
    public void setName(String name){this.name = name;}
    public void setLocation(String location){this.location = location;}
}
