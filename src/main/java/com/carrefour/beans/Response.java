package com.carrefour.beans;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(description = "Resupuesta Genérica utilizada para todas las respuestas de los servicios")
public class Response implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(notes = "Descripción de la respuesta")
	private String description;
	
	@ApiModelProperty(notes = "Objeto de la respuesta")
	private transient Object response;


	@ApiModelProperty(notes = "Mapa de erroes (si los hay)")
	private Map<String, String> errors = new HashMap<>();

	
	private HttpStatus statusHttp;
}
