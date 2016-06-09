package mq;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataGenerate {
	public static void main(String[] s) {
		generateData();
	}

	// TODO currently use org.json.JSONArray, should switch to org.json.simple.*
	// later.
	public static void generateData() {
		int number1 = 100;
		int number2 = 2000;
//		DecimalFormat fmt = new DecimalFormat("0000");
		JSONArray tupleList1 = new JSONArray();
		for (int i = 0; i < number1; i++) {
			JSONObject value = new JSONObject();
			value.put("id1", i);
//			value.put("value1", "value1_" + fmt.format(i));
			value.put("value1", "value1_" + i);
			tupleList1.put(value);
		}

		JSONArray tupleList2 = new JSONArray();
		for (int i = 0; i < number2; i++) {
			JSONObject value = new JSONObject();
			// here we have column "id1" in dataset2 as the join key
			value.put("id1", (int) (Math.random() * number1));
//			value.put("value2", "value2_" + fmt.format(i));
			value.put("value2", "value2_" + i);
			value.put("id2", i);
			tupleList2.put(value);
		}
		jsonArray2File(tupleList1, "data1.json");
		jsonArray2File(tupleList2, "data2.json");
	}

	static void jsonArray2File(JSONArray tupleList, String filePath) {
		File file1 = new File(filePath);
		FileWriter writer1 = null;
		try {
			writer1 = new FileWriter(file1);
			writer1.write("[");
			for (int i = 0; i < tupleList.length(); i++) {
				if (i > 0) {
					writer1.write(",\r\n");
				}
				JSONObject row = tupleList.getJSONObject(i);
				writer1.write(row.toString());

			}
			writer1.write("]");
			writer1.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer1 != null) {
				try {
					writer1.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
