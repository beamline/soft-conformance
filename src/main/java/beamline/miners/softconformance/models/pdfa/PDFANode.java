package beamline.miners.softconformance.models.pdfa;

import java.io.Serializable;

import lombok.Getter;

public class PDFANode implements Serializable {

	private static final long serialVersionUID = 6709549464431219608L;
	@Getter private PDFA graph;
	@Getter private String label;
	
	public PDFANode(String label, PDFA graph) {
		this.graph = graph;
		this.label = label;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof PDFANode && label.equals(((PDFANode) o).label));
	}
	
	@Override
	public int hashCode() {
		return label.hashCode();
	}
	
	@Override
	public String toString() {
		return label;
	}
}
