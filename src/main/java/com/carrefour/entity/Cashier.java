package com.carrefour.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;



/**
 * The persistent class for the store database table.
 *
 */
@Data
@Entity
@Table(name = "cp_cashier")
public class Cashier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_cashier")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idCashier;

	@Column(name = "cashier")
	private String cashier;
	
	@Column(name = "id_store")
	private Integer idStore;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "surname1")
	private String surname1;
	
	@Column(name = "surname2")
	private String surname2;

	@Column(name = "document")
	private String document;
	
	@Column(name = "phone")
	private String phone;

	@Column(name = "training")
	private Boolean training;
	
	@Column(name = "pos_type")
	private String posType;

	@Column(name = "cashcount")
	private Boolean cashcount;
	
	@Column(name = "inicash")
	private Boolean inicash;

	@Column(name = "inicash_amount")
	private Integer inicashAmount;
	
	@Column(name = "level_authorization")
	private Integer levelAuthorization;
	
	@Column(name = "badgecode")
	private String badgecode;
	
	@Column(name = "reset_password")
	private Boolean resetPassword;
}
