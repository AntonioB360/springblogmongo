package com.luke.springblog.service;

import com.luke.springblog.model.Autor;

public interface AutorService {

    public Autor criar(Autor autor);

    public Autor obterPorCodigo(String codigo);

}
