package beamline.miners.softconformance.algorithms;

import beamline.events.BEvent;
import beamline.miners.softconformance.models.SoftConformanceReport;
import beamline.miners.softconformance.models.SoftConformanceTracker;
import beamline.miners.softconformance.models.pdfa.PDFA;
import beamline.models.algorithms.StreamMiningAlgorithm;

public class PDFAConformance extends StreamMiningAlgorithm<SoftConformanceReport> {

	private static final long serialVersionUID = 7119224165246591983L;

	private SoftConformanceTracker tracker = null;
	private String attributeForDiscovery = null;
	private int resultsRefreshRate = 10;
	
	public PDFAConformance(PDFA model, Double alpha, int maxCasesToStore) {
		model = WeightsNormalizer.normalize(model, alpha);
		this.tracker = new SoftConformanceTracker(model, maxCasesToStore);
	}
	
	public PDFAConformance(PDFA normalizedModel, int maxCasesToStore) {
		this.tracker = new SoftConformanceTracker(normalizedModel, maxCasesToStore);
	}
	
	public PDFAConformance setResultsRefreshRate(int resultsRefreshRate) {
		this.resultsRefreshRate = resultsRefreshRate;
		return this;
	}
	
	/**
	 * 
	 * @param attributeForDiscovery if null, use the activity name
	 * @return
	 */
	public PDFAConformance setAttributeForDiscovery(String attributeForDiscovery) {
		this.attributeForDiscovery = attributeForDiscovery;
		return this;
	}
	
	@Override
	public SoftConformanceReport ingest(BEvent event) {
		String caseID = event.getTraceName();
		String activityName = attributeForDiscovery == null? event.getEventName() : event.getProcessAttributes().get(attributeForDiscovery).toString();
		
		tracker.replay(caseID, activityName);
		if (getProcessedEvents() % resultsRefreshRate == 0) {
			return tracker.getReport();
		}
		
		return null;
	}
}
