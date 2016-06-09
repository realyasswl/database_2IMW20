package mq;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MQTest {

	public static void main(String[] ss) {
		// MQJoin mq = new MQJoin();
		String joinKey = "id1";
		JSONArray s = null;
		JSONArray l = null;
		JSONParser p = new JSONParser();

		try {
			Object obj = p.parse(new FileReader("data1.json"));
			s = (JSONArray) obj;
			Object obj1 = p.parse(new FileReader("data2.json"));
			l = (JSONArray) obj1;
			AbstractMQJoin mq = new MQJoinAdvanced(s, l, joinKey);
			//mq.build();
			for(int i=0;i<1000;i++){
				// TODO: change 0000 to a random number
				mq.query("value1_0000", "b", i);
			}
//			mq.probe(list, key);
			
			System.out.println("done");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
