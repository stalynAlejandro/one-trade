package com.pagonxt.onetradefinance.work.parser.bpmn.usertask;

/**
 * BPMN parse exception for Pagonxt User Tasks
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class BpmnUserTaskParserException extends RuntimeException {

    /**
     * constructor method
     * @param message : a string with a message
     */
    public BpmnUserTaskParserException(String message) {
        super(message);
    }
}
