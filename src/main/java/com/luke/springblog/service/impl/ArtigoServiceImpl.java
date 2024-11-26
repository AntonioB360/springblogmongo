package com.luke.springblog.service.impl;

import com.luke.springblog.model.Artigo;
import com.luke.springblog.model.Autor;
import com.luke.springblog.repository.ArtigoRepository;
import com.luke.springblog.repository.AutorRepository;
import com.luke.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArtigoServiceImpl implements ArtigoService {

    private final MongoTemplate mongoTemplate;

    public ArtigoServiceImpl (MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    };

    @Autowired
    private ArtigoRepository artigoRepository;

    @Autowired
    private AutorRepository autorRepository;


    @Override
    public List<Artigo> obterTodos() {
        return this.artigoRepository.findAll();
    }

    @Override
    public Artigo obterPorCodigo(String codigo) {
        return this.artigoRepository
                .findById(codigo)
                .orElseThrow(()-> new IllegalArgumentException("Artigo não existe"));
    }

    @Override
    public Artigo criar(Artigo artigo) {

        if(artigo.getAutor().getCodigo() != null){

            Autor autor = this.autorRepository
                    .findById(artigo.getAutor().getCodigo())
                    .orElseThrow(()-> new IllegalArgumentException("Autor inexistente!!!"));

            artigo.setAutor(autor);
        }else {
            artigo.setAutor(null);
        }

        return this.artigoRepository.save(artigo);
    }

    @Override
    public List<Artigo> findByDataGreaterThan(LocalDateTime data) {

        Query query = new Query(Criteria.where("data").gt(data));

        return mongoTemplate.find(query,Artigo.class);
    }

    @Override
    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status) {

        Query query = new Query(Criteria.where("data")
                .is(data).and("status").is(status));

        return mongoTemplate.find(query,Artigo.class);
    }

    @Override
    public void atualizar(Artigo updateArtigo) {

        this.artigoRepository.save(updateArtigo);
    }

    @Override
    public void atualizarArtigo(String id, String novaUrl) {

        Query query = new Query(Criteria.where("_id").is(id));

        Update update = new Update().set("url", novaUrl);

        this.mongoTemplate.updateFirst(query, update, Artigo.class);
    }

    @Override
    public void deleteById(String id){

        artigoRepository.deleteById(id);

    }

    @Override
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data) {
      return  this.artigoRepository.
              findByStatusAndDataGreaterThan(status,data);
    }

    @Override
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate) {
        return artigoRepository.obterArtigoPorDataHora(de, ate);
    }

    @Override
    public List<Artigo> encontrarArtigosComplexos(Integer status, LocalDateTime data, String titulo) {

        Criteria criteria = new Criteria();

        criteria.and("data").lte(data);
        if (status != null) {
            criteria.and("status").is(status);
        }

        if (titulo != null && !titulo.isEmpty() ) {
            criteria.and("titulo").regex(titulo,"i");
        }

        Query query = new Query(criteria);

        return mongoTemplate.find(query, Artigo.class);


    }

    @Override
    public Page<Artigo> listaArtigos(Pageable pageable) {
        return artigoRepository.findAll(pageable);
    }


}
