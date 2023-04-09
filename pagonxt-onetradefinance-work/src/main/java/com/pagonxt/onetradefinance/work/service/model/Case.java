package com.pagonxt.onetradefinance.work.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * Model class for case
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Case {

    @JsonProperty("__flowableVersion")
    private Integer flowableVersion;

    private String caseDefinitionKey;

    private String caseDefinitionName;

    private String startUserId;

    private String startTime;

    private String id;

    private String state;

    private List<String> involvedUsers;

    private List<Variable> variables;

    private String caseDefinitionId;

    private String caseDefinitionCategory;

    private List<IdentityLink> identityLinks;

    private List<String> candidateGroups;

    private String name;

    private String tenantId;

    /**
     * getter method
     * @return : an integer value with the flowable version
     */
    public Integer getFlowableVersion() {
        return flowableVersion;
    }

    /**
     * setter method
     * @param flowableVersion : an integer value with the flowable version
     */
    public void setFlowableVersion(Integer flowableVersion) {
        this.flowableVersion = flowableVersion;
    }

    /**
     * getter method
     * @return : a string with the definition key of the case
     */
    public String getCaseDefinitionKey() {
        return caseDefinitionKey;
    }

    /**
     * setter method
     * @param caseDefinitionKey : a string with the definition key of the case
     */
    public void setCaseDefinitionKey(String caseDefinitionKey) {
        this.caseDefinitionKey = caseDefinitionKey;
    }

    /**
     * getter method
     * @return : a string with the definition name of the case
     */
    public String getCaseDefinitionName() {
        return caseDefinitionName;
    }

    /**
     * setter method
     * @param caseDefinitionName : a string with the definition name of the case
     */
    public void setCaseDefinitionName(String caseDefinitionName) {
        this.caseDefinitionName = caseDefinitionName;
    }

    /**
     * getter method
     * @return : a string with the id of the start user
     */
    public String getStartUserId() {
        return startUserId;
    }

    /**
     * setter method
     * @param startUserId : a string with the id of the start user
     */
    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    /**
     * getter method
     * @return : a string with the start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * setter method
     * @param startTime : a string with the start time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * getter method
     * @return : a string with the id
     */
    public String getId() {
        return id;
    }

    /**
     * setter method
     * @param id : a string with the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter method
     * @return : a string with the state
     */
    public String getState() {
        return state;
    }

    /**
     * setter method
     * @param state : a string with the state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * getter method
     * @return : a list with involved users
     */
    public List<String> getInvolvedUsers() {
        return involvedUsers;
    }

    /**
     * setter method
     * @param involvedUsers : a list with involved users
     */
    public void setInvolvedUsers(List<String> involvedUsers) {
        this.involvedUsers = involvedUsers;
    }

    /**
     * getter method
     * @return : a list with variables
     */
    public List<Variable> getVariables() {
        return variables;
    }

    /**
     * setter method
     * @param variables : a list with variables
     */
    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    /**
     * getter method
     * @return : a string with the definition id of the case
     */
    public String getCaseDefinitionId() {
        return caseDefinitionId;
    }

    /**
     * setter method
     * @param caseDefinitionId : a string with the definition id of the case
     */
    public void setCaseDefinitionId(String caseDefinitionId) {
        this.caseDefinitionId = caseDefinitionId;
    }

    /**
     * getter method
     * @return : a string with the definition category of the case
     */
    public String getCaseDefinitionCategory() {
        return caseDefinitionCategory;
    }

    /**
     * setter method
     * @param caseDefinitionCategory : a string with the definition category of the case
     */
    public void setCaseDefinitionCategory(String caseDefinitionCategory) {
        this.caseDefinitionCategory = caseDefinitionCategory;
    }

    /**
     * getter method
     * @return : a list with the identity links
     */
    public List<IdentityLink> getIdentityLinks() {
        return identityLinks;
    }

    /**
     * setter method
     * @param identityLinks : a list with the identity links
     */
    public void setIdentityLinks(List<IdentityLink> identityLinks) {
        this.identityLinks = identityLinks;
    }

    /**
     * getter method
     * @return : a list with the candidate groups
     */
    public List<String> getCandidateGroups() {
        return candidateGroups;
    }

    /**
     * setter method
     * @param candidateGroups : a list with the candidate groups
     */
    public void setCandidateGroups(List<String> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    /**
     * getter method
     * @return : a string with the name
     */
    public String getName() {
        return name;
    }

    /**
     * setter method
     * @param name : a string with the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter method
     * @return : a string with the tenant id
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * setter method
     * @param tenantId : a string with the tenant id
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * Equals method
     * @param o an object
     * @return true if the objects are equals, or not if they aren't equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Case aCase = (Case) o;
        return Objects.equals(flowableVersion, aCase.flowableVersion)
                && Objects.equals(caseDefinitionKey, aCase.caseDefinitionKey) &&
                Objects.equals(caseDefinitionName, aCase.caseDefinitionName) &&
                Objects.equals(startUserId, aCase.startUserId) &&
                Objects.equals(startTime, aCase.startTime) &&
                Objects.equals(id, aCase.id) &&
                Objects.equals(state, aCase.state) &&
                Objects.equals(involvedUsers, aCase.involvedUsers) &&
                Objects.equals(variables, aCase.variables) &&
                Objects.equals(caseDefinitionId, aCase.caseDefinitionId) &&
                Objects.equals(caseDefinitionCategory, aCase.caseDefinitionCategory) &&
                Objects.equals(identityLinks, aCase.identityLinks) &&
                Objects.equals(candidateGroups, aCase.candidateGroups) &&
                Objects.equals(name, aCase.name) &&
                Objects.equals(tenantId, aCase.tenantId);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(flowableVersion, caseDefinitionKey, caseDefinitionName, startUserId, startTime, id, state
                , involvedUsers, variables, caseDefinitionId, caseDefinitionCategory, identityLinks, candidateGroups,
                name, tenantId);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Case{"
                + "flowableVersion=" + flowableVersion +
                ", caseDefinitionKey='" + caseDefinitionKey + '\'' +
                ", caseDefinitionName='" + caseDefinitionName + '\'' +
                ", startUserId='" + startUserId + '\'' +
                ",startTime='" + startTime + '\'' +
                ", id='" + id + '\'' +
                ", state='" + state + '\'' +
                ", involvedUsers=" + involvedUsers +
                ", variables=" + variables +
                ", caseDefinitionId='" + caseDefinitionId + '\'' +
                ", caseDefinitionCategory='" + caseDefinitionCategory + '\'' +
                ", identityLinks=" + identityLinks +
                ", candidateGroups=" + candidateGroups +
                ", name='" + name + '\'' +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }
}
