package mq;

import org.json.simple.JSONObject;

public class Bucket {
	/**
	 * join key is typically a 4 byte integer cached in the hash bucket for
	 * quick access in case the record is stored somewhere outside
	 */
	protected Long key;
	/** points to the record */
	protected Extended recordPtr;

	public Bucket(Long key) {
		this.key = key;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Extended getRecordPtr() {
		return recordPtr;
	}

	public void setRecordPtr(Extended recordPtr) {
		this.recordPtr = recordPtr;
	}
}
