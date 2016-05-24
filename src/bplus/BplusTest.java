package bplus;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BplusTest {
	static Integer n = 3000000;
	static Integer[] array = new Integer[n];
	static {
		for (int i = 0; i < n; i++) {
			array[i] = i;
		}
	}

	public static void main(String[] s) {
		testBinarySearch();
		buildBplusAndTest();
	}

	private static void buildBplusAndTest() {
		ShuffleArray(array);
		List<TestForm> iter = new LinkedList<TestForm>();
		DecimalFormat fmt = new DecimalFormat("0000");
		for (int i : array) {
			TestForm f = new TestForm();
			f.setKey(i);
			f.setMessage(fmt.format(i));
			iter.add(f);
		}
		long ts = System.currentTimeMillis();
		BplusTree tree = BplusTree.build(iter, new TestCommand());
		// tree.printAllNode();
		long spent = System.currentTimeMillis() - ts;
		System.out.println("bplus tree built after " + spent);
		ts = System.currentTimeMillis();
		for (Integer i = 0; i < n; i++) {
			Wrapped w = tree.find(i);
		}
		spent = System.currentTimeMillis() - ts;
		System.out.println(spent);
	}

	private static void testBinarySearch() {
		long ts = System.currentTimeMillis();
		for (Integer i = 0; i < n; i++) {
			int index = binarySearch(array, i);
		}
		long spent = System.currentTimeMillis() - ts;
		System.out.println(spent);
	}

	/** array is in a ascending order */
	private static int binarySearch(Comparable[] array, Comparable a) {
		double d = 1d / 2;
		int index = (int) (array.length * d);
		double step = 0;
		int iteration = 1;
		while (index > 0 && index < array.length) {
			iteration++;
			step = 1d / Math.pow(2, iteration);
			int result = array[index].compareTo(a);
			if (result == 0) {
				return index;
			} else if (result > 0) {
				d = d - step;
			} else {
				d = d + step;
			}
			index = (int) (array.length * d);
		}

		int result = array[index].compareTo(a);
		if (result == 0) {
			return index;
		} else {
			return -1;
		}
	}

	private static void ShuffleArray(Integer[] array) {
		int index;
		Random random = new Random();
		for (int i = array.length - 1; i > 0; i--) {
			index = random.nextInt(i + 1);
			if (index != i) {
				array[index] ^= array[i];
				array[i] ^= array[index];
				array[index] ^= array[i];
			}
		}
	}
}

class TestForm {
	public String toString() {
		return message;
	}

	private Integer key;
	private String message;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

class TestCommand implements Command {

	@Override
	public Comparable getKey(Object c) {
		return ((TestForm) c).getKey();
	}
}
