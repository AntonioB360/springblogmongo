package com.luke.springblog.dto;


import com.luke.springblog.model.Artigo;
import com.luke.springblog.model.Autor;
import lombok.Data;

@Data
public class ArtigoComAutorRequest {

    private Autor autor;

    private Artigo artigo;



}
