
-- Deletes the Flowable Work database.
--
-- Original scripts found here: https://documentation.flowable.com/latest/develop/dbs/overview/#database-scripts
-- This page points to the latest scripts version: https://developer-docs.flowable.com/db-scripts/latest/db-scripts.zip
-- Which, once downloaded, corresponded to the 3.11.0 Flowable version (this project's Flowable version is 3.11.5).
--
-- Fixes/customizations:
--
-- merged the files flowable.oracle.drop.work.all.sql, flowable.oracle.drop.engine.all.sql and flowable.oracle.drop.inspect.all.sql
-- fixed some index names that didn't match with the schema created by starting up the application
-- deleted some indexes that didn't exist


-- flowable.oracle.drop.work.all.sql (customized)

drop table FLW_TEMPL_VAR_DEFINITION;
drop table FLW_TEMPL_DEFINITION;
drop table FLW_TEMPL_DEPLOYMENT_RESOURCE;
drop table FLW_TEMPL_DEPLOYMENT;
drop table FLW_TP_DATABASECHANGELOG;
drop table FLW_TP_DATABASECHANGELOGLOCK;

drop index ACT_IDX_SERVICE_DEF_UNIQ;
drop index IDX_SERVICE_DEF_DEPLOYMENT;

drop table FLW_SE_DEFINITION;
drop table FLW_SE_DEPLOYMENT_RESOURCE;
drop table FLW_SE_DEPLOYMENT;
drop table FLW_SE_DATABASECHANGELOG;
drop table FLW_SE_DATABASECHANGELOGLOCK;

drop index IDX_POLICY_DEF_LINK_POLICY_DEF;
drop index ACT_IDX_POLICIES_DEF_UNIQ;
drop index IDX_POLICY_DEF_DEPLOYMENT;

drop table FLW_POLICY_DEF_LINK;
drop table FLW_POLICY_DEFINITION;
drop table FLW_POLICY_DEPLOYMENT_RESOURCE;
drop table FLW_POLICY_DEPLOYMENT;
drop table FLW_PO_DATABASECHANGELOG;
drop table FLW_PO_DATABASECHANGELOGLOCK;

drop index FLW_IDX_DO_SCHEMA_DEF_UNIQ;
drop index IDX_DO_SCHEMA_DEF_DEPLOYMENT;
drop index FLW_IDX_DATAOBJECT_DEF_UNIQ;
drop index IDX_DATAOBJECT_DEF_DEPLOYMENT;

drop table FLW_DO_DATAOBJECT_INSTANCE;
drop table FLW_DO_VARIABLES;
drop table FLW_MASTER_DATA_INSTANCE;
drop table FLW_DO_SCHEMA_DEFINITION;
drop table FLW_DATAOBJECT_DEFINITION;
drop table FLW_DO_DEPLOYMENT_RESOURCE;
drop table FLW_DATAOBJECT_DEPLOYMENT;
drop table FLW_DO_DATABASECHANGELOG;
drop table FLW_DO_DATABASECHANGELOGLOCK;

drop index FLW_IDX_AUDIT_INST_CREATE;
drop index FLW_IDX_AUDIT_INST_SCOPE;
drop index FLW_IDX_AUDIT_INST_TYPE;
drop index FLW_IDX_AUDIT_INST_DEF;

drop table FLW_AUDIT_INSTANCE;
drop table FLW_AU_DATABASECHANGELOG;
drop table FLW_AU_DATABASECHANGELOGLOCK;

drop index IDX_HI_ACTION_INST_ACTION_DEF;
drop index ACTION_HI_DEFINITION_ID_;
drop index IDX_HI_ACTION_LINK_ACTION_INST;
drop index FLW_IDX_HI_ACTION_LINK_SCOPE;
drop index IDX_ACTION_INST_ACTION_DEF;
drop index IDX_ACTION_DEF_DEPLOYMENT;
drop index ACTION_DEFINITION_ID_;
drop index IDX_ACTION_LINK_ACTION_INST;
drop index FLW_IDX_ACTION_LINK_SCOPE;
drop index IDX_ACT_TYPE_LINK_ACT_INST;

drop table FLW_ACTION_TYPE_LINK;
drop table FLW_ACTION_DEF_LINK;
drop table FLW_HI_ACTION_LINK;
drop table FLW_HI_ACTION_INSTANCE;
drop table FLW_ACTION_LINK;
drop table FLW_ACTION_INSTANCE;
drop table FLW_ACTION_DEFINITION;
drop table FLW_ACTION_DEPLOYMENT_RESOURCE;
drop table FLW_ACTION_DEPLOYMENT;
drop table FLW_AD_DATABASECHANGELOG;
drop table FLW_AD_DATABASECHANGELOGLOCK;

drop index idx_contitem_folder;
drop index idx_rendition_scope;
drop index idx_rendition_procid;
drop index idx_rendition_taskid;
drop index idx_contitem_scope;
drop index idx_contitem_procid;
drop index idx_contitem_taskid;
drop index idx_contitem_name;
drop index idx_contitem_verparid;
drop index idx_contitem_bfolder;

drop table FLW_CO_METADATA;
drop table FLW_CO_DEFINITION;
drop table FLW_CO_DEPLOYMENT_RESOURCE;
drop table FLW_CO_DEPLOYMENT;
drop table FLW_CO_CONTENT_OBJECT;
drop table FLW_CO_CONTENT_ITEM;
drop table FLW_CO_RENDITION_ITEM;
drop table FLW_CO_DATABASECHANGELOG;
drop table FLW_CO_DATABASECHANGELOGLOCK;

drop index FLW_IDX_FORM_DEF_UNIQ;
drop index FLW_IDX_FORM_TASK;
drop index FLW_IDX_FORM_PROC;
drop index FLW_IDX_FORM_SCOPE;

drop table FLW_FO_FORM_INSTANCE;
drop table FLW_FO_FORM_DEFINITION;
drop table FLW_FO_FORM_RESOURCE;
drop table FLW_FO_FORM_DEPLOYMENT;
drop table FLW_FO_DATABASECHANGELOG;
drop table FLW_FO_DATABASECHANGELOGLOCK;

drop index ACT_IDX_APP_DEF_UNIQ;
drop index ACT_IDX_APP_DEF_DPLY;
drop index ACT_IDX_APP_RSRC_DPL;

drop table ACT_APP_PAGEDEF;
drop table ACT_APP_APPDEF;
drop table ACT_APP_DEPLOYMENT_RESOURCE;
drop table ACT_APP_DEPLOYMENT;
drop table ACT_APP_DATABASECHANGELOG;
drop table ACT_APP_DATABASECHANGELOGLOCK;

drop index IDX_FLW_SEQUENCE_DEF_UNIQ;
drop index IDX_SEQUENCE_DEF_DEPLOYMENT;
drop index FLW_CON_PROP_NS_NAME_TENANT;
drop index FLW_IDX_TRANSLATION_SCOPE;
drop index IDX_COMMENT_SCOPE_ID;
drop index IDX_COMMENT_USER_ID;

drop table FLW_SEQUENCE_DEFINITION;
drop table FLW_CONFIGURATION_PROPERTY;
drop table FLW_COMMENT;
drop table FLW_TRANSLATION;
drop table FLW_VAR_EXT_DEFINITION;
drop table FLW_QUERY_DEFINITION;
drop table FLW_PL_DEPLOYMENT_RESOURCE;
drop table FLW_PL_DEPLOYMENT;
drop table FLW_PL_DATABASECHANGELOG;
drop table FLW_PL_DATABASECHANGELOGLOCK;

drop table FLW_LIC_USER_LOGIN;
drop table FLW_LIC_LICENSE;
drop table FLW_LIC_DATABASECHANGELOG;
drop table FLW_LIC_DATABASECHANGELOGLOCK;

drop index IDX_USER_ACCOUNT_USER_DEF;
drop index IDX_USER_ACCOUNT_USER;
drop index IDX_USER_ACC_TYPE_SUB_EXT_USER;
drop index FLW_IDX_USER_UPDATE_TIME;
drop index FLW_IDX_USER_CREATE_TIME;
drop index FLW_IDX_PRIV_GROUP;
drop index FLW_IDX_PRIV_USER;
drop index FLW_IDX_PRIV_MAPPING;
drop index FLW_IDX_MEMB_USER;
drop index FLW_IDX_MEMB_GROUP;

drop table FLW_ID_USER_ACCOUNT;
drop table FLW_ID_USER_ACCOUNT_DEFINITION;
drop table FLW_ID_PRIV_MAPPING;
drop table FLW_ID_PRIV;
drop table FLW_ID_TOKEN;
drop table FLW_IDENTITY_INFO;
drop table FLW_ID_INFO;
drop table FLW_ID_USER_DEFINITIONS;
drop table FLW_ID_MEMBERSHIP;
drop table FLW_ID_USER;
drop table FLW_ID_GROUP;
drop table FLW_ID_PROPERTY;
drop table FLW_ID_DATABASECHANGELOG;
drop table FLW_ID_DATABASECHANGELOGLOCK;

-- flowable.oracle.drop.engine.all.sql (customized)

drop index ACT_IDX_DMN_DEC_UNIQ;

drop table ACT_DMN_HI_DECISION_EXECUTION;
drop table ACT_DMN_DECISION;
drop table ACT_DMN_DEPLOYMENT_RESOURCE;
drop table ACT_DMN_DEPLOYMENT;
drop table ACT_DMN_DATABASECHANGELOG;
drop table ACT_DMN_DATABASECHANGELOGLOCK;

drop index ACT_IDX_CASE_DEF_UNIQ;
drop index ACT_IDX_PLAN_ITEM_STAGE_INST;
drop index ACT_IDX_MIL_CASE_INST;
drop index ACT_IDX_MIL_CASE_DEF;
drop index ACT_IDX_SENTRY_PLAN_ITEM;
drop index ACT_IDX_SENTRY_CASE_INST;
drop index ACT_IDX_SENTRY_CASE_DEF;
drop index ACT_IDX_PLAN_ITEM_CASE_INST;
drop index ACT_IDX_PLAN_ITEM_CASE_DEF;
drop index ACT_IDX_CASE_INST_PARENT;
drop index ACT_IDX_CASE_DEF_DPLY;
drop index ACT_IDX_CMMN_RSRC_DPL;
drop index ACT_IDX_CASE_INST_REF_ID_;

drop table ACT_CMMN_HI_PLAN_ITEM_INST;
drop table ACT_CMMN_HI_MIL_INST;
drop table ACT_CMMN_HI_CASE_INST;
drop table ACT_CMMN_RU_MIL_INST;
drop table ACT_CMMN_RU_SENTRY_PART_INST;
drop table ACT_CMMN_RU_PLAN_ITEM_INST;
drop table ACT_CMMN_RU_CASE_INST;
drop table ACT_CMMN_CASEDEF;
drop table ACT_CMMN_DEPLOYMENT_RESOURCE;
drop table ACT_CMMN_DEPLOYMENT;
drop table ACT_CMMN_DATABASECHANGELOG;
drop table ACT_CMMN_DATABASECHANGELOGLOCK;

drop index ACT_IDX_CHANNEL_DEF_UNIQ;
drop index ACT_IDX_EVENT_DEF_UNIQ;

drop table FLW_CHANNEL_DEFINITION;
drop table FLW_EVENT_DEFINITION;
drop table FLW_EVENT_RESOURCE;
drop table FLW_EVENT_DEPLOYMENT;
drop table FLW_EV_DATABASECHANGELOG;
drop table FLW_EV_DATABASECHANGELOGLOCK;

drop index ACT_IDX_HI_PRO_INST_END;
drop index ACT_IDX_HI_PRO_I_BUSKEY;
drop index ACT_IDX_HI_ACT_INST_START;
drop index ACT_IDX_HI_ACT_INST_END;
drop index ACT_IDX_HI_DETAIL_PROC_INST;
drop index ACT_IDX_HI_DETAIL_ACT_INST;
drop index ACT_IDX_HI_DETAIL_TIME;
drop index ACT_IDX_HI_DETAIL_NAME;
drop index ACT_IDX_HI_DETAIL_TASK_ID;
drop index ACT_IDX_HI_PROCVAR_PROC_INST;
drop index ACT_IDX_HI_PROCVAR_TASK_ID;
drop index ACT_IDX_HI_PROCVAR_EXE;
drop index ACT_IDX_HI_ACT_INST_PROCINST;
drop index ACT_IDX_HI_IDENT_LNK_TASK;
drop index ACT_IDX_HI_IDENT_LNK_PROCINST;
drop index ACT_IDX_HI_TASK_INST_PROCINST;

drop table ACT_HI_PROCINST;
drop table ACT_HI_ACTINST;
drop table ACT_HI_DETAIL;
drop table ACT_HI_COMMENT;
drop table ACT_HI_ATTACHMENT;

drop index ACT_IDX_BYTEAR_DEPL;
drop index ACT_IDX_EXE_PROCINST;
drop index ACT_IDX_EXE_PARENT;
drop index ACT_IDX_EXE_SUPER;
drop index ACT_IDX_TSKASS_TASK;
drop index ACT_IDX_TASK_EXEC;
drop index ACT_IDX_TASK_PROCINST;
drop index ACT_IDX_TASK_PROCDEF;
drop index ACT_IDX_VAR_EXE;
drop index ACT_IDX_VAR_PROCINST;
drop index ACT_IDX_JOB_EXECUTION_ID;
drop index ACT_IDX_JOB_PROC_DEF_ID;
drop index ACT_IDX_MODEL_SOURCE;
drop index ACT_IDX_MODEL_SOURCE_EXTRA;
drop index ACT_IDX_MODEL_DEPLOYMENT;
drop index ACT_IDX_PROCDEF_INFO_JSON;

drop index ACT_IDX_EXEC_BUSKEY;
drop index ACT_IDX_VARIABLE_TASK_ID;

drop index ACT_IDX_RU_ACTI_START;
drop index ACT_IDX_RU_ACTI_END;
drop index ACT_IDX_RU_ACTI_PROC;
drop index ACT_IDX_RU_ACTI_PROC_ACT;
drop index ACT_IDX_RU_ACTI_EXEC;
drop index ACT_IDX_RU_ACTI_EXEC_ACT;

alter table ACT_GE_BYTEARRAY
    drop CONSTRAINT ACT_FK_BYTEARR_DEPL;

alter table ACT_RU_EXECUTION
    drop CONSTRAINT ACT_FK_EXE_PROCINST;

alter table ACT_RU_EXECUTION
    drop CONSTRAINT ACT_FK_EXE_PARENT;

alter table ACT_RU_EXECUTION
    drop CONSTRAINT ACT_FK_EXE_SUPER;

alter table ACT_RU_EXECUTION
    drop CONSTRAINT ACT_FK_EXE_PROCDEF;

alter table ACT_RU_IDENTITYLINK
    drop CONSTRAINT ACT_FK_TSKASS_TASK;

alter table ACT_RU_IDENTITYLINK
    drop CONSTRAINT ACT_FK_IDL_PROCINST;

alter table ACT_RU_IDENTITYLINK
    drop CONSTRAINT ACT_FK_ATHRZ_PROCEDEF;

alter table ACT_RU_TASK
    drop CONSTRAINT ACT_FK_TASK_EXE;

alter table ACT_RU_TASK
    drop CONSTRAINT ACT_FK_TASK_PROCINST;

alter table ACT_RU_TASK
    drop CONSTRAINT ACT_FK_TASK_PROCDEF;

alter table ACT_RU_VARIABLE
    drop CONSTRAINT ACT_FK_VAR_EXE;

alter table ACT_RU_VARIABLE
    drop CONSTRAINT ACT_FK_VAR_PROCINST;

alter table ACT_RU_JOB
    drop CONSTRAINT ACT_FK_JOB_EXECUTION;

alter table ACT_RU_JOB
    drop CONSTRAINT ACT_FK_JOB_PROCESS_INSTANCE;

alter table ACT_RU_JOB
    drop CONSTRAINT ACT_FK_JOB_PROC_DEF;

alter table ACT_RU_TIMER_JOB
    drop CONSTRAINT act_fk_timer_job_execution;

alter table ACT_RU_TIMER_JOB
    drop CONSTRAINT act_fk_timer_job_process_instance;

alter table ACT_RU_TIMER_JOB
    drop CONSTRAINT act_fk_timer_job_proc_def;

alter table ACT_RU_SUSPENDED_JOB
    drop CONSTRAINT act_fk_suspended_job_execution;

alter table ACT_RU_SUSPENDED_JOB
    drop CONSTRAINT act_fk_suspended_job_process_instance;

alter table ACT_RU_SUSPENDED_JOB
    drop CONSTRAINT act_fk_suspended_job_proc_def;

alter table ACT_RU_DEADLETTER_JOB
    drop CONSTRAINT act_fk_deadletter_job_execution;

alter table ACT_RU_DEADLETTER_JOB
    drop CONSTRAINT act_fk_deadletter_job_process_instance;

alter table ACT_RU_DEADLETTER_JOB
    drop CONSTRAINT act_fk_deadletter_job_proc_def;

alter table ACT_RU_EVENT_SUBSCR
    drop CONSTRAINT ACT_FK_EVENT_EXEC;

alter table ACT_RE_PROCDEF
    drop CONSTRAINT ACT_UNIQ_PROCDEF;

alter table ACT_RE_MODEL
    drop CONSTRAINT ACT_FK_MODEL_SOURCE;

alter table ACT_RE_MODEL
    drop CONSTRAINT ACT_FK_MODEL_SOURCE_EXTRA;

alter table ACT_RE_MODEL
    drop CONSTRAINT ACT_FK_MODEL_DEPLOYMENT;

alter table ACT_PROCDEF_INFO
    drop CONSTRAINT ACT_UNIQ_INFO_PROCDEF;

alter table ACT_PROCDEF_INFO
    drop CONSTRAINT ACT_FK_INFO_JSON_BA;

alter table ACT_PROCDEF_INFO
    drop CONSTRAINT ACT_FK_INFO_PROCDEF;

drop index ACT_IDX_EVENT_SUBSCR_CONFIG_;
drop index ACT_IDX_EVENT_SUBSCR;
drop index ACT_IDX_ATHRZ_PROCEDEF;
drop index ACT_IDX_PROCDEF_INFO_PROC;

drop table ACT_RU_ACTINST;
drop table ACT_RE_DEPLOYMENT;
drop table ACT_RE_MODEL;
drop table ACT_RE_PROCDEF;
drop table ACT_RU_EXECUTION;
drop table ACT_RU_EVENT_SUBSCR;

drop table ACT_EVT_LOG;
drop table ACT_PROCDEF_INFO;

drop index ACT_IDX_HI_TASK_SCOPE;
drop index ACT_IDX_HI_TASK_SUB_SCOPE;
drop index ACT_IDX_HI_TASK_SCOPE_DEF;

drop table ACT_HI_TASKINST;
drop table ACT_HI_TSK_LOG;

drop index ACT_IDX_TASK_CREATE;
drop index ACT_IDX_TASK_SCOPE;
drop index ACT_IDX_TASK_SUB_SCOPE;
drop index ACT_IDX_TASK_SCOPE_DEF;

drop table ACT_RU_TASK;

drop index FLW_IDX_BATCH_PART;
alter table FLW_RU_BATCH_PART
    drop CONSTRAINT FLW_FK_BATCH_PART_PARENT;

drop table FLW_RU_BATCH_PART;
drop table FLW_RU_BATCH;

drop index ACT_IDX_JOB_SCOPE;
drop index ACT_IDX_JOB_SUB_SCOPE;
drop index ACT_IDX_JOB_SCOPE_DEF;
drop index ACT_IDX_TJOB_SCOPE;
drop index ACT_IDX_TJOB_SUB_SCOPE;
drop index ACT_IDX_TJOB_SCOPE_DEF;
drop index ACT_IDX_SJOB_SCOPE;
drop index ACT_IDX_SJOB_SUB_SCOPE;
drop index ACT_IDX_SJOB_SCOPE_DEF;
drop index ACT_IDX_DJOB_SCOPE;
drop index ACT_IDX_DJOB_SUB_SCOPE;
drop index ACT_IDX_DJOB_SCOPE_DEF;

drop index ACT_IDX_JOB_CORRELATION_ID;

alter table ACT_RU_JOB
    drop CONSTRAINT ACT_FK_JOB_EXCEPTION;

drop table ACT_RU_JOB;
drop table ACT_RU_TIMER_JOB;
drop table ACT_RU_SUSPENDED_JOB;
drop table ACT_RU_DEADLETTER_JOB;
drop table ACT_RU_HISTORY_JOB;
drop table ACT_RU_EXTERNAL_JOB;

drop index ACT_IDX_HI_IDENT_LNK_USER;
drop index ACT_IDX_HI_IDENT_LNK_SCOPE;
drop index ACT_IDX_HI_IDENT_LNK_SCOPE_DEF;

drop table ACT_HI_IDENTITYLINK;

drop index ACT_IDX_IDENT_LNK_USER;
drop index ACT_IDX_IDENT_LNK_GROUP;
drop index ACT_IDX_IDENT_LNK_SCOPE;
drop index ACT_IDX_IDENT_LNK_SCOPE_DEF;

drop table ACT_RU_IDENTITYLINK;

drop index ACT_IDX_HI_ENT_LNK_SCOPE;
drop index ACT_IDX_HI_ENT_LNK_SCOPE_DEF;
drop index ACT_IDX_HI_ENT_LNK_ROOT_SCOPE;
drop index ACT_IDX_HI_ENT_LNK_REF_SCOPE;

drop table ACT_HI_ENTITYLINK;

drop index ACT_IDX_ENT_LNK_SCOPE;
drop index ACT_IDX_ENT_LNK_SCOPE_DEF;
drop index ACT_IDX_ENT_LNK_ROOT_SCOPE;
drop index ACT_IDX_ENT_LNK_REF_SCOPE;

drop table ACT_RU_ENTITYLINK;

drop index ACT_IDX_HI_PROCVAR_NAME_TYPE;
drop index ACT_IDX_HI_VAR_SCOPE_ID_TYPE;
drop index ACT_IDX_HI_VAR_SUB_ID_TYPE;

drop table ACT_HI_VARINST;

drop index ACT_IDX_VAR_BYTEARRAY;
drop index ACT_IDX_RU_VAR_SCOPE_ID_TYPE;
drop index ACT_IDX_RU_VAR_SUB_ID_TYPE;

alter table ACT_RU_VARIABLE
    drop CONSTRAINT ACT_FK_VAR_BYTEARRAY;

drop table ACT_RU_VARIABLE;

drop table ACT_GE_BYTEARRAY;
drop table ACT_GE_PROPERTY;

-- flowable.oracle.drop.inspect.all.sql

drop index IDX_META_MODEL_SCOPE;

drop table FLW_INSP_META_MODEL;

drop index IDX_TEST_MODEL_CREATOR;
drop index IDX_TEST_MODEL_SCOPE;

drop table FLW_INSP_TEST_MODEL;

drop table FLW_TEST_USER_DEFINITION;

drop table FLW_TEST_INSTANCE;
drop table FLW_INSP_RESOURCE;
drop table FLW_INSP_DEPLOYMENT;
drop table FLW_ISP_DATABASECHANGELOG;
drop table FLW_ISP_DATABASECHANGELOGLOCK;
