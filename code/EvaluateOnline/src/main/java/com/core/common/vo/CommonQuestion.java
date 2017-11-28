package com.core.common.vo;

public class CommonQuestion {
	private int qid;
	private String qname;
	private int qstatue;
	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public String getQname() {
		return qname;
	}
	public void setQname(String qname) {
		this.qname = qname;
	}
	public int getQstatue() {
		return qstatue;
	}
	public void setQstatue(int qstatue) {
		this.qstatue = qstatue;
	}
	@Override
	public String toString() {
		return "CommonQuestion [qid=" + qid + ", qname=" + qname + ", qstatue=" + qstatue + "]";
	}
	
}
