/**
 * 
 */
package com.winnovature.xml.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name="RespMngException", namespace = "http://npci.org/etc/schema/")
public class RespMngException {

	private Head head;
	private Txn txn;
	
	public Head getHead() {
		return head;
	}

	@XmlElement(name="Head")
	public void setHead(Head head) {
		this.head = head;
	}

	public Txn getTxn() {
		return txn;
	}

	@XmlElement(name="Txn")
	public void setTxn(Txn txn) {
		this.txn = txn;
	}
}
