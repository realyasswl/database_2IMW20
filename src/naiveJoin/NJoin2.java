package naiveJoin;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import bplus.BplusTree;
import bplus.Wrapped;
import mq.Bucket;
import mq.Extended;
import mq.HashBucketComparator;

/** some basic hash join */
public class NJoin2 {
	/** aka R */
	JSONArray smallerSet;
	/** aka S */
	JSONArray largerSet;
	String joinKey;

	public NJoin2(JSONArray smallerSet, JSONArray largerSet, String joinKey) {
		this.smallerSet = smallerSet;
		this.largerSet = largerSet;
		this.joinKey = joinKey;
	}

	int mod = common.Constants.mod;

	Object[] bucketArray = new Object[mod];

	int hash(long t) {
		return (int) (t % mod);
	}

	List<Bucket> computeBucketAddress(JSONObject tuple, String key) {
		int hashKey = hash((long) tuple.get(key));
		List<Bucket> list = (List<Bucket>) bucketArray[hashKey];
		if (list == null) {
			list = new ArrayList<Bucket>();
			bucketArray[hashKey] = list;
		}
		return list;
	}

	public void build(JSONArray list, String key) {
		for (int index = 0; index < list.size(); index++) {
			JSONObject tuple = (JSONObject) list.get(index);
			java.lang.Long id = (Long) tuple.get(key);
			List<Bucket> bucketList = computeBucketAddress(tuple, key);

			Bucket bucket = new Bucket(id);
			bucket.setRecordPtr(new Extended(tuple, new BitSet()));
			bucketList.add(bucket);
		}

		// here we have a part for optimization by sorting the list, then we can
		// apply binary search in the search section
		HashBucketComparator c = new HashBucketComparator();
		for (int i = 0; i < mod; i++) {
			List<Bucket> bucketList = (List<Bucket>) bucketArray[i];
			Collections.sort(bucketList, c);
		}
	}

	private Bucket probe(JSONObject tuple, String key) {
		java.lang.Long id = (Long) tuple.get(key);
		List<Bucket> bucketList = computeBucketAddress(tuple, key);

		return bucketList.get(Collections.binarySearch(bucketList, new Bucket(id), new HashBucketComparator()));
	}

	public void query(String rkey, String rvalue, BplusTree rtree, String skey, String svalue, BplusTree stree,
			int seqnr) {
		// System.out.println(seqnr + "|" + rkey + ":" + rvalue + "|" + skey +
		// ":" + svalue);
		if (svalue != null) {
			if(stree==null){
				for (int i = 0; i < largerSet.size(); i++) {
					JSONObject srecord = (JSONObject) largerSet.get(i);
					if (svalue.equals(srecord.get(skey))) {
						// here we use this record to probe in bucket list
						JSONObject vrecord = probe(srecord, joinKey).getRecordPtr().getObj();
						if (vrecord.get(rkey).equals(rvalue)) {
							// found a match
							System.out.println(seqnr + "|R:" + vrecord.toString() + "|S:" + srecord.toString());
						}
					}
				}
			}else{
				Wrapped r = stree.find(svalue);
				if(r!=null){
					Extended e = (Extended) r.getC().getChildren().get(r.getI());
					JSONObject srecord =e.getObj();
					e.getBs().set(seqnr);	
					JSONObject vrecord = probe(srecord, joinKey).getRecordPtr().getObj();
					if (vrecord.get(rkey).equals(rvalue)) {
						// found a match
						System.out.println(seqnr + "|R:" + vrecord.toString() + "|S:" + srecord.toString());
					}
				}else{
					
				}

			}
		}
	}
}
