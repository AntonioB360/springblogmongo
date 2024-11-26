package com.luke.springblog.service;

import com.luke.springblog.model.Artigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoService {

    public List<Artigo> obterTodos();

    public Artigo obterPorCodigo(String codigo);

    public Artigo criar(Artigo artigo);

    public List<Artigo> findByDataGreaterThan(LocalDateTime localDateTime);

    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status);

    public void atualizar (Artigo updateArtigo);

    public void atualizarArtigo (String id, String novaUrl);

    public void deleteById(String id);

    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);

    public List<Artigo> obterArtigoPorDataHora (LocalDateTime de, LocalDateTime ate);

    public List<Artigo> encontrarArtigosComplexos (Integer status, LocalDateTime data, String titulo);

    public Page<Artigo> listaArtigos(Pageable pageable);

}
