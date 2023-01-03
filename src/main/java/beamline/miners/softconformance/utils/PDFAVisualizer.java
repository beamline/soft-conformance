package beamline.miners.softconformance.utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import beamline.graphviz.Dot;
import beamline.graphviz.Dot.GraphDirection;
import beamline.miners.softconformance.models.pdfa.PDFA;
import beamline.miners.softconformance.models.pdfa.PDFAEdge;
import beamline.miners.softconformance.models.pdfa.PDFANode;
import beamline.graphviz.DotEdge;
import beamline.graphviz.DotNode;

public class PDFAVisualizer {
	
	public static Dot getDot(PDFA pdfa) {
		Dot dot = new Dot();
		dot.setDirection(GraphDirection.leftRight);
		Map<String, DotNode> map = new HashMap<String, DotNode>();
		DecimalFormat f = new DecimalFormat("#.##");

		for (PDFANode node : pdfa.getNodes()) {
			DotNode n = dot.addNode(node.getLabel());
			n.setOption("fontname", "Calibri");
			map.put(node.getLabel(), n);
		}
		
		for (PDFAEdge edge : pdfa.getEdges()) {
			DotEdge e = dot.addEdge(map.get(edge.getSource().getLabel()), map.get(edge.getTarget().getLabel()));
			e.setLabel(f.format(edge.getProbability()));
			e.setOption("penwidth", Double.toString(0.5 + (edge.getProbability() * 5.0)));
			e.setOption("fontname", "Calibri");
		}
		return dot;
	}
}
