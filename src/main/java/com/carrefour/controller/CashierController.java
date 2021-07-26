package com.carrefour.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.carrefour.beans.Response;
import com.carrefour.dto.CashierDTO;
import com.carrefour.service.imp.CashierServiceImp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 */
@RestController
@RequestMapping("/cashier")
@CrossOrigin
@Api(tags = "cashier", description = "cashier")
public class CashierController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CashierController.class);
	
	@Autowired
	CashierServiceImp cashierService;
	
	@GetMapping
	@ApiOperation(value = "get cashier for idStore and idCashier", response = ResponseEntity.class)
	public ResponseEntity<Response> findCashier(final HttpServletRequest pRequest, final HttpServletResponse pResponse,
			@RequestParam final String id,
			@RequestParam final Integer store) {

		LOGGER.debug("GET: {}/{}", store, id);
		
		Response response = cashierService.findByIdCashier(store, id);
		
		return new ResponseEntity<>(response, response.getStatusHttp());
	}
	
	@GetMapping(path = "/all")
	@ApiOperation(value = "get all cashier for idStore", response = CashierDTO.class, responseContainer = "List", notes = "cashier list")
	public ResponseEntity<Response> findAllCashier(final HttpServletRequest pRequest, final HttpServletResponse pResponse, 
			@RequestParam Integer store) {

		LOGGER.debug("GET: {}", store);

		Response response = cashierService.findAllCashier(store);

		return new ResponseEntity<>(response, response.getStatusHttp());
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update an existing cashier", notes = "Update an existing cashier")
	@ApiResponses({@ApiResponse(code = HttpServletResponse.SC_OK, message = "Cashier updated."),
			@ApiResponse(code = HttpServletResponse.SC_CONFLICT, message = "Cashier not exists."),
			@ApiResponse(code = HttpServletResponse.SC_NOT_ACCEPTABLE, message = "Mandatory fields error.")})
	public ResponseEntity<Response> updateCashier(@RequestBody final CashierDTO cashier, 
			final HttpServletResponse pRresponse, final HttpServletRequest pRequest) {
		
		LOGGER.info("PUT -> /cashier   [{}/{}]", cashier.getIdStore(), cashier.getCashier());
		
		final Response response = cashierService.updateCashier(cashier);
		
		return new ResponseEntity<>(response, response.getStatusHttp());
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create a new cashier", notes = "Create a new cashier")
	@ApiResponses({
			@ApiResponse(code = HttpServletResponse.SC_OK, message = "cashier create."),
			@ApiResponse(code = HttpServletResponse.SC_NOT_ACCEPTABLE, message = "Mandatory fields error.")
	})
	public ResponseEntity<Response> createCashier(@RequestBody final CashierDTO cashier, 
			final HttpServletResponse pResponse, final HttpServletRequest pRequest) {

		LOGGER.info("POST -> /cashier   [{}/{}]", cashier.getIdStore(), cashier.getCashier());
		final Response response = cashierService.createCashier(cashier);
		return new ResponseEntity<>(response, response.getStatusHttp());
	}
	

	@DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete an existing cashier", notes = "Delete an existing cashier")
	@ApiResponses({@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Cashier deleted."),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Cashier doesn't exists."),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Mandatory fields error.")})
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<Response> deleteCashier(final HttpServletResponse pResponse, 
			@RequestParam final String id,
			@RequestParam final Integer store){

		LOGGER.debug("DELETE: {}/{}", store, id);
		final Response response = cashierService.deleteCashier(store,id);
		return new ResponseEntity<>(response, response.getStatusHttp());
	}
}
