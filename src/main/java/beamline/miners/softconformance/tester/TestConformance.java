package beamline.miners.softconformance.tester;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import beamline.events.BEvent;
import beamline.miners.softconformance.algorithms.PDFAConformance;
import beamline.miners.softconformance.models.SoftConformanceReport;
import beamline.miners.softconformance.models.pdfa.PDFA;
import beamline.sources.StringTestSource;

public class TestConformance {
	
	public static void main(String[] args) throws Exception {
		
		PDFA reference = new PDFA();
		reference.addNode("A");
		reference.addNode("B");
		reference.addNode("C");
		reference.addEdge("A", "A", 0.2);
		reference.addEdge("A", "B", 0.8);
		reference.addEdge("B", "C", 1);
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env
			.addSource(new StringTestSource("ABCDE", "ABCKE"))
			.keyBy(BEvent::getProcessName)
			.flatMap(new PDFAConformance(reference, 0.5, 100).setResultsRefreshRate(2))
			.addSink(new SinkFunction<SoftConformanceReport>(){
				private static final long serialVersionUID = 8175737646028240418L;
				@Override
				public void invoke(SoftConformanceReport value, Context context) throws Exception {
					for(String caseId : value.keySet()) {
						System.out.println("Case: " + caseId + "\tsoft conformance: " + value.get(caseId).getSoftConformance() + "\tmean of probs: " + value.get(caseId).getMeanProbabilities());
					}
					System.out.println("-----");
					System.out.flush();
				}
			});
		env.execute();
	}
}
