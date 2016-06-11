package mq;

import java.util.BitSet;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.simple.JSONObject;

public class Extended {

	private JSONObject obj;
	private BitSet bs;
	public Extended(JSONObject obj, BitSet bs) {
		super();
		this.obj = obj;
		this.bs = bs;
	}
	public JSONObject getObj() {
		return obj;
	}
	public void setObj(JSONObject obj) {
		this.obj = obj;
	}
	public BitSet getBs() {
		return bs;
	}
	public void setBs(BitSet bs) {
		this.bs = bs;
	}
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
