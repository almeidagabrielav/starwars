package com.empresa.starwars.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder

@Document(collection = "planets")
public class Planet {

    @Id
    private String id;
    private String name;
    private String climate;
    private String terrain;
}
