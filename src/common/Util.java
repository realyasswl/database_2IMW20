package common;

public class Util {
	/** list is in a ascending order */
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
}
