package beamline.miners.softconformance.algorithms;

import beamline.miners.softconformance.models.pdfa.PDFA;
import beamline.miners.softconformance.models.pdfa.PDFAEdge;
import beamline.miners.softconformance.models.pdfa.PDFANode;

/**
 * This class implements Eq (5) of https://andrea.burattin.net/public-files/publications/2022-arxiv.pdf
 * 
 * @author Andrea Burattin
 */
public class WeightsNormalizer {
	
	public static PDFA normalize(PDFA pdfa, Double alpha) {
		
		PDFA newPdfa = pdfa.getNewCopy();
		newPdfa.setWeightFactor(alpha);
		
		// update old connections
		double ratio = 1d / newPdfa.getNodes().size();
		for (PDFAEdge edge : newPdfa.getEdges()) {
			double value = edge.getProbability();
			edge.setProbability((alpha * value) + ((1d - alpha) * ratio));
		}
		
		// add rest of connections
		for (PDFANode source : newPdfa.getNodes()) {
			for (PDFANode target : newPdfa.getNodes()) {
				if (newPdfa.findEdge(source.getLabel(), target.getLabel()) == null) {
					// there's need to add this edge
					newPdfa.addEdge(source.getLabel(), target.getLabel(), (1d - alpha) * ratio);
				}
			}
		}
		
		return newPdfa;
	}
}
