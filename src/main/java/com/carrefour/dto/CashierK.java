package com.carrefour.dto;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CashierK implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("store_id")
	private String storeId;
	
	@JsonProperty("update_date")
	private String updateDate;
	
	@JsonProperty("cashier_id")
	private String cashierId;
	
	@JsonProperty("cashier_name")
	private String cashierName;
	
	@JsonProperty("cashier_surname1")
	private String cashierSurname1;
	
	@JsonProperty("cashier_surname2")
	private String cashierSurname2;
	
	@JsonProperty("cashier_document")
	private String cashierDocument;	
	
	@JsonProperty("cashier_phone")
	private String cashierPhone;
	
	@JsonProperty("training")
	private Boolean training;
	
	@JsonProperty("pos_type")
	private String posType;
	
	@JsonProperty("cashcount")
	private Boolean cashCount;
	
	@JsonProperty("inicash")
	private Boolean iniCash;
	
	@JsonProperty("inicash_amount")
	private Integer inicashAmount;
	
	@JsonProperty("level_authorization")
	private Integer levelAuthorization;
	
	@JsonProperty("badgecode")
	private String badgeCode;
	
	@JsonProperty("pass_reset")
	private Boolean passReset;
	
}
