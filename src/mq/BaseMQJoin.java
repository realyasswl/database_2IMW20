package mq;

import org.json.simple.JSONArray;

public class BaseMQJoin extends AbstractMQJoin {
	BaseMQJoin(JSONArray smallerSet, JSONArray largerSet, String joinKey) {
		super.set(smallerSet, largerSet, joinKey);
	}

	@Override
	public void build(JSONArray list, String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void probe(JSONArray list, String key) {
		// TODO Auto-generated method stub

	}

}
