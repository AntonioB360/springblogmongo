package com.luke.springblog.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
public class Artigo {
    @Id
    private String codigo;

    @NotBlank(message = "O Título do Artigo não pode estar em branco")
    private String titulo;

    @NotNull(message = "a data do artigo não pode ser nula")
    private LocalDateTime data;

    @NotBlank(message = "O texto do Artigo não pode estar em branco")
    @TextIndexed
    private String texto;

    private  String url;

    @NotNull(message = "o status do artigo não pode ser nulo")
    private Integer status;

    @DBRef
    private Autor autor;

    @Version
    private long version;


}
