package com.xinguang.tubobo.api.baseDTO;

import java.io.Serializable;

public class DictItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private String label;
    private String value;
    
    public DictItem(String label, String value) {
        super();
        this.label = label;
        this.value = value;
    }
    public String getLabel() {
        return label;
    }
    public String getValue() {
        return value;
    }
}
