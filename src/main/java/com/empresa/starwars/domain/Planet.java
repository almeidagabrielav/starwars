package com.empresa.starwars.domain;

import lombok.Builder;
import lombok.Data;

@Data //Criar Get Set
@Builder //Padrao de Projeto

public class Planet {
    private int Id;
    private String Name;
    private String Clime;
    private String Ground;
}
