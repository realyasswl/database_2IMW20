package mq;

/**
 * <p>
 * we use bitset to represent the query id set(QID set), which means we can use
 * an Integer to do this by transferring this integer into binary format, and
 * each bit records one result.
 * </p>
 * <p>
 * Java actually has java.util.BitSet already.
 * </p>
 */
@Deprecated
public class QIDSet {
	private int v;

	public QIDSet() {
		this.v = 0;
	}

}
