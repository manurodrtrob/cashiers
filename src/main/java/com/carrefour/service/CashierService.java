package com.carrefour.service;

import com.carrefour.beans.Response;
import com.carrefour.dto.CashierDTO;

public interface CashierService {
	Response findAllCashier(Integer idStore);
	
	Response findByIdCashier(Integer idStore, String idCashier);
	
	Response updateCashier(CashierDTO cashierDTO);
	
	Response createCashier(CashierDTO cashierDTO);

	Response deleteCashier(Integer idStore, String idCashier);
}
