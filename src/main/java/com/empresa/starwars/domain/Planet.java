package com.empresa.starwars.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Data
@Builder

@Document(collection = "planets")
public class Planet implements Serializable {

    @Id
    private String id;
    @Indexed
    private String name;
    private String climate;
    private String terrain;
}
