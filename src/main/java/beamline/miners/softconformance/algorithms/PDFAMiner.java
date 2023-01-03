package beamline.miners.softconformance.algorithms;

import java.util.HashMap;
import java.util.Map;

import beamline.events.BEvent;
import beamline.miners.softconformance.models.pdfa.PDFA;
import beamline.miners.softconformance.models.pdfa.PDFAEdge;
import beamline.miners.softconformance.models.pdfa.PDFANode;
import beamline.models.algorithms.StreamMiningAlgorithm;

public class PDFAMiner extends StreamMiningAlgorithm<PDFA> {

	private static final long serialVersionUID = 3334439954689982814L;
	
	private String attributeForDiscovery;
	private Double alpha = 0.5;
	private int modelRefreshRate = 10;
	
	private Map<String, String> previousActivity = new HashMap<>();
	private PDFA pdfa = new PDFA();
	
	/**
	 * Use the activity name for the discovery
	 */
	public PDFAMiner() {
		this(null);
	}
	
	public PDFAMiner(String attributeForDiscovery) {
		this.attributeForDiscovery = attributeForDiscovery;
	}
	
	public PDFAMiner setModelRefreshRate(int modelRefreshRate) {
		this.modelRefreshRate = modelRefreshRate;
		return this;
	}
	
	public PDFAMiner setAlpha(Double alpha) {
		this.alpha = alpha;
		return this;
	}
	
	@Override
	public PDFA ingest(BEvent event) {
		String caseID = event.getTraceName();
		String activityName = attributeForDiscovery == null? event.getEventName() : event.getProcessAttributes().get(attributeForDiscovery).toString();

		observeEvent(caseID, activityName);
		if (getProcessedEvents() % modelRefreshRate == 0) {
			return getModel();
		}
		
		return null;
	}

	private void observeEvent(String caseID, String activityName) {
		String prevEvent = previousActivity.get(caseID);
		if (pdfa.findNode(activityName) == null) {
			pdfa.addNode(activityName);
		}
		if (prevEvent != null) {
			PDFAEdge edge = pdfa.findEdge(prevEvent, activityName);
			if (edge == null) {
				pdfa.addEdge(prevEvent, activityName, 1);
			} else {
				edge.setProbability(edge.getProbability() + 1);
			}
		}
		previousActivity.put(caseID, activityName);
	}

	private PDFA getModel() {
		Map<PDFANode, Double> sums = new HashMap<PDFANode, Double>();
		for (PDFANode source : pdfa.getNodes()) {
			double sum = 0;
			for (PDFAEdge out : pdfa.getOutEdges(source)) {
				sum += out.getProbability();
			}
			sums.put(source, sum);
		}
		
		
		// create new PDFA
		PDFA normalizedPdfa = new PDFA();
		for(PDFANode node : pdfa.getNodes()) {
			normalizedPdfa.addNode(node.getLabel());
		}
		for (PDFAEdge edge : pdfa.getEdges()) {
			normalizedPdfa.addEdge(
					edge.getSource().getLabel(),
					edge.getTarget().getLabel(),
					edge.getProbability() / sums.get(edge.getSource()));
		}
		return WeightsNormalizer.normalize(normalizedPdfa, alpha);
	}
}
