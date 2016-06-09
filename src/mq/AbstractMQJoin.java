package mq;

import java.util.BitSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Use JSONArray as relation. The smaller set will be referred as R, and larger
 * set will be referred as S. There is no qid in the probe set, so we use a
 * array/list to record qid just for probe set
 */
public abstract class AbstractMQJoin {

	int mod = common.Constants.mod;
	int sid = 1;
	/** aka R */
	JSONArray smallerSet;
	/** aka S */
	JSONArray largerSet;
	BitSet[] smallerQID;
	BitSet[] probeQID;
	String joinKey;

	Object[] bucketArray = new Object[mod];

	int hash(long t) {
		return (int) (t % mod);
	}

	/** use the smaller set to build */
	public abstract void build(JSONArray list, String key);

	/** use the larger set to probe */
	public abstract void probe(JSONArray list, String key);

	/**
	 * <p>
	 * we want to test the performance of join query, and the standard statement
	 * is as follows:
	 * </p>
	 * <b>select r,*,s.* from r, s where r.value="" and s.value="" and
	 * r.id1=s.id1;</b>
	 * <p>
	 * What we do now is to separate this statement into two pieces:
	 * </p>
	 * <b>select r,* from r where r.value="";</b> <b>select s.* from s where
	 * s.value="";</b>
	 * <p>
	 * And r.id1 and s.id1 are the join key.
	 * </p>
	 * 
	 */
	public void query(String rcond, String scond, int seqnr) {
		if (rcond != null) {
			for (int i = 0; i < smallerSet.size(); i++) {
				JSONObject tuple = (JSONObject) smallerSet.get(i);
				if (rcond == tuple.get("value1")) {
					smallerQID[i].set(seqnr);
					System.out.println("Test1");
				}
			}
		}

		if (scond != null) {
			for (int i = 0; i < largerSet.size(); i++) {
				JSONObject tuple = (JSONObject) largerSet.get(i);
				if (scond == tuple.get("value2")) {
					probeQID[i].set(seqnr);
					System.out.println("Test2");
				}
			}
		}
	}

	public void set(JSONArray smallerSet, JSONArray largerSet, String joinKey) {
		this.smallerSet = smallerSet;
		this.largerSet = largerSet;
		this.joinKey = joinKey;
		this.smallerQID = new BitSet[smallerSet.size()];
		this.probeQID = new BitSet[largerSet.size()];
	};
}
