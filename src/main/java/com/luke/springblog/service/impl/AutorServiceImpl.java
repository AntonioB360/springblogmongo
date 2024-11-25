package com.luke.springblog.service.impl;

import com.luke.springblog.model.Autor;
import com.luke.springblog.repository.AutorRepository;
import com.luke.springblog.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServiceImpl implements AutorService {

    @Autowired
    private AutorRepository autorRepository;


    @Override
    public Autor criar(Autor autor) {
        return this.autorRepository.save(autor);
    }

    @Override
    public Autor obterPorCodigo(String codigo) {
        return this.autorRepository
                .findById(codigo)
                .orElseThrow(()-> new IllegalArgumentException("Autor não encontrado!"));
    }
}
