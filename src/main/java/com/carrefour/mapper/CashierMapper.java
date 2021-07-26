package com.carrefour.mapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.carrefour.dto.CashierDTO;
import com.carrefour.dto.CashierK;
import com.carrefour.entity.Cashier;

@Component
public class CashierMapper {

	public CashierDTO mapperEntityToCashierDTO (Cashier cashier){
		CashierDTO cashierDTO = new CashierDTO();
		cashierDTO.setIdCashier(cashier.getIdCashier());
		cashierDTO.setIdStore(cashier.getIdStore());
		cashierDTO.setCashier(cashier.getCashier());
		cashierDTO.setName(cashier.getName());
		cashierDTO.setSurname1(cashier.getSurname1());
		cashierDTO.setSurname2(cashier.getSurname2());
		cashierDTO.setDocument(cashier.getDocument());
		cashierDTO.setCashCount(cashier.getCashcount());
		cashierDTO.setBadgeCode(cashier.getBadgecode());
		cashierDTO.setIniCash(cashier.getInicash());
		cashierDTO.setIniCashAmount(cashier.getInicashAmount());
		cashierDTO.setLevelAuthorization(cashier.getLevelAuthorization());
		cashierDTO.setPhone(cashier.getPhone());
		cashierDTO.setTraining(cashier.getTraining());
		cashierDTO.setPosType(cashier.getPosType());
		cashierDTO.setResetPassword(cashier.getResetPassword());
		
		return cashierDTO;
	}
	
	public List<CashierDTO> mapperEntityToDTO (List<Cashier> cashierList){
		List<CashierDTO> dtoList = new ArrayList<>();
		for (Cashier cashier: cashierList) {
			CashierDTO cashierDTO = mapperEntityToCashierDTO(cashier);
			dtoList.add(cashierDTO);
		}
		return dtoList;
	}
	
	public CashierK mapperEntityToCashier(Cashier cashierEntity) {
		CashierK cashier = new CashierK();
		cashier.setBadgeCode(cashierEntity.getBadgecode());
		cashier.setCashCount(cashierEntity.getCashcount());
		cashier.setCashierId(cashierEntity.getIdCashier().toString());
		cashier.setCashierDocument(cashierEntity.getDocument());
		cashier.setCashierName(cashierEntity.getName());
		cashier.setCashierSurname1(cashierEntity.getSurname1());
		cashier.setCashierPhone(cashierEntity.getPhone());
		cashier.setCashierSurname2(cashierEntity.getSurname2());
		Calendar calendar = Calendar.getInstance();
	    Date date =  calendar.getTime();
		cashier.setUpdateDate(date.toString()); // temporal
		cashier.setTraining(cashierEntity.getTraining());
		cashier.setStoreId(cashierEntity.getIdStore().toString());
		cashier.setPosType(cashierEntity.getPosType());
		cashier.setPassReset(cashierEntity.getResetPassword());
		cashier.setLevelAuthorization(cashierEntity.getLevelAuthorization());
		cashier.setInicashAmount(cashierEntity.getInicashAmount());
		cashier.setIniCash(cashierEntity.getInicash());
		
		return cashier;
	}
}
	
