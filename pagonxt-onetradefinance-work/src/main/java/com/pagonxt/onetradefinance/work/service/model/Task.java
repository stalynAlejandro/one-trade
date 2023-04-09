package com.pagonxt.onetradefinance.work.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model class for task
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Task {

    //class attributes
    @JsonProperty("__flowableVersion")
    private Integer flowableVersion;

    private String rootScopeId;

    private String rootScopeName;

    private Date dueDate;

    private String processDefinitionName;

    private String scopeDefinitionName;

    private List<Translation> translations = new ArrayList<>();

    private String id;

    private String processDefinitionId;

    private String processInstanceId;

    private String scopeId;

    private Integer priority;

    private Date claimTime;

    private String executionId;

    private String taskDefinitionKey;

    private String scopeDefinitionId;

    private String scopeDefinitionKey;

    private String parentScopeDefinitionCategory;

    private String rootScopeDefinitionKey;

    private List<String> candidateGroups;

    private String processDefinitionCategory;

    private String name;

    private String assignee;

    private Long durationInMillis;

    private String description;

    private String scopeDefinitionCategory;

    private String parentScopeId;

    private String processDefinitionKey;

    private String rootScopeType;

    private String parentScopeType;

    private String scopeType;

    private String rootScopeDefinitionCategory;

    private List<String> involvedUsers;

    private String parentScopeDefinitionName;

    private String owner;

    private List<Variable> variables;

    private String parentScopeDefinitionId;

    private String formKey;

    private String parentScopeDefinitionKey;

    private List<IdentityLink> identityLinks;

    private Date createTime;

    private String tenantId;

    private String rootScopeDefinitionId;

    private Date endTime;

    private String taskModelName;

    private String rootScopeDefinitionName;

    private String category;

    /**
     * getter method
     * @return : an integer with flowable version
     */
    public Integer getFlowableVersion() {
        return flowableVersion;
    }

    /**
     * setter method
     * @param flowableVersion : an integer with flowable version
     */
    public void setFlowableVersion(Integer flowableVersion) {
        this.flowableVersion = flowableVersion;
    }

    /**
     * getter method
     * @return : a string with the id of root scope
     */
    public String getRootScopeId() {
        return rootScopeId;
    }

    /**
     * setter method
     * @param rootScopeId : a string with the id of root scope
     */
    public void setRootScopeId(String rootScopeId) {
        this.rootScopeId = rootScopeId;
    }

    /**
     * getter method
     * @return : a string with the name of root scope
     */
    public String getRootScopeName() {
        return rootScopeName;
    }

    /**
     * setter method
     * @param rootScopeName : a string with the name of root scope
     */
    public void setRootScopeName(String rootScopeName) {
        this.rootScopeName = rootScopeName;
    }

    /**
     * getter method
     * @return : a Date object with the due Date
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * setter method
     * @param dueDate : a Date object with the due Date
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * getter method
     * @return : a string with the definition name of the process
     */
    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    /**
     * setter method
     * @param processDefinitionName : a string with the definition name of the process
     */
    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    /**
     * getter method
     * @return : a string with the definition name of the scope
     */
    public String getScopeDefinitionName() {
        return scopeDefinitionName;
    }

    /**
     * setter method
     * @param scopeDefinitionName : a string with the definition name of the scope
     */
    public void setScopeDefinitionName(String scopeDefinitionName) {
        this.scopeDefinitionName = scopeDefinitionName;
    }

    /**
     * getter method
     * @return : a list with translations
     * @see Translation
     */
    public List<Translation> getTranslations() {
        return translations;
    }

    /**
     * setter method
     * @param translations :  a list with translations
     * @see Translation
     */
    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
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
     * @return : a string with the definition id of the process
     */
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    /**
     * setter method
     * @param processDefinitionId : a string with the definition id of the process
     */
    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    /**
     * getter method
     * @return : a string with the id of the process instance
     */
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    /**
     * setter method
     * @param processInstanceId : a string with the id of the process instance
     */
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    /**
     * getter method
     * @return : a string with the scope id
     */
    public String getScopeId() {
        return scopeId;
    }

    /**
     * setter method
     * @param scopeId : a string with the scope id
     */
    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    /**
     * getter method
     * @return : a string with the priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * setter method
     * @param priority : a string with the priority
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * getter method
     * @return : a Date object with the claim time
     */
    public Date getClaimTime() {
        return claimTime;
    }

    /**
     * setter method
     * @param claimTime : a Date object with the claim time
     */
    public void setClaimTime(Date claimTime) {
        this.claimTime = claimTime;
    }

    /**
     * getter method
     * @return : a string with the execution id
     */
    public String getExecutionId() {
        return executionId;
    }

    /**
     * setter method
     * @param executionId : a string with the execution id
     */
    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    /**
     * getter method
     * @return : a string with the task definition key
     */
    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    /**
     * setter method
     * @param taskDefinitionKey : a string with the task definition key
     */
    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    /**
     * getter method
     * @return : a string with the scope definition id
     */
    public String getScopeDefinitionId() {
        return scopeDefinitionId;
    }

    /**
     * setter method
     * @param scopeDefinitionId : a string with the scope definition id
     */
    public void setScopeDefinitionId(String scopeDefinitionId) {
        this.scopeDefinitionId = scopeDefinitionId;
    }

    /**
     * getter method
     * @return : a string with the scope definition key
     */
    public String getScopeDefinitionKey() {
        return scopeDefinitionKey;
    }

    /**
     * setter method
     * @param scopeDefinitionKey : a string with the scope definition key
     */
    public void setScopeDefinitionKey(String scopeDefinitionKey) {
        this.scopeDefinitionKey = scopeDefinitionKey;
    }

    /**
     * getter method
     * @return : a string with the scope definition category
     */
    public String getParentScopeDefinitionCategory() {
        return parentScopeDefinitionCategory;
    }

    /**
     * setter method
     * @param parentScopeDefinitionCategory : a string with the scope definition category
     */
    public void setParentScopeDefinitionCategory(String parentScopeDefinitionCategory) {
        this.parentScopeDefinitionCategory = parentScopeDefinitionCategory;
    }

    /**
     * getter method
     * @return : a string with the root scope definition key
     */
    public String getRootScopeDefinitionKey() {
        return rootScopeDefinitionKey;
    }

    /**
     * setter method
     * @param rootScopeDefinitionKey : a string with the root scope definition key
     */
    public void setRootScopeDefinitionKey(String rootScopeDefinitionKey) {
        this.rootScopeDefinitionKey = rootScopeDefinitionKey;
    }

    /**
     * getter method
     * @return : a list of candidate groups
     */
    public List<String> getCandidateGroups() {
        return candidateGroups;
    }

    /**
     * setter method
     * @param candidateGroups : a list of candidate groups
     */
    public void setCandidateGroups(List<String> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    /**
     * getter method
     * @return : a string with the process definition category
     */
    public String getProcessDefinitionCategory() {
        return processDefinitionCategory;
    }

    /**
     * setter method
     * @param processDefinitionCategory : a string with the process definition category
     */
    public void setProcessDefinitionCategory(String processDefinitionCategory) {
        this.processDefinitionCategory = processDefinitionCategory;
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
     * @return : a string with the assignee
     */
    public String getAssignee() {
        return assignee;
    }

    /**
     * setter method
     * @param assignee : a string with the assignee
     */
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    /**
     * getter method
     * @return : a long value with the duration in milis
     */
    public Long getDurationInMillis() {
        return durationInMillis;
    }

    /**
     * setter method
     * @param durationInMillis : a long value with the duration in milis
     */
    public void setDurationInMillis(Long durationInMillis) {
        this.durationInMillis = durationInMillis;
    }

    /**
     * getter method
     * @return : a string with the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * setter method
     * @param description : a string with the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * getter method
     * @return : a string with the scope definition category
     */
    public String getScopeDefinitionCategory() {
        return scopeDefinitionCategory;
    }

    /**
     * setter method
     * @param scopeDefinitionCategory : a string with the scope definition category
     */
    public void setScopeDefinitionCategory(String scopeDefinitionCategory) {
        this.scopeDefinitionCategory = scopeDefinitionCategory;
    }

    /**
     * getter method
     * @return : a string with the parent scope id
     */
    public String getParentScopeId() {
        return parentScopeId;
    }

    /**
     * setter method
     * @param parentScopeId : a string with the parent scope id
     */
    public void setParentScopeId(String parentScopeId) {
        this.parentScopeId = parentScopeId;
    }

    /**
     * getter method
     * @return : a string with the process definition key
     */
    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    /**
     * setter method
     * @param processDefinitionKey : a string with the process definition key
     */
    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    /**
     * getter method
     * @return : a string with the root scope type
     */
    public String getRootScopeType() {
        return rootScopeType;
    }

    /**
     * setter method
     * @param rootScopeType : a string with the root scope type
     */
    public void setRootScopeType(String rootScopeType) {
        this.rootScopeType = rootScopeType;
    }

    /**
     * getter method
     * @return : a string with the parent scope type
     */
    public String getParentScopeType() {
        return parentScopeType;
    }

    /**
     * setter method
     * @param parentScopeType : a string with the parent scope type
     */
    public void setParentScopeType(String parentScopeType) {
        this.parentScopeType = parentScopeType;
    }

    /**
     * getter method
     * @return : a string with the scope type
     */
    public String getScopeType() {
        return scopeType;
    }

    /**
     * setter method
     * @param scopeType : a string with the scope type
     */
    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    /**
     * getter method
     * @return : a string with the root scope definition category
     */
    public String getRootScopeDefinitionCategory() {
        return rootScopeDefinitionCategory;
    }

    /**
     * setter method
     * @param rootScopeDefinitionCategory : a string with the root scope definition category
     */
    public void setRootScopeDefinitionCategory(String rootScopeDefinitionCategory) {
        this.rootScopeDefinitionCategory = rootScopeDefinitionCategory;
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
     * @return : a string with the parent scope definition name
     */
    public String getParentScopeDefinitionName() {
        return parentScopeDefinitionName;
    }

    /**
     * setter method
     * @param parentScopeDefinitionName : a string with the parent scope definition name
     */
    public void setParentScopeDefinitionName(String parentScopeDefinitionName) {
        this.parentScopeDefinitionName = parentScopeDefinitionName;
    }

    /**
     * getter method
     * @return : a string with the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * setter method
     * @param owner : a string with the owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * getter method
     * @return : a list of variables
     */
    public List<Variable> getVariables() {
        return variables;
    }

    /**
     * setter method
     * @param variables : a list of variables
     */
    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    /**
     * getter method
     * @return : a string with the parent scope definition id
     */
    public String getParentScopeDefinitionId() {
        return parentScopeDefinitionId;
    }

    /**
     * setter method
     * @param parentScopeDefinitionId : a string with the parent scope definition id
     */
    public void setParentScopeDefinitionId(String parentScopeDefinitionId) {
        this.parentScopeDefinitionId = parentScopeDefinitionId;
    }

    /**
     * getter method
     * @return : a string with the form key
     */
    public String getFormKey() {
        return formKey;
    }

    /**
     * setter method
     * @param formKey : a string with the form key
     */
    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    /**
     * getter method
     * @return : a string with the parent scope definition key
     */
    public String getParentScopeDefinitionKey() {
        return parentScopeDefinitionKey;
    }

    /**
     * setter method
     * @param parentScopeDefinitionKey : a string with the parent scope definition key
     */
    public void setParentScopeDefinitionKey(String parentScopeDefinitionKey) {
        this.parentScopeDefinitionKey = parentScopeDefinitionKey;
    }

    /**
     * getter method
     * @return : a list of identity links
     */
    public List<IdentityLink> getIdentityLinks() {
        return identityLinks;
    }

    /**
     * setter method
     * @param identityLinks : a list of identity links
     */
    public void setIdentityLinks(List<IdentityLink> identityLinks) {
        this.identityLinks = identityLinks;
    }

    /**
     * getter method
     * @return : a Date object with create time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * setter method
     * @param createTime : a Date object with create time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
     * getter method
     * @return : a string with the root scope definition id
     */
    public String getRootScopeDefinitionId() {
        return rootScopeDefinitionId;
    }

    /**
     * setter method
     * @param rootScopeDefinitionId : a string with the root scope definition id
     */
    public void setRootScopeDefinitionId(String rootScopeDefinitionId) {
        this.rootScopeDefinitionId = rootScopeDefinitionId;
    }

    /**
     * getter method
     * @return : a Date object with the end time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * setter method
     * @param endTime : a Date object with the end time
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * getter method
     * @return : a string with the task model name
     */
    public String getTaskModelName() {
        return taskModelName;
    }

    /**
     * setter method
     * @param taskModelName : a string with the task model name
     */
    public void setTaskModelName(String taskModelName) {
        this.taskModelName = taskModelName;
    }

    /**
     * getter method
     * @return : a string with the root scope definition name
     */
    public String getRootScopeDefinitionName() {
        return rootScopeDefinitionName;
    }

    /**
     * setter method
     * @param rootScopeDefinitionName : a string with the root scope definition name
     */
    public void setRootScopeDefinitionName(String rootScopeDefinitionName) {
        this.rootScopeDefinitionName = rootScopeDefinitionName;
    }

    /**
     * getter method
     * @return : a string with the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * setter method
     * @param category : a string with the category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Task{" +
                "flowableVersion=" + flowableVersion +
                ", rootScopeId='" + rootScopeId + '\'' +
                ", rootScopeName='" + rootScopeName + '\'' +
                ", dueDate=" + dueDate +
                ", processDefinitionName='" + processDefinitionName + '\'' +
                ", scopeDefinitionName='" + scopeDefinitionName + '\'' +
                ", translations=" + translations +
                ", id='" + id + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", scopeId='" + scopeId + '\'' +
                ", priority=" + priority +
                ", claimTime=" + claimTime +
                ", executionId='" + executionId + '\'' +
                ", taskDefinitionKey='" + taskDefinitionKey + '\'' +
                ", scopeDefinitionId='" + scopeDefinitionId + '\'' +
                ", scopeDefinitionKey='" + scopeDefinitionKey + '\'' +
                ", parentScopeDefinitionCategory='" + parentScopeDefinitionCategory + '\'' +
                ", rootScopeDefinitionKey='" + rootScopeDefinitionKey + '\'' +
                ", candidateGroups=" + candidateGroups +
                ", processDefinitionCategory='" + processDefinitionCategory + '\'' +
                ", name='" + name + '\'' +
                ", assignee='" + assignee + '\'' +
                ", durationInMillis=" + durationInMillis +
                ", description='" + description + '\'' +
                ", scopeDefinitionCategory='" + scopeDefinitionCategory + '\'' +
                ", parentScopeId='" + parentScopeId + '\'' +
                ", processDefinitionKey='" + processDefinitionKey + '\'' +
                ", rootScopeType='" + rootScopeType + '\'' +
                ", parentScopeType='" + parentScopeType + '\'' +
                ", scopeType='" + scopeType + '\'' +
                ", rootScopeDefinitionCategory='" + rootScopeDefinitionCategory + '\'' +
                ", involvedUsers=" + involvedUsers +
                ", parentScopeDefinitionName='" + parentScopeDefinitionName + '\'' +
                ", owner='" + owner + '\'' +
                ", variables=" + variables +
                ", parentScopeDefinitionId='" + parentScopeDefinitionId + '\'' +
                ", formKey='" + formKey + '\'' +
                ", parentScopeDefinitionKey='" + parentScopeDefinitionKey + '\'' +
                ", identityLinks=" + identityLinks +
                ", createTime=" + createTime +
                ", tenantId='" + tenantId + '\'' +
                ", rootScopeDefinitionId='" + rootScopeDefinitionId + '\'' +
                ", endTime=" + endTime +
                ", taskModelName='" + taskModelName + '\'' +
                ", rootScopeDefinitionName='" + rootScopeDefinitionName + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
