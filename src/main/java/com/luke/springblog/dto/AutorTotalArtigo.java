package com.luke.springblog.dto;

import com.luke.springblog.model.Autor;
import lombok.Data;

@Data
public class AutorTotalArtigo {

    private Autor autor;
    private Long totalArtigos;



}
