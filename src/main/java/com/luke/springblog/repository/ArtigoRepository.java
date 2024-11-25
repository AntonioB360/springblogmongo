package com.luke.springblog.repository;

import com.luke.springblog.model.Artigo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtigoRepository extends MongoRepository<Artigo,String> {

    public void deleteById(String id);





}
