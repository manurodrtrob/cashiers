
--
-- CentralPos	Modelo entidad-relación
--

--
-- Schema cashier
--

DROP SCHEMA IF EXISTS cashier CASCADE;

DROP TABLE IF EXISTS cashier.cp_cashier CASCADE;



--
-- Name: cashier; Type: SCHEMA; Schema: - Owner: apl_smartpos;
--

CREATE SCHEMA IF NOT EXISTS cashier AUTHORIZATION apl_smartpos;


--
-- Name: cp_cashier; Type: TABLE; Schema: cashier; Owner: apl_smartpos; Tablespace: 
--

CREATE TABLE cashier.cp_cashier (
    id_cashier serial PRIMARY KEY,
	cashier character(4) NOT NULL,
	id_store integer NOT NULL,
    name character varying(20) NOT NULL,
    surname1 character varying(20),
    surname2 character varying(20),
    document character varying(13) NOT NULL,
    phone character varying(15) NOT NULL,
    training boolean NOT NULL,
    pos_type character(1) NOT NULL,
    cashcount boolean NOT NULL,
    inicash boolean NOT NULL,
    inicash_amount integer NOT NULL DEFAULT 0,
    level_authorization smallint,
    badgecode character varying(13),
	reset_password boolean,
    update_timestamp timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: update_timestamp; Type: TRIGGER; Schema: cashier; Owner: apl_smartpos;
--

CREATE TRIGGER update_timestamp BEFORE INSERT OR UPDATE ON cashier.cp_cashier FOR EACH ROW EXECUTE PROCEDURE update_timestamp();

