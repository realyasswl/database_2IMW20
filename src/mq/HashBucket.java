package mq;

import java.util.BitSet;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.simple.JSONObject;

public class HashBucket {

	public HashBucket(Long id, int sid) {
		this.sid = sid;
		this.key = id;
	}

	/**
	 * lock is used to synchronize between threads during building of the hash
	 * table
	 */
	private boolean lck;
	/**
	 * session id identify the last session when this hash bucket was updated
	 */
	private int sid;
	/**
	 * join key is typically a 4 byte integer cached in the hash bucket for
	 * quick access in case the record is stored somewhere outside
	 */
	private Long key;
	/** points to the record */
	private JSONObject recordPtr;
	/** points the next bucket in case of overflow */
	private HashBucket nextBucket;
	/** points to the set of query IDs for the tuple are located */
//	private QIDSet qidSet;
	private BitSet qidSet;

	public boolean isLck() {
		return lck;
	}

	public void setLck(boolean lck) {
		this.lck = lck;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
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

	public HashBucket getNextBucket() {
		return nextBucket;
	}

	public void setNextBucket(HashBucket nextBucket) {
		this.nextBucket = nextBucket;
	}

	public BitSet getQidSet() {
		return qidSet;
	}

	public void setQidSet(BitSet qidSet) {
		this.qidSet = qidSet;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
