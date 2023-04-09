-- Deletes the PagoNxt database.

DROP TABLE pgn_cle;

DROP TABLE pgn_cle_request;

DROP TABLE pgn_cle_modification;
DROP TABLE pgn_cle_modification_pgn_cle;

DROP TABLE pgn_cle_advance;
DROP TABLE pgn_cle_advance_pgn_cle;

DROP TABLE pgn_cle_advance_request;
DROP TABLE pgn_cle_advance_request_pgn_cle;

DROP TABLE pgn_cle_advance_modification;
DROP TABLE pgn_cle_advance_modification_pgn_cle_advance;

DROP TABLE pgn_cle_advance_cancellation;
DROP TABLE pgn_cle_advance_cancellation_pgn_cle_advance;

DROP TABLE pgn_cle_other_operations;
DROP TABLE pgn_cle_other_operations_pgn_cle;

DROP TABLE pgn_databasechangelog;
DROP TABLE pgn_databasechangeloglock;
