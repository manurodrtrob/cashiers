package com.carrefour.common;


import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;


@Component
public class Messages {

	static ResourceBundle mensajes = ResourceBundle.getBundle("messages.MessagesBundle");

	public static final String CASHIER_ERROR_GET = "cashier.error.get";
	public static final String CASHIER_ERROR_MANDATORY = "cashier.error.mandatory";
	public static final String CASHIER_ERROR_MAXLENGTH = "cashier.error.max_length";
	public static final String CASHIER_ERROR_UNIQUE = "cashier.error.unique";
	public static final String CASHIER_ERROR_NUMERIC = "cashier.error.numeric";
	public static final String CASHIER_ERROR_FORMAT = "cashier.error.format";
	
	public static final String ERROR_GET_CASHIER = "error.get.cashier";
	public static final String ERROR_UPDATE_CASHIER = "error.update.cashier";
	public static final String ERROR_CREATE_CASHIER = "error.create.cashier";
	public static final String ERROR_DELETE_CASHIER = "error.delete.cashier";
	
	private Messages() {}

	public static final String getMensaje(final String clave) {

		return mensajes.getString(clave);
	}

	public static final String getMensaje(final String key, final Object... params) {

		try {
			return MessageFormat.format(mensajes.getString(key), params);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
