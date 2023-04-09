package com.pagonxt.onetradefinance.work.common;

/**
 * Class with common constants
 * These constants are used in all products and events. Include:
 * Constants for SLA
 * Constants with product, event or country,necessaries for the start of a case
 * Constants to define the document visibilty of the users
 * Constants with the data objects that we use to reference requests of export collection (advance included)
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class CaseCommonConstants {

    private CaseCommonConstants() {
    }

    public static final String SLA_CUTOFF = "SLA_cutOff";
    public static final String SLA_PRIORITY = "SLA_priority";
    public static final String SLA_START_TIME = "SLA_startSLATime";
    public static final String SLA_END_TIME = "SLA_endSLATime";

    public static final String SLA_START = "SLA_start";
    public static final String SLA_END = "SLA_end";
    public static final String SLA_RESOLUTION_TIME = "SLA_resolutionTime";
    public static final String SLA_END_ONHOLD_AS_PRIORITY = "SLA_endOnholdAsPriority";
    public static final String VARNAME_PRODUCT = "product";
    public static final String VARNAME_EVENT = "event";
    public static final String VARNAME_COUNTRY = "country";
    public static final String VARNAME_REGISTRATION_COMPLETED = "registrationCompleted";
    public static final String TIMER_DEFINITION_ID_DRAFT_REMINDER = "draftReminderTimerEventListener";
    public static final String TIMER_DEFINITION_ID_DRAFT_CANCELATION = "draftCancelationTimerEventListener";
    public static final String DOCUMENT_DEFINITION_KEY_METADATA = "PGN_CON001";
    public static final String VISIBILITY_CUSTOMER = "PUBLIC_OFFICE_AND_CUSTOMER";
    public static final String VISIBILITY_OFFICE = "PUBLIC_OFFICE";
    public static final String VISIBILITY_BO = "INTERNAL_BO";
    public static final String DATA_OBJECT_MODEL = "CLE_DO003";
    public static final String DATA_OBJECT_MODEL_ADVANCE = "CLE_DO005";
}
