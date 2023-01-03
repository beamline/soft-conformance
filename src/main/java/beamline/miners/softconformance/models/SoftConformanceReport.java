package beamline.miners.softconformance.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import beamline.models.responses.Response;

public class SoftConformanceReport extends Response implements Map<String, SoftConformanceStatus> {

	private static final long serialVersionUID = -6013483211788018759L;
	private Map<String, SoftConformanceStatus> content = new HashMap<>();
	
	@Override
	public int size() {
		return content.size();
	}

	@Override
	public boolean isEmpty() {
		return content.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return content.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return content.containsValue(value);
	}

	@Override
	public SoftConformanceStatus get(Object key) {
		return content.get(key);
	}

	@Override
	public SoftConformanceStatus put(String key, SoftConformanceStatus value) {
		return content.put(key, value);
	}

	@Override
	public SoftConformanceStatus remove(Object key) {
		return content.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends SoftConformanceStatus> m) {
		content.putAll(m);
	}

	@Override
	public void clear() {
		content.clear();
	}

	@Override
	public Set<String> keySet() {
		return content.keySet();
	}

	@Override
	public Collection<SoftConformanceStatus> values() {
		return content.values();
	}

	@Override
	public Set<Entry<String, SoftConformanceStatus>> entrySet() {
		return content.entrySet();
	}

}
