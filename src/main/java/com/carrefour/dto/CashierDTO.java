package com.carrefour.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CashierDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id_cashier")
	private Integer idCashier;
	
	@JsonProperty("id_store")
	private Integer idStore;
	
    @JsonProperty("cashier")
	private String cashier;
	
    @JsonProperty("name")
	private String name;
	
    @JsonProperty("surname1")
	private String surname1;
	
    @JsonProperty("surname2")
	private String surname2;

    @JsonProperty("document")
	private String document;
	
    @JsonProperty("phone")
	private String phone;

    @JsonProperty("training")
	private Boolean training;
    
    @JsonProperty("pos_type")
    private String posType;
    
    @JsonProperty("cashcount")
	private Boolean cashCount;
	
    @JsonProperty("inicash")
	private Boolean iniCash;

    @JsonProperty("inicash_amount")
	private Integer iniCashAmount;
	
    @JsonProperty("level_authorization")
	private Integer levelAuthorization;
	
    @JsonProperty("badgecode")
	private String badgeCode;
    
    @JsonProperty("reset_password")
	private Boolean resetPassword;
}
