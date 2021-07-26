package com.carrefour.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PosTypeDTO implements Serializable {

	private static final long serialVersionUID = 1L;

    @JsonProperty("cod_type")
	private String codType;
    
    @JsonProperty("json_language")
	private String jsonLangage;

}
