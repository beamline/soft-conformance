package beamline.miners.softconformance.models.pdfa;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.Getter;

public class PDFAEdge implements Serializable {

	private static final long serialVersionUID = -1656536907446074140L;
	private double probability;
	@Getter private PDFANode source;
	@Getter private PDFANode target;
	
	public PDFAEdge(PDFANode source, PDFANode target, double probability) {
		this.source = source;
		this.target = target;
		this.probability = probability;
	}
	
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	public double getProbability() {
		return probability;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PDFAEdge) {
			PDFAEdge e = (PDFAEdge) o;
			return new EqualsBuilder()
					.append(getSource(), e.getSource())
					.append(getTarget(), e.getTarget())
					.isEquals();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(getSource())
				.append(getTarget())
				.toHashCode();
	}
	
	@Override
	public String toString() {
		return source + " -> " + target + " (" + probability + ")";
	}
}
