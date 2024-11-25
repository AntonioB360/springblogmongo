package com.luke.springblog.controller;

import com.luke.springblog.model.Artigo;
import com.luke.springblog.repository.ArtigoRepository;
import com.luke.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/artigos")
public class ArtigoController {

    @Autowired
    private ArtigoService artigoService;

    @GetMapping
    public List<Artigo> obterTodos(){

        return this.artigoService.obterTodos();
    }

    @GetMapping("/{codigo}")
    public Artigo obterPorCodigo(@PathVariable String codigo){

        return this.artigoService.obterPorCodigo(codigo);
    }

    @PostMapping()
    public Artigo criar(@RequestBody Artigo artigo){

        return this.artigoService.criar(artigo);
    }

    @GetMapping("/maiordata")
    public List<Artigo> findByDataGreaterThan(
            @RequestParam("data")LocalDateTime data){

    return  artigoService.findByDataGreaterThan(data);
    }

    @GetMapping("/data-status")
    public List<Artigo> findByDataAndStatus(
            @RequestParam("data") LocalDateTime data,
            @RequestParam("status") Integer status){

        return  artigoService.findByDataAndStatus(data,status);
    }

    @PutMapping
    public void atualizar(@RequestBody Artigo artigo){

        this.artigoService.atualizar(artigo);
    }


    @PutMapping("/{id}")
    public void atualizarArtigo(
            @PathVariable String id,
            @RequestBody String url){

        this.artigoService.atualizarArtigo(id, url);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {

        this.artigoService.deleteById(id);


    }



}
