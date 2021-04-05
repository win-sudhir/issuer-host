package com.winnovature.dto;

import java.io.Serializable;

public class ReasonData implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private String reasonCode;
    private String reasonDesc;

    public ReasonData(String reasonCode, String reasonDesc)
    {
        this.reasonCode = reasonCode;
        this.reasonDesc = reasonDesc;
    }

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

}
