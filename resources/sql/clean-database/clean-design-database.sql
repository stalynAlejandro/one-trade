
-- Deletes the Flowable Design database, except for the license and changelog tables which are shared with Flowable Work.
--
-- Original scripts found here: https://documentation.flowable.com/latest/develop/dbs/overview/#database-scripts
-- This page points to the latest scripts version: https://developer-docs.flowable.com/db-scripts/latest/db-scripts.zip
-- Which, once downloaded, corresponded to the 3.11.0 Flowable version (this project's Flowable version is 3.11.5).
--
-- Fixes/customizations:
--
-- fixed duplicate drop table sentence
-- omitted the drop table sentences relative to licensing, necessary for Flowable Work to start


-- flowable-oracle.drop.design.all.sql (customized)

drop index idx_model_resource;

drop table ACT_DE_MODEL_RESOURCE;
drop table FLW_DE_TENANT;
drop table ACT_DE_MODEL_METADATA;
drop table ACT_DE_TOKEN;
drop table ACT_DE_USER;
drop table ACT_DE_MODEL_RELATION;
drop table ACT_DE_MODEL_TAG_RELATION;
drop table ACT_DE_MODEL_TAG;

drop index idx_proc_mod_history_proc;

drop table ACT_DE_MODEL_HISTORY;

drop index idx_proc_mod_created;

drop table ACT_DE_MODEL;
drop table ACT_DE_DATABASECHANGELOG;
drop table ACT_DE_DATABASECHANGELOGLOCK;

drop index FLW_IDX_PALETTE_DEF_DPLY;
drop index FLW_IDX_PALETTE_DEF_UNIQ;

drop table FLW_PALETTE_DEFINITION;

drop index FLW_IDX_PALETTE_RSRC_DPL;

drop table FLW_PALETTE_DEPLOYMENT_RES;
drop table FLW_PALETTE_DEPLOYMENT;
drop table FLW_PAL_DATABASECHANGELOG;
drop table FLW_PAL_DATABASECHANGELOGLOCK;
