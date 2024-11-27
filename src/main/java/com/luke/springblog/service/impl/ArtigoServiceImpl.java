package com.luke.springblog.service.impl;

import com.luke.springblog.model.Artigo;
import com.luke.springblog.dto.ArtigoStatusCount;
import com.luke.springblog.model.Autor;
import com.luke.springblog.dto.AutorTotalArtigo;
import com.luke.springblog.repository.ArtigoRepository;
import com.luke.springblog.repository.AutorRepository;
import com.luke.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;


import java.time.LocalDate;
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

    @Autowired
    private MongoTransactionManager transactionManager;


    @Override
    public List<Artigo> obterTodos() {
        return this.artigoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Artigo obterPorCodigo(String codigo) {
        return this.artigoRepository
                .findById(codigo)
                .orElseThrow(()-> new IllegalArgumentException("Artigo não existe"));
    }

    @Override
    public ResponseEntity<?> criarArtigoComAutor(Artigo artigo, Autor autor) {

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute(status -> {

            try{
                autorRepository.save(autor);
                artigo.setData(LocalDateTime.now());
                artigo.setAutor(autor);
                artigoRepository.save(artigo);

            } catch (Exception ex) {
                status.setRollbackOnly();
                throw new RuntimeException("Erro ao criar uma artigo com autor: " + ex.getMessage());
            }
            return null;

        });

        return null;
    }







    /*@Override
    public ResponseEntity<?> criar(Artigo artigo) {
        if(artigo.getAutor().getCodigo() != null){
            Autor autor = this.autorRepository
                    .findById(artigo.getAutor().getCodigo())
                    .orElseThrow(()-> new IllegalArgumentException("Autor inexistente!!!"));
            artigo.setAutor(autor);
        }else {
            artigo.setAutor(null);
        }

        try{
             this.artigoRepository.save(artigo);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch(DuplicateKeyException dke) {

           return  ResponseEntity.status(HttpStatus.CONFLICT).body("Artigo já existe na Coleção!");
        } catch (Exception ex){

            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar artigo" + ex.getMessage());
        }
    }*/

    @Override
    public ResponseEntity<?> atualizarArtigo(String id, Artigo artigo) {
       try{
           Artigo existenteArtigo =
                   this.artigoRepository.findById(id).orElse(null);

           if (existenteArtigo == null){
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artigo não encontrado!");
           }

           existenteArtigo.setTitulo(artigo.getTitulo());
           existenteArtigo.setData(artigo.getData());
           existenteArtigo.setTexto(artigo.getTexto());
           this.artigoRepository.save(existenteArtigo);
           return ResponseEntity.status(HttpStatus.OK).build();

       } catch (Exception ex){

           return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Erro ao atualizar artigo" + ex.getMessage());
       }
    }



    /*@Transactional
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

        try{
            return this.artigoRepository.save(artigo);

        } catch(OptimisticLockingFailureException ex) {

            Artigo atualizado =
                    artigoRepository.findById(artigo.getCodigo()).orElse(null);

            if(atualizado !=null){
                atualizado.setTitulo(artigo.getTitulo());
                atualizado.setTexto(artigo.getTexto());
                atualizado.setStatus(artigo.getStatus());

                atualizado.setVersion(atualizado.getVersion()+1);

                return this.artigoRepository.save(atualizado);
            } else {
                throw new RuntimeException("artigo não encontrado" + artigo.getCodigo());
           }
        }
    }*/

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

    @Transactional
    @Override
    public void atualizar(Artigo updateArtigo) {

        this.artigoRepository.save(updateArtigo);
    }

    @Transactional
    @Override
    public void atualizarArtigo(String id, String novaUrl) {

        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("url", novaUrl);

        this.mongoTemplate.updateFirst(query, update, Artigo.class);
    }

    @Transactional
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
        Sort sort = Sort.by("titulo").ascending();
        Pageable paginacao = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(), sort);

        return artigoRepository.findAll(paginacao);
    }

    @Override
    public List<Artigo> findByStatusOrderByTituloAsc(Integer status) {
        return artigoRepository.findByStatusOrderByTituloAsc(status);
    }

    @Override
    public List<Artigo> findByTexto(String searchTerm) {

        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(searchTerm);

        Query query = TextQuery.queryText(criteria).sortByScore();

        return mongoTemplate.find(query, Artigo.class);


    }

    @Override
    public List<ArtigoStatusCount> contarArtigosPorStatus() {

        TypedAggregation<Artigo> aggregation =
                Aggregation.newAggregation(
                        Artigo.class,Aggregation.group("status").count().as("quantidade"),
                        Aggregation.project("quantidade").and("status").previousOperation()
                );
        AggregationResults<ArtigoStatusCount> results = mongoTemplate.aggregate(aggregation, ArtigoStatusCount.class);

        return results.getMappedResults();
    }

    @Override
    public List<AutorTotalArtigo> calcularTotalArtigosPorAutor(LocalDate dataInicio,
                                                               LocalDate dataFim) {

        TypedAggregation<Artigo> aggregation =
                Aggregation.newAggregation(
                        Artigo.class, Aggregation.match(Criteria.where("data")
                                .gte(dataInicio.atStartOfDay()).lt(dataFim.plusDays(1).atStartOfDay())),
                                Aggregation.group("autor").count().as("totalArtigos"),
                                Aggregation.project("totalArtigos").and("autor").previousOperation());

        AggregationResults<AutorTotalArtigo> results = mongoTemplate.aggregate(aggregation, AutorTotalArtigo.class);

        return results.getMappedResults();




    }


}






