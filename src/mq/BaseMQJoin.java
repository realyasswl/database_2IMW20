package mq;

import java.util.List;

import org.json.simple.JSONArray;

public class BaseMQJoin extends AbstractMQJoin {
	BaseMQJoin(JSONArray smallerSet, JSONArray largerSet, String joinKey) {
		super.set(smallerSet, largerSet, joinKey);
	}

	@Override
	public void build(List<Extended> list, String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void probe(List<Extended> list, String key) {
		// TODO Auto-generated method stub

	}

}
