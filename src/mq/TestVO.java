package mq;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TestVO {
	private String rvalue;

	public String getRvalue() {
		return rvalue;
	}

	public void setRvalue(String rvalue) {
		this.rvalue = rvalue;
	}

	public String getSvalue() {
		return svalue;
	}

	public void setSvalue(String svalue) {
		this.svalue = svalue;
	}

	private String svalue;
	
	public TestVO(String rvalue,String svalue){
		this.rvalue=rvalue;
		this.svalue=svalue;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
