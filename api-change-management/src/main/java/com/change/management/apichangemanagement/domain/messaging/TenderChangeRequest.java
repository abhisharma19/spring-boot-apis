package com.change.management.apichangemanagement.domain.messaging;

import com.change.management.apichangemanagement.domain.TenderChangeCriteria;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.io.Serializable;

public class TenderChangeRequest implements Serializable {

    private static final long serialVersionUID = 4768934858374859372L;

    @Valid
    private TenderChangeCriteria criteria;

    public TenderChangeCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(TenderChangeCriteria critera) {
        this.criteria = critera;
    }
}
