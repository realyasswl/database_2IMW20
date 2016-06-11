package naiveJoin;

import org.json.simple.JSONObject;

public class NBucket {
	/**
	 * join key is typically a 4 byte integer cached in the hash bucket for
	 * quick access in case the record is stored somewhere outside
	 */
	private Long key;
	/** points to the record */
	private JSONObject recordPtr;

	public NBucket(Long key) {
		this.key = key;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public JSONObject getRecordPtr() {
		return recordPtr;
	}

	public void setRecordPtr(JSONObject recordPtr) {
		this.recordPtr = recordPtr;
	}

}
