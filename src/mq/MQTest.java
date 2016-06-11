package mq;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import naiveJoin.NJoin;

public class MQTest {

	public static void main(String[] ss) {
		// MQJoin mq = new MQJoin();
		String joinKey = "id1";
		JSONArray s = null;
		JSONArray l = null;
		JSONParser p = new JSONParser();

		int queryCount = 100000;

		try {
			Object obj = p.parse(new FileReader("data1.json"));
			s = (JSONArray) obj;
			Object obj1 = p.parse(new FileReader("data2.json"));
			l = (JSONArray) obj1;

			List<TestVO> voList = geneQueryConditions(queryCount);
			TestVO vo = null;

			// mq test begins
			long mqstart = System.currentTimeMillis();
			System.out.println("mqstart at " + mqstart);
			AbstractMQJoin mq = new MQJoinAdvanced(s, l, joinKey);
			mq.build(s, joinKey);
			for (int i = 0; i < voList.size(); i++) {
//				if (i % 1000 == 0) {
//					System.out.println("mq:" + i);
//				}
				vo = voList.get(i);
				mq.query("value1", vo.getRvalue(),null, "value2", vo.getSvalue(),null, i);
			}
			mq.probe(l, joinKey);
			long mqspent = System.currentTimeMillis() - mqstart;
			// mq test ends

			// basic hash join begins
			long njstart = System.currentTimeMillis();
			System.out.println("njstart at " + njstart);
			NJoin nj = new NJoin(s, l, joinKey);
			nj.build(s, joinKey);
			for (int i = 0; i < voList.size(); i++) {
//				if (i % 1000 == 0) {
//					System.out.println("nj:" + i);
//				}
				vo = voList.get(i);
				nj.query("value1", vo.getRvalue(), "value2", vo.getSvalue(), i);
			}
			long njspent = System.currentTimeMillis() - njstart;
			// hash join ends
			System.out.println("done, mqspent=" + mqspent + ",njspent=" + njspent);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static List<TestVO> geneQueryConditions(int count) {
		List<TestVO> result = new ArrayList<TestVO>();
		Random rand = new Random();
		TestVO vo = null;
		for (int i = 0; i < count; i++) {
			vo = new TestVO("value1_" + rand.nextInt(common.Constants.smallerSize),
					"value2_" + rand.nextInt(common.Constants.largerSize));
			result.add(vo);
//			 System.out.println(i + "|" + vo);
		}
		return result;
	}
}
