package com.carrefour.service.imp;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.carrefour.beans.Response;
import com.carrefour.common.Constant;
import com.carrefour.common.Messages;
import com.carrefour.configuration.KafkaCashierTopicsConfiguration;
import com.carrefour.dto.CashierDTO;
import com.carrefour.dto.CashierK;
import com.carrefour.entity.Cashier;
import com.carrefour.mapper.CashierMapper;
import com.carrefour.repository.CashierRepository;
import com.carrefour.service.CashierService;
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
	private KafkaTemplate<String, CashierK> cashierKafkaTemplate;
	
	@Autowired
	private KafkaCashierTopicsConfiguration cashierTopicsConfiguration;
	
	@Override
	public Response findAllCashier(Integer idStore) {
		Response response = new Response();

		final Optional<List<Cashier>> cashierOptional = cashierRepository.findByIdStore(idStore);
				
		if (cashierOptional.isPresent()) {
			response.setResponse(cashierMapper.mapperEntityToDTO(cashierOptional.get()));		
		}
		response.setStatusHttp(HttpStatus.ACCEPTED);
		return response;
	}
	
	public Response findByIdCashier(Integer idStore, String idCashier) {
		Response response = new Response();

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
	
	private boolean validateFieldCashier(CashierDTO cashierDTO, Boolean create, Response response) {
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
	
	private void validateFieldResetPassword (CashierDTO cashierDTO, Boolean create, Response response) {
		if (create) {
			cashierDTO.setResetPassword(true);
			return;
		}
		if (cashierDTO.getResetPassword() == null) {
			cashierDTO.setResetPassword(false);
		} 
	}
	
	private boolean validateCashier(CashierDTO cashierDTO, Boolean create, Response response) {
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
	public Response updateCashier(CashierDTO cashierDTO) {
		Response response = new Response();
		if (validateCashier(cashierDTO, false, response)) {
			Optional<Cashier> cashierOptional = cashierRepository.findByIdStoreAndCashier(cashierDTO.getIdStore(), cashierDTO.getCashier());
			
			if (cashierOptional.isPresent()) {
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

	@Override
	public Response createCashier (CashierDTO cashierDTO) {
		Response response = new Response();
		if (validateCashier(cashierDTO, true, response)) {
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
			CashierK cashierK = cashierMapper.mapperEntityToCashier(createCashier.get());
			cashierKafkaTemplate.send(cashierTopicsConfiguration.getCashier().getTopic(),cashierK.getStoreId()+"-"+cashierK.getCashierId(), cashierK);
			
			response.setStatusHttp(HttpStatus.OK);
		}else {
			LOGGER.error("Create Pos: {}/{}, Cannot create, there are errors on given data.", cashierDTO.getIdStore(), cashierDTO.getCashier());
			response.setStatusHttp(HttpStatus.NOT_ACCEPTABLE);
			response.setDescription(Messages.getMensaje(Messages.ERROR_CREATE_CASHIER));
		}
		return response;
	}
	
	@Override
	public Response deleteCashier(Integer idStore, String idCashier){
		Response response = new Response();
		final Optional<Cashier> cashier = cashierRepository.findByIdStoreAndCashier(idStore,idCashier);
		if (cashier.isPresent()) {
			cashierRepository.delete(cashier.get());
			response.setStatusHttp(HttpStatus.OK);
		}else {
			response.getErrors().put(Constant.PARAMS_CASHIER, Messages.getMensaje(Messages.CASHIER_ERROR_GET));
			response.setStatusHttp(HttpStatus.NOT_FOUND);
			response.setDescription(Messages.getMensaje(Messages.ERROR_DELETE_CASHIER));
		}
		return response;
	}
}
