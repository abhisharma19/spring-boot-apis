package com.change.management.apichangemanagement.domain.messaging;

import com.change.management.apichangemanagement.domain.TenderChangeEntity;
import com.change.management.apichangemanagement.domain.exception.Error;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TenderChangeResponse implements Serializable {

    private TenderChangeEntity tenderChangeEntity;
    private List<Error> errors;

    public TenderChangeResponse(){
        super();
    }

    public TenderChangeResponse(TenderChangeEntity tenderChangeEntity) {
        this.tenderChangeEntity = tenderChangeEntity;
    }

    public TenderChangeEntity getTenderChangeEntity() {
        return tenderChangeEntity;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
