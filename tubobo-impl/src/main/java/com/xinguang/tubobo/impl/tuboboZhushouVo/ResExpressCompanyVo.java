package com.xinguang.tubobo.impl.tuboboZhushouVo;

import java.io.Serializable;

public class ResExpressCompanyVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String label;
    private String value;
    private String regularExpression;
    
    public ResExpressCompanyVo(String label, String value, String regularExpression) {
        super();
        this.label = label;
        this.value = value;
        this.regularExpression = regularExpression;
    }
    
    public String getLabel() {
        return label;
    }
    public String getValue() {
        return value;
    }
	public String getRegularExpression() {
		return regularExpression;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}
}
