package beamline.miners.softconformance.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import beamline.miners.softconformance.models.pdfa.PDFA;

public class SoftConformanceTracker extends HashMap<String, SoftConformanceStatus> {

	private static final long serialVersionUID = -2976171768156311061L;
	private Queue<String> caseIdHistory;
	private PDFA model;
	private int maxCasesToStore;
	
	public SoftConformanceTracker(PDFA model) {
		this(model, 1000);
	}
	
	public SoftConformanceTracker(PDFA model, int maxCasesToStore) {
		this.caseIdHistory = new LinkedList<>();
		this.model = model;
		this.maxCasesToStore = maxCasesToStore;
	}
	
	public SoftConformanceStatus replay(String caseId, String newEventName) {
		if (containsKey(caseId)) {
			// now we can perform the replay
			get(caseId).replayEvent(newEventName);
			// need to refresh the cache
			caseIdHistory.remove(caseId);
		} else {
			// check if we can store the new case
			if (caseIdHistory.size() >= maxCasesToStore) {
				// we have no room for the case, we need to remove the case id
				// with most far update time
				String toRemove = caseIdHistory.poll();
				remove(toRemove);
			}
			// now we can perform the replay
			SoftConformanceStatus cs = new SoftConformanceStatus(model, caseId);
			cs.replayEvent(newEventName);
			put(caseId, cs);

		}
		// put the replayed case as first one
		caseIdHistory.add(caseId);

		return get(caseId);
	}
	
	public SoftConformanceReport getReport() {
		SoftConformanceReport report = new SoftConformanceReport();
		for(Entry<String, SoftConformanceStatus> e : entrySet()) {
			report.put(e.getKey(), e.getValue());
		}
		return report;
	}
}

