package mq;

import java.util.Comparator;

public class HashBucketComparator implements Comparator<HashBucket> {

	@Override
	public int compare(HashBucket o1, HashBucket o2) {
		return (int) (o1.getKey() - o2.getKey());
	}
}