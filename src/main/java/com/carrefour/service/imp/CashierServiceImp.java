package com.carrefour.service.imp;

import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.carrefour.beans.CPResponse;
import com.carrefour.common.Constant;
import com.carrefour.common.Messages;
import com.carrefour.configuration.CommunicationConfiguration;
import com.carrefour.configuration.KafkaCashierTopicsConfiguration;
import com.carrefour.dto.CashierDTO;
import com.carrefour.entity.Cashier;
import com.carrefour.mapper.CashierMapper;
import com.carrefour.repository.CashierRepository;
import com.carrefour.service.CashierService;
import com.carrefour.smartpos.cashier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class CashierServiceImp implements CashierService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CashierServiceImp.class);
	
	@Autowired
	private CashierRepository cashierRepository;
	
	@Autowired
	private CashierMapper cashierMapper;
	
	@Autowired
	private KafkaTemplate<String, cashier> cashierKafkaTemplate;
	
	@Autowired
	private KafkaCashierTopicsConfiguration cashierTopicsConfiguration;
	
	@Autowired 
	CommunicationConfiguration commConfig;
	
	@Override
	public CPResponse findAllCashier(Integer idStore) {
		CPResponse response = new CPResponse();

		final Optional<List<Cashier>> cashierOptional = cashierRepository.findByIdStore(idStore);
				
		if (cashierOptional.isPresent()) {
			response.setResponse(cashierMapper.mapperEntityToDTO(cashierOptional.get()));		
		}
		response.setStatusHttp(HttpStatus.ACCEPTED);
		return response;
	}
	
	public CPResponse findByIdCashier(Integer idStore, String idCashier) {
		CPResponse response = new CPResponse();

		final Optional<Cashier> cashierOptional = cashierRepository.findByIdStoreAndCashier(idStore, idCashier);
		if (cashierOptional.isPresent()) {
			response.setResponse(cashierMapper.mapperEntityToCashierDTO(cashierOptional.get()));
			response.setStatusHttp(HttpStatus.ACCEPTED);
		}else {
			response.getErrors().put(Constant.PARAMS_CASHIER,Messages.getMensaje(Messages.CASHIER_ERROR_GET));
			response.setStatusHttp(HttpStatus.NOT_FOUND);
			response.setDescription(Messages.getMensaje(Messages.ERROR_GET_CASHIER));
		}
		return response;
	}
	
	private boolean validateFieldCashier(CashierDTO cashierDTO, Boolean create, CPResponse response) {
		boolean validate = true;
		
		if ( StringUtils.isBlank(cashierDTO.getCashier()) ) {
			response.getErrors().put(Constant.CASHIER, Messages.getMensaje(Messages.CASHIER_ERROR_MANDATORY));
			validate = false;
		} else if ( !StringUtils.isNumeric(cashierDTO.getCashier()) ) {
			response.getErrors().put(Constant.CASHIER, Messages.getMensaje(Messages.CASHIER_ERROR_NUMERIC));
			validate = false;
		} else if ( StringUtils.length(cashierDTO.getCashier()) > 4) {
			response.getErrors().put(Constant.CASHIER, Messages.getMensaje(Messages.CASHIER_ERROR_MAXLENGTH));
			validate = false;
		} else if ( create && StringUtils.length(cashierDTO.getCashier()) < 4) {
			cashierDTO.setCashier(String.format( "%04d", Integer.parseInt(cashierDTO.getCashier()) ));
		}
		return validate;
	}
	
	private void validateFieldResetPassword (CashierDTO cashierDTO, Boolean create, CPResponse response) {
		if (create) {
			cashierDTO.setResetPassword(true);
			return;
		}
		if (cashierDTO.getResetPassword() == null) {
			cashierDTO.setResetPassword(false);
		} 
	}
	
	private boolean validateCashier(CashierDTO cashierDTO, Boolean create, CPResponse response) {
		boolean validate = true;
		
		if ( cashierDTO.getIdStore() == null ) {
			response.getErrors().put(Constant.ID_STORE, Messages.getMensaje(Messages.CASHIER_ERROR_MANDATORY));
			validate = false;
		}
			
		if ( !this.validateFieldCashier(cashierDTO, create, response) ) {
			validate = false;
		}
		
		if ( StringUtils.isBlank(cashierDTO.getName()) ) {
			response.getErrors().put(Constant.NAME, Messages.getMensaje(Messages.CASHIER_ERROR_MANDATORY));
			validate = false;
		}
		if ( StringUtils.isBlank(cashierDTO.getSurname1()) ) {
			response.getErrors().put(Constant.SURNAME, Messages.getMensaje(Messages.CASHIER_ERROR_MANDATORY));
			validate = false;
		}
		if ( StringUtils.isBlank(cashierDTO.getDocument()) ) {
			cashierDTO.setDocument(Constant.DOCUMENT_DEFAULT);
		} else if ( StringUtils.length(cashierDTO.getDocument()) > 13) {
			response.getErrors().put(Constant.DOCUMENT, Messages.getMensaje(Messages.CASHIER_ERROR_MAXLENGTH));
			validate = false;
		}
		if ( StringUtils.isBlank(cashierDTO.getPhone()) ) {
			cashierDTO.setPhone(Constant.PHONE_DEFAULT);
		} else if ( StringUtils.length(cashierDTO.getPhone()) > 15) {
			response.getErrors().put(Constant.PHONE, Messages.getMensaje(Messages.CASHIER_ERROR_MAXLENGTH));
			validate = false;
		}
		
		if ( StringUtils.isBlank(cashierDTO.getPosType()) ) {
			response.getErrors().put(Constant.POS_TYPE, Messages.getMensaje(Messages.CASHIER_ERROR_MANDATORY));
			validate = false;
		} else if ( !StringUtils.isAllUpperCase(cashierDTO.getPosType()) || StringUtils.length(cashierDTO.getPosType()) > 1 ) {
			response.getErrors().put(Constant.POS_TYPE, Messages.getMensaje(Messages.CASHIER_ERROR_FORMAT));
			validate = false;
		}
		
		// Llamar al microservicio de pos para obtener los tipos de pos y verificar posType
		
		if ( !cashierDTO.getIniCash() ) {
			cashierDTO.setIniCashAmount(0);
		}
		if (  StringUtils.length(cashierDTO.getBadgeCode())> 13 ) {
			response.getErrors().put(Constant.BADGECODE, Messages.getMensaje(Messages.CASHIER_ERROR_MAXLENGTH));
			validate = false;
		}
		
		this.validateFieldResetPassword(cashierDTO, create, response);

		if(create) {
			// TODO - validar que la tienda exista
			
			final Optional<Cashier> oldCashier = cashierRepository.findByIdStoreAndCashier(cashierDTO.getIdStore(), cashierDTO.getCashier());
			// Si se crea debe ser ÚNICO
			if(oldCashier.isPresent()) {
				response.getErrors().put(Constant.CASHIER, Messages.getMensaje(Messages.CASHIER_ERROR_UNIQUE, cashierDTO.getCashier()));
				validate = false;
			}
		} 
		
		return validate;
	}
	
	@Override
	public CPResponse updateCashier(CashierDTO cashierDTO) {
		CPResponse response = new CPResponse();
		if (validateCashier(cashierDTO, false, response)) {
			
			Optional<Cashier> cashierOptional = cashierRepository.findByIdStoreAndCashier(cashierDTO.getIdStore(), cashierDTO.getCashier());
			
			if (cashierOptional.isPresent()) {
				String storesMicroServiceURL = makeStoreURL(cashierDTO.getIdStore());
				try {
					ResponseEntity<CPResponse> storeResponse = new RestTemplate().getForEntity(storesMicroServiceURL, CPResponse.class);
					
					if (storeResponse.getStatusCode().equals(HttpStatus.OK)) {
						cashierOptional.get().setName(cashierDTO.getName());
						cashierOptional.get().setSurname1(cashierDTO.getSurname1());
						cashierOptional.get().setSurname2(cashierDTO.getSurname2());
						cashierOptional.get().setDocument(cashierDTO.getDocument());
						cashierOptional.get().setPhone(cashierDTO.getPhone());
						cashierOptional.get().setTraining(cashierDTO.getTraining());
						cashierOptional.get().setPosType(cashierDTO.getPosType());
						cashierOptional.get().setCashcount(cashierDTO.getCashCount());
						cashierOptional.get().setInicash(cashierDTO.getIniCash());
						cashierOptional.get().setInicashAmount(cashierDTO.getIniCashAmount());
						cashierOptional.get().setLevelAuthorization(cashierDTO.getLevelAuthorization());
						cashierOptional.get().setBadgecode(cashierDTO.getBadgeCode());
						cashierOptional.get().setResetPassword(cashierDTO.getResetPassword());
						final Optional<Cashier> updatedCashier = Optional.of(cashierRepository.save(cashierOptional.get()));						
						response.setResponse(cashierMapper.mapperEntityToCashierDTO(updatedCashier.get()));
						response.setStatusHttp(HttpStatus.OK);
						// Después de enviar la información a kafka hay que modificar reset_password
						sendMessage(updatedCashier.get(), storeResponse.getBody(),1);
					}else {
						LOGGER.error("Error cashier {}, calling store: {}, status code {}, body {}",cashierDTO.getIdCashier(), storesMicroServiceURL, storeResponse.getStatusCode(), storeResponse.getBody());
						response.setStatusHttp(HttpStatus.FAILED_DEPENDENCY);
						response.setDescription(Messages.getMensaje(Messages.ERROR_UPDATE_CASHIER));
					}
				}catch (RestClientException e) {
					LOGGER.error("Error cashier {}, calling store: {}, error {}",cashierDTO.getIdCashier(), storesMicroServiceURL,e);
					response.setStatusHttp(HttpStatus.FAILED_DEPENDENCY);
					response.setDescription(Messages.getMensaje(Messages.ERROR_UPDATE_CASHIER));
				}
			
			} else {
				LOGGER.error("Update cashier: {}/{}, Cannot update, not found.", cashierDTO.getIdStore(), cashierDTO.getCashier());
				response.setStatusHttp(HttpStatus.CONFLICT);
				response.setDescription(Messages.getMensaje(Messages.ERROR_UPDATE_CASHIER));
			}
			
		} else {
			LOGGER.error("Update cashier: {}/{}, Cannot update, there are errors on given data.", cashierDTO.getIdStore(), cashierDTO.getCashier());
			response.setStatusHttp(HttpStatus.NOT_ACCEPTABLE);
			response.setDescription(Messages.getMensaje(Messages.ERROR_UPDATE_CASHIER));
		}
		return response;
	}

	private String makeStoreURL(Integer pStoreId) {
		URIBuilder url = new URIBuilder();
		try {
			 url = new URIBuilder(commConfig.getStoresUrl());
			 url .addParameter(Constant.STORE_PARAM, String.valueOf(pStoreId));
			return url.build().toString();
		} catch (URISyntaxException e) {
			LOGGER.error("URL {}, error:{}", commConfig.getStoresUrl(), e);
		}
		return "";	
	}
	
	@SuppressWarnings("unchecked")
	private void sendMessage(Cashier cashierEntity, CPResponse response, int action) {
		LinkedHashMap<Object, Object> store = (LinkedHashMap<Object, Object>) response.getResponse();
		String werks = (String) store.get(Constant.WERKS);
		cashier cashierK = cashierMapper.mapperEntityToCashier(cashierEntity, werks);
		cashierK.setAction(action);
		cashierKafkaTemplate.send(cashierTopicsConfiguration.getCashier().getTopic(),werks, cashierK);
	}
	
	@Override
	public CPResponse createCashier (CashierDTO cashierDTO) {
		CPResponse response = new CPResponse();
		if (validateCashier(cashierDTO, true, response)) {
			String storesMicroServiceURL = makeStoreURL(cashierDTO.getIdStore());
			
			try {
				ResponseEntity<CPResponse> storeResponse = new RestTemplate().getForEntity(storesMicroServiceURL, CPResponse.class);
				if (storeResponse.getStatusCode().equals(HttpStatus.OK)) {
					Cashier cashier = new Cashier();
					cashier.setCashier(cashierDTO.getCashier());
					cashier.setIdStore(cashierDTO.getIdStore());
					cashier.setName(cashierDTO.getName());
					cashier.setSurname1(cashierDTO.getSurname1());
					cashier.setSurname2(cashierDTO.getSurname2());
					cashier.setDocument(cashierDTO.getDocument());
					cashier.setPhone(cashierDTO.getPhone());
					cashier.setTraining(cashierDTO.getTraining());
					cashier.setPosType(cashierDTO.getPosType());
					cashier.setCashcount(cashierDTO.getCashCount());
					cashier.setInicash(cashierDTO.getIniCash());
					cashier.setInicashAmount(cashierDTO.getIniCashAmount());
					cashier.setLevelAuthorization(cashierDTO.getLevelAuthorization());
					cashier.setBadgecode(cashierDTO.getBadgeCode());
					cashier.setResetPassword(cashierDTO.getResetPassword());
					final Optional<Cashier> createCashier = Optional.of(cashierRepository.save(cashier));
					response.setResponse(cashierMapper.mapperEntityToCashierDTO(createCashier.get()));
					response.setStatusHttp(HttpStatus.OK);
					sendMessage(createCashier.get(),storeResponse.getBody(),1);
				}else {
					LOGGER.error("Error calling store: {}, status code {}, body {}", storesMicroServiceURL, storeResponse.getStatusCode(), storeResponse.getBody());
					response.setStatusHttp(HttpStatus.FAILED_DEPENDENCY);
					response.setDescription(Messages.getMensaje(Messages.ERROR_CREATE_CASHIER));
				}
			}catch (RestClientException e) {
				LOGGER.error("Error  calling store: {}, error {}", storesMicroServiceURL,e);
				response.setStatusHttp(HttpStatus.FAILED_DEPENDENCY);
				response.setDescription(Messages.getMensaje(Messages.ERROR_CREATE_CASHIER));
			}
			
		}else {
			LOGGER.error("Create Pos: {}/{}, Cannot create, there are errors on given data.", cashierDTO.getIdStore(), cashierDTO.getCashier());
			response.setStatusHttp(HttpStatus.NOT_ACCEPTABLE);
			response.setDescription(Messages.getMensaje(Messages.ERROR_CREATE_CASHIER));
		}
		return response;
	}
	
	@Override
	public CPResponse deleteCashier(Integer idStore, String idCashier){
		CPResponse response = new CPResponse();
		final Optional<Cashier> cashier = cashierRepository.findByIdStoreAndCashier(idStore,idCashier);
		if (cashier.isPresent()) {
			String storesMicroServiceURL = makeStoreURL(cashier.get().getIdStore());
	
			try {
				ResponseEntity<CPResponse> storeResponse = new RestTemplate().getForEntity(storesMicroServiceURL, CPResponse.class);
				if (storeResponse.getStatusCode().equals(HttpStatus.OK)) {
					cashierRepository.delete(cashier.get());
					response.setStatusHttp(HttpStatus.OK);
					sendMessage(cashier.get(), storeResponse.getBody(),0);
				}else {
					LOGGER.error("Error calling store: {}, status code {}, body {}", storesMicroServiceURL, storeResponse.getStatusCode(), storeResponse.getBody());
					response.setStatusHttp(HttpStatus.FAILED_DEPENDENCY);
					response.setDescription(Messages.getMensaje(Messages.ERROR_CREATE_CASHIER));
				}
			}catch (RestClientException e) {
				LOGGER.error("Error  calling store: {}, error {}", storesMicroServiceURL,e);
				response.setStatusHttp(HttpStatus.FAILED_DEPENDENCY);
				response.setDescription(Messages.getMensaje(Messages.ERROR_CREATE_CASHIER));
			}
		}else {
			response.getErrors().put(Constant.PARAMS_CASHIER, Messages.getMensaje(Messages.CASHIER_ERROR_GET));
			response.setStatusHttp(HttpStatus.NOT_FOUND);
			response.setDescription(Messages.getMensaje(Messages.ERROR_DELETE_CASHIER));
		}
		return response;
	}
}
