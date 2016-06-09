package mq;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/** this version of MQJoin has some modifications */
public class MQJoinAdvanced extends BaseMQJoin {

	MQJoinAdvanced(JSONArray smallerSet, JSONArray largerSet, String joinKey) {
		super(smallerSet, largerSet, joinKey);
	}

	List<HashBucket> computeBucketAddress(JSONObject tuple, String key) {
		int hashKey = hash((long) tuple.get(key));
		List<HashBucket> list = (List<HashBucket>) bucketArray[hashKey];
		if (list == null) {
			list = new ArrayList<HashBucket>();
			bucketArray[hashKey] = list;
		}
		return list;
	}

	/**
	 * <p>
	 * Implement the build phase described in page 482 MQJoin actually skip the
	 * S1 part which is about prefetch, since we are doing all operations
	 * in-memory.
	 * </p>
	 * <p>
	 * In S2 part, because we only deal with local client, we don't need to lock
	 * bucket and maintain session id. Cut that too.
	 * </p>
	 * <p>
	 * Also return a list of buckets instead of just one. In case of overflow,
	 * we new a HashBucket instance and add it to the end of the list.
	 * </p>
	 * 
	 * @param key
	 *            is the name of the column which is used to do hash calculation
	 * 
	 */
	public void build(JSONArray list, String key) {
		for (int index = 0; index < list.size(); index++) {
			JSONObject tuple = (JSONObject) list.get(index);
			java.lang.Long id = (Long) tuple.get(key);
			List<HashBucket> bucketList = computeBucketAddress(tuple, key);

			HashBucket bucket = new HashBucket(id, sid);
			bucket.setQidSet(smallerQID[index]);
			bucket.setRecordPtr(tuple);
			bucketList.add(bucket);
		}

		// here we have a part for optimization by sorting the list, then we can
		// apply binary search in the search section
		HashBucketComparator c = new HashBucketComparator();
		for (int i = 0; i < mod; i++) {
			List<HashBucket> bucketList = (List<HashBucket>) bucketArray[i];
			Collections.sort(bucketList, c);
		}
	}

	public void probe(JSONArray list, String key) {
		JSONArray otherGroup = new JSONArray();
		JSONArray keyMatchGroup = new JSONArray();
		HashBucketComparator c = new HashBucketComparator();
		otherGroup.clear();
		keyMatchGroup.clear();
		for (int index = 0; index < list.size(); index++) {
			JSONObject tuple = (JSONObject) list.get(index);
			java.lang.Long id = (Long) tuple.get(key);
			List<HashBucket> bucketList = computeBucketAddress(tuple, key);
			// here is a little different from algorithm described in paper
			// since we've already sorted the bucket list for each key, we
			// can do a binary search to speed up, and we have the bucket list
			// instead of just a bucket which has pointer to the next bucket, we
			// can do this in one loop

			HashBucket bucket = bucketList.get(Collections.binarySearch(bucketList, new HashBucket(id, sid), c));
			BitSet bs1 = bucket.getQidSet();
			BitSet bs2 = super.probeQID[index];
			BitSet result = new BitSet(bs1.size());
//			System.out.println("index=" + index + "|id:" + id + "|has:" + hash(id) + "|bs1:" + bs1.toString() + "|bs2:"
//					+ bs2.toString());
			result.or(bs1);
			result.and(bs2);
			if (!result.isEmpty()) {
				System.out.println(result.toString() + "|R:" + bucket.getRecordPtr().toString() + "|S:"
						+ super.largerSet.get(index).toString());
			}

		}
	}
}
