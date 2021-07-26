package com.carrefour.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carrefour.entity.Cashier;


@Repository
public interface CashierRepository extends JpaRepository<Cashier, Integer> {

	Optional<List<Cashier>> findByIdStore(Integer idStore);
	
	Optional<Cashier> findByIdCashier(Integer idCashier);
	
	Optional<Cashier> findByIdStoreAndCashier(Integer idStore, String cashier);
}
