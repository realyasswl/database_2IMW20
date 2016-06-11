package mq;

import java.util.Comparator;

public class HashBucketComparator implements Comparator<Bucket> {

	@Override
	public int compare(Bucket o1, Bucket o2) {
		return (int) (o1.getKey() - o2.getKey());
	}
}