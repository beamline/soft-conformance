package beamline.miners.softconformance.models.pdfa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import beamline.models.responses.Response;
import lombok.Getter;

public class PDFA extends Response {

	private static final long serialVersionUID = -5286828828055525535L;
	private String attributeNameUsed = null;
	private Double weightFactor = null;
	@Getter private Set<PDFANode> nodes = new LinkedHashSet<>();
	@Getter private Set<PDFAEdge> edges = new LinkedHashSet<>();
	
	private final Map<PDFANode, Collection<PDFAEdge>> inEdgeMap = new LinkedHashMap<>();
	private final Map<PDFANode, Collection<PDFAEdge>> outEdgeMap = new LinkedHashMap<>();

	
	public synchronized PDFA getNewCopy() {
		PDFA pdfa = new PDFA();
		pdfa.attributeNameUsed = attributeNameUsed;
		pdfa.weightFactor = weightFactor;
		
		for (PDFANode n : nodes) {
			pdfa.addNode(n.getLabel());
		}
		for (PDFAEdge e : edges) {
			pdfa.addEdge(e.getSource().getLabel(), e.getTarget().getLabel(), e.getProbability());
		}
		
		return pdfa;
	}
	
	public synchronized void setAttributeNameUsed(String attributeNameUsed) {
		this.attributeNameUsed = attributeNameUsed;
	}
	
	public synchronized String getAttributeNameUsed() {
		return attributeNameUsed;
	}

	public Double getWeightFactor() {
		return weightFactor;
	}

	public void setWeightFactor(Double weightFactor) {
		this.weightFactor = weightFactor;
	}
	
	public synchronized double getSeqenceProbability(String source, String target) {
		PDFAEdge e = findEdge(source, target);
		if (e == null) {
			return 0d;
		}
		return e.getProbability();
	}
	
	public synchronized boolean addNode(String label) {
		PDFANode snNode = new PDFANode(label, this);
		if (nodes.add(snNode)) {
			synchronized (inEdgeMap) {
				inEdgeMap.put(snNode, new LinkedHashSet<>());
			}
			synchronized (outEdgeMap) {
				outEdgeMap.put(snNode, new LinkedHashSet<>());
			}
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized boolean addEdge(String fromNodeLabel, String toNodeLaben, double probability) {
		PDFANode source = findNode(fromNodeLabel);
		PDFANode target = findNode(toNodeLaben);
		PDFAEdge trans = new PDFAEdge(source, target, probability);
		if (edges.add(trans)) {
			synchronized (inEdgeMap) {
				Collection<PDFAEdge> collection = inEdgeMap.get(target);
				collection.add(trans);
			}
			synchronized (outEdgeMap) {
				Collection<PDFAEdge> collection = outEdgeMap.get(source);
				collection.add(trans);
			}
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized PDFANode findNode(String identifier) {
		for (PDFANode s : nodes) {
			if (s.getLabel().equals(identifier)) {
				return s;
			}
		}
		return null;
	}
	
	public synchronized PDFAEdge findEdge(String fromSNNode, String toSNNode) {
		for (PDFAEdge t : getEdges(findNode(fromSNNode), findNode(toSNNode), getEdges())) {
			return t;
		}
		return null;
	}
	
	public synchronized PDFAEdge findEdge(String fromSNNode, String toSNNode, double probability) {
		for (PDFAEdge t : getEdges(findNode(fromSNNode), findNode(toSNNode), getEdges())) {
			if (t.getProbability() == probability) {
				return t;
			}
		}
		return null;
	}
	
	protected Collection<PDFAEdge> getEdges(PDFANode source, PDFANode target, Collection<PDFAEdge> collection) {
		Collection<PDFAEdge> s2t = new HashSet<>();
		for (PDFAEdge a : collection) {
			if (a.getSource().equals(source) && a.getTarget().equals(target)) {
				s2t.add(a);
			}
		}
		return Collections.unmodifiableCollection(s2t);
	}
	
	public Collection<PDFAEdge> getOutEdges(PDFANode node) {
		Collection<PDFAEdge> col = outEdgeMap.get(node);
		if (col == null) {
			return Collections.emptyList();
		} else {
			return new ArrayList<PDFAEdge>(col);
		}
	}


	
	/*@Override
	protected AbstractDirectedGraph<PDFANode, PDFAEdge> getEmptyClone() {
		return new PDFA();
	}

	@Override
	protected Map<? extends DirectedGraphElement, ? extends DirectedGraphElement> cloneFrom(
			DirectedGraph<PDFANode, PDFAEdge> graph) {
		assert (graph instanceof PDFA);
		Map<DirectedGraphElement, DirectedGraphElement> mapping = new HashMap<DirectedGraphElement, DirectedGraphElement>();
		
		PDFA orig = (PDFA) graph;
		for (PDFANode n : orig.nodes) {
			addNode(n.getLabel());
			mapping.put(n, findNode(n.getLabel()));
		}
		for (PDFAEdge e : orig.edges) {
			addEdge(e.getSource(), e.getTarget(), e.getProbability());
			mapping.put(e, findEdge(e.getSource(), e.getTarget(), e.getProbability()));
		}
		return mapping;
	}*/

	/*@SuppressWarnings("rawtypes")
	@Override
	public synchronized void removeEdge(DirectedGraphEdge edge) {
		assert (edge instanceof PDFAEdge);
		PDFAEdge t = (PDFAEdge) edge;
		removePDFAEdge(t);
	}
	
	private synchronized PDFAEdge removePDFAEdge(PDFAEdge PDFAEdge) {
		PDFAEdge result = removeNodeFromCollection(edges, PDFAEdge);
		return (result == null ? null : result);
	}

	private synchronized PDFANode removeNode(PDFANode node) {
		PDFANode result = removeNodeFromCollection(nodes, findNode(node));
		return (result == null ? null : result);
	}
	
	protected synchronized <T> T removeNodeFromCollection(Collection<T> collection, T object) {
		for (T toRemove : collection) {
			if (toRemove.equals(object)) {
				collection.remove(toRemove);
				graphElementRemoved(object);
				return toRemove;
			}
		}
		return null;
	}*/

}
