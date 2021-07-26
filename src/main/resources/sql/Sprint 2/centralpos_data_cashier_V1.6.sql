
--
-- CentralPos	Modelo entidad-relación
--

DELETE FROM cashier.cp_cashier;


--
-- Carga de datos cp_cashier
--

INSERT INTO cashier.cp_cashier(cashier, id_store, name, surname1, surname2, document, phone, training, pos_type, cashcount, inicash, inicash_amount, level_authorization, badgecode) VALUES 
	('111',6,'nombre1','surname1','surname2','x555s','91332323',false,'F',false,false,200,8,'saddd'),
	('222',8,'nombre2','surname1','surname2','x555s','91332323',false,'L',false,false,300,8,'sader'),
	('333',10,'nombre3','surname1','surname2','x555s','91332323',false,'I',false,false,500,8,'sadbg'),
	('444',10,'nombre4','surname1','surname2','x555s','91332323',false,'E',false,false,100,8,'sadjh'),
	('555',17,'nombre5','surname1','surname2','x555s','91332323',false,'P',false,false,800,8,'sadry');