
--
-- CentralPos	Modelo entidad-relación
--

--
-- Permisos sobre el esquema cashier
--

GRANT CREATE ON SCHEMA cashier TO apl_smartpos;
GRANT USAGE ON SCHEMA cashier TO apl_smartpos;
GRANT USAGE ON SCHEMA cashier TO group_exexx;
GRANT CREATE ON SCHEMA cashier TO group_instalador;
GRANT USAGE ON SCHEMA cashier TO group_instalador;
GRANT USAGE ON SCHEMA cashier TO group_select;
GRANT USAGE ON SCHEMA cashier TO group_spufi;


--
-- Permisos sobre las tablas del esquema cashier
--

GRANT ALL ON TABLE cashier.cp_cashier TO apl_smartpos;
GRANT DELETE, INSERT, SELECT, UPDATE ON TABLE cashier.cp_cashier TO group_exexx;
GRANT ALL ON TABLE cashier.cp_cashier TO group_instalador;
GRANT SELECT ON TABLE cashier.cp_cashier TO group_select;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE cashier.cp_cashier TO group_spufi;


--
-- Permisos sobre las secuencias del esquema cashier
--

GRANT ALL ON SEQUENCE cashier.cp_cashier_id_cashier_seq TO apl_smartpos;
GRANT SELECT, USAGE ON SEQUENCE cashier.cp_cashier_id_cashier_seq TO group_spufi;
GRANT ALL ON SEQUENCE cashier.cp_cashier_id_cashier_seq TO group_exexx;
GRANT ALL ON SEQUENCE cashier.cp_cashier_id_cashier_seq TO group_instalador;
