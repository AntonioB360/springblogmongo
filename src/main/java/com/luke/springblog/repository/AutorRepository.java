package com.luke.springblog.repository;

import com.luke.springblog.model.Autor;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface AutorRepository extends MongoRepository<Autor,String> {
}
