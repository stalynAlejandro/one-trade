package com.pagonxt.onetradefinance.integrations.model.trade;

import com.pagonxt.onetradefinance.integrations.model.UserInfo;

public class TradeContractsQuery {
    private UserInfo requester;
    private String customerCode;
    private String status;
    private String contractReference;
    //order must be 'asc' or 'desc'
    private String order;
    private String sort;
    private Integer start;
    private Integer size;

    public UserInfo getRequester() {
        return requester;
    }

    public void setRequester(UserInfo requester) {
        this.requester = requester;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContractReference() {
        return contractReference;
    }

    public void setContractReference(String contractReference) {
        this.contractReference = contractReference;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "TradeContractsQuery{" +
                "requester=" + requester +
                ", customerCode='" + customerCode + '\'' +
                ", status='" + status + '\'' +
                ", contractReference='" + contractReference + '\'' +
                ", order='" + order + '\'' +
                ", sort='" + sort + '\'' +
                ", start=" + start +
                ", size=" + size +
                '}';
    }
}
