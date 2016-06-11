package mq;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import bplus.BplusTree;
import bplus.Wrapped;

/**
 * Use JSONArray as relation. The smaller set will be referred as R, and larger
 * set will be referred as S. There is no qid in the probe set, so we use a
 * array/list to record qid just for probe set
 */
public abstract class AbstractMQJoin {

	int mod = common.Constants.mod;
	int sid = 1;
	/** aka R */
	List<Extended> smallerSet;
	/** aka S */
	List<Extended> largerSet;
	// BitSet[] smallerQID;
	// BitSet[] probeQID;
	String joinKey;

	Object[] bucketArray = new Object[mod];

	int hash(long t) {
		return (int) (t % mod);
	}

	/** use the smaller set to build */
	public abstract void build(List<Extended> list, String key);

	/** use the larger set to probe */
	public abstract void probe(List<Extended> list, String key);

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
	public void query(String rkey, String rvalue, BplusTree rtree, String skey, String svalue, BplusTree stree,
			int seqnr) {
		if (rvalue != null) {
			if (rtree == null) {
				for (int i = 0; i < smallerSet.size(); i++) {
					Extended e = smallerSet.get(i);
					JSONObject tuple = e.getObj();
					if (rvalue.equals(tuple.get(rkey))) {
						// System.out.println(rcond+" found");
						e.getBs().set(seqnr);
					}
				}
			} else {
				Wrapped r = rtree.find(rvalue);
				Extended e = (Extended) r.getC().getChildren().get(r.getI());
				e.getBs().set(seqnr);
			}
		}

		if (svalue != null) {
			if (stree == null) {
				for (int i = 0; i < largerSet.size(); i++) {
					Extended e = largerSet.get(i);
					JSONObject tuple = e.getObj();
					if (svalue.equals(tuple.get(skey))) {
						// System.out.println(scond+" found");
						e.getBs().set(seqnr);
					}
				}
			} else {
				Wrapped r = stree.find(svalue);
				Extended e = (Extended) r.getC().getChildren().get(r.getI());
				e.getBs().set(seqnr);
			}
		}
	}

	BitSet[] geneBitSetArray(int size) {
		BitSet[] r = new BitSet[size];
		for (int i = 0; i < size; i++) {
			r[i] = new BitSet();
		}
		return r;
	}

	public void set(JSONArray smallerSet, JSONArray largerSet, String joinKey) {
		this.smallerSet = new ArrayList<Extended>();
		this.largerSet = new ArrayList<Extended>();
		JSONObject o = null;
		for (int i = 0; i < smallerSet.size(); i++) {
			o = (JSONObject) smallerSet.get(i);
			this.smallerSet.add(new Extended(o, new BitSet()));
		}
		for (int i = 0; i < largerSet.size(); i++) {
			o = (JSONObject) largerSet.get(i);
			this.largerSet.add(new Extended(o, new BitSet()));
		}
		this.joinKey = joinKey;
		// this.smallerQID = geneBitSetArray(smallerSet.size());
		// this.probeQID = geneBitSetArray(largerSet.size());
	};
}
