package mq;

import bplus.BplusTree;
import bplus.Command;
import bplus.Wrapped;
import naiveJoin.NJoin;
import naiveJoin.NJoin2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MQTest2 {
	public static void main(String[] ss) {
		// MQJoin mq = new MQJoin();
		String joinKey = "id1";
		String rkey = "value1";
		String skey = "value2";
		JSONArray s = null;
		JSONArray l = null;
		JSONParser p = new JSONParser();

		int queryCount = 9999;

		try {
			Object obj = p.parse(new FileReader("data1.json"));
			s = (JSONArray) obj;
			Object obj1 = p.parse(new FileReader("data2.json"));
			l = (JSONArray) obj1;
			
			List<Extended> sl=new ArrayList<Extended>();
			List<Extended> ll=new ArrayList<Extended>();
			JSONObject o=null;
			for (int i = 0; i < s.size(); i++) {
				o = (JSONObject) s.get(i);
				sl.add(new Extended(o, new BitSet()));
			}
			for (int i = 0; i < l.size(); i++) {
				o = (JSONObject) l.get(i);
				ll.add(new Extended(o, new BitSet()));
			}
			
			BplusTree rtree = BplusTree.build(sl, new MQCommand(rkey));
			BplusTree stree = BplusTree.build(ll, new MQCommand(skey));
			
			List<TestVO> voList = MQTest.geneQueryConditions(queryCount);
			TestVO vo = null;

			// basic hash join begins
			long njstart = System.currentTimeMillis();
			System.out.println("njstart at " + njstart);
			NJoin2 nj = new NJoin2(s, l, joinKey);
			nj.build(s, joinKey);
			for (int i = 0; i < voList.size(); i++) {
//				if (i % 1000 == 0) {
//					System.out.println("nj:" + i);
//				}
				vo = voList.get(i);
				nj.query("value1", vo.getRvalue(),rtree, "value2", vo.getSvalue(),stree, i);
			}
			long njspent = System.currentTimeMillis() - njstart;
			// hash join ends

			// mq test begins
			long mqstart = System.currentTimeMillis();
			System.out.println("mqstart at " + mqstart);
			AbstractMQJoin mq = new MQJoinAdvanced(s, l, joinKey);
			mq.build(sl, joinKey);
			for (int i = 0; i < voList.size(); i++) {
//				if (i % 1000 == 0) {
//					System.out.println("mq:" + i);
//				}
				vo = voList.get(i);
				mq.query("value1", vo.getRvalue(),rtree, "value2", vo.getSvalue(),stree, i);
			}
			mq.probe(ll, joinKey);
			long mqspent = System.currentTimeMillis() - mqstart;
			// mq test ends
			System.out.println("done, mqspent=" + mqspent + ",njspent=" + njspent);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}

class MQCommand implements Command {
	private String key;

	public MQCommand(String key) {
		super();
		this.key = key;
	}

	@Override
	public Comparable getKey(Object c) {
		return (String) ((Extended) c).getObj().get(key);
	}
}