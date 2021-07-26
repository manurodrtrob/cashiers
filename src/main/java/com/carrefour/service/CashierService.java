package com.carrefour.service;

import com.carrefour.beans.CPResponse;
import com.carrefour.dto.CashierDTO;

public interface CashierService {
	CPResponse findAllCashier(Integer idStore);
	
	CPResponse findByIdCashier(Integer idStore, String idCashier);
	
	CPResponse updateCashier(CashierDTO cashierDTO);
	
	CPResponse createCashier(CashierDTO cashierDTO);

	CPResponse deleteCashier(Integer idStore, String idCashier);
}
