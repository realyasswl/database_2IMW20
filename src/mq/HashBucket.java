package mq;

import java.util.BitSet;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.simple.JSONObject;

public class HashBucket extends Bucket{

	public HashBucket(Long id, int sid) {
		super(id);
		this.sid = sid;
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
	
	/** points the next bucket in case of overflow */
	private HashBucket nextBucket;
	/** points to the set of query IDs for the tuple are located */


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

	public HashBucket getNextBucket() {
		return nextBucket;
	}

	public void setNextBucket(HashBucket nextBucket) {
		this.nextBucket = nextBucket;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
