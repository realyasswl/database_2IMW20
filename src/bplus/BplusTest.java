package bplus;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BplusTest {
	public static void main(String[] s) {
//		int n = 20;
//		int[] array = new int[n];
//		for (int i = 0; i < n; i++) {
//			array[i] = i;
//		}
//		ShuffleArray(array);
		int[] array={5,14,12,0,8,13,10,3,17,1,9,6,19,7,18};
		List<TestForm> iter = new LinkedList<TestForm>();
		DecimalFormat fmt = new DecimalFormat("0000");
		for (int i : array) {
			TestForm f = new TestForm();
			f.setKey(i);
			f.setMessage(fmt.format(i));
			iter.add(f);
		}
		BplusTree tree = BplusTree.build(iter, new TestCommand());
		tree.printAllNode();
		Wrapped w = tree.find(5);
	}

	private static void ShuffleArray(int[] array) {
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
