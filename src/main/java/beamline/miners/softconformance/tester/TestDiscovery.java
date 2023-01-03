package beamline.miners.softconformance.tester;

import java.io.File;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import beamline.events.BEvent;
import beamline.miners.softconformance.algorithms.PDFAMiner;
import beamline.miners.softconformance.models.pdfa.PDFA;
import beamline.miners.softconformance.utils.PDFAVisualizer;
import beamline.sources.StringTestSource;

public class TestDiscovery {
	
	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env
			.addSource(new StringTestSource("AABC", "ABC", "ABC", "ABC"))
			.keyBy(BEvent::getProcessName)
			.flatMap(new PDFAMiner().setModelRefreshRate(1))
			.addSink(new SinkFunction<PDFA>(){
				private static final long serialVersionUID = 5305853831656162136L;

				@Override
				public void invoke(PDFA value, Context context) throws Exception {
					System.out.println(value.getProcessedEvents());
					PDFAVisualizer.getDot(value).exportToSvg(new File("test.svg"));
				}
			});
		env.execute();
	}
}
