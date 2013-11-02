package edu.columbia.cs.event.pipeline.component;

import edu.columbia.cs.event.graph.EventVertex;
import edu.columbia.cs.event.graph.SimplePrune;
import edu.columbia.cs.event.graph.WeightedEdge;
import edu.columbia.cs.event.graph.scorer.AggregateEdgeScorer;
import edu.columbia.cs.event.graph.scorer.EdgeScorer;
import edu.columbia.cs.event.graph.scorer.WordNetSubsumption;
import edu.columbia.cs.event.pipeline.TimeUnit;
import edu.columbia.cs.event.annotations.Cluster;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.filters.FilterUtils;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import net.sf.extjwnl.data.POS;
import org.apache.commons.collections15.Factory;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 11/1/13
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class GraphBuilder implements PipelineComponent {

    private AggregateEdgeScorer edgeScorer = new AggregateEdgeScorer();

    public void init() {

        WordNetSubsumption verbSubsumption = new WordNetSubsumption(POS.VERB, "VB[^Z]");
        WordNetSubsumption commonNounSubsumption = new WordNetSubsumption(POS.NOUN, "^NN$|^NNS$");
        commonNounSubsumption.setNormalized(true);

        edgeScorer.addEdgeScorer(verbSubsumption);
        edgeScorer.addEdgeScorer(commonNounSubsumption);
    }


    public void processTimeUnit(TimeUnit timeUnit) {

        int numVertices = timeUnit.getClusters().size();

        Graph<EventVertex, WeightedEdge> g = new DirectedSparseMultigraph<EventVertex, WeightedEdge>();
        List<EventVertex> vertices = new ArrayList<EventVertex>(numVertices);

        for (Cluster cluster : timeUnit.getClusters())
            vertices.add(new EventVertex(cluster));




        int numEdges = 0;

        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {

                double edgeScore = 0;

                edgeScore += edgeScorer.score(vertices.get(i), vertices.get(j));

                if (edgeScore > 0) {
                    g.addEdge(new WeightedEdge(edgeScore),
                            vertices.get(i),
                            vertices.get(j),
                            EdgeType.DIRECTED);
                } else if (edgeScore < 0 ) {
                    g.addEdge(new WeightedEdge(-edgeScore),
                            vertices.get(j),
                            vertices.get(i),
                            EdgeType.DIRECTED);

                }

            }
        }

        SimplePrune pruner = new SimplePrune();
        g = pruner.pruneGraph(g);

        WeakComponentClusterer<EventVertex,WeightedEdge> clusterer =
                new WeakComponentClusterer<EventVertex, WeightedEdge>();
        Set<Set<EventVertex>> clusters = clusterer.transform(g);

        Set<EventVertex> maxCluster = null;
        int maxClusterSize = 0;

        for (Set<EventVertex> cluster : clusters) {
            if (maxClusterSize < cluster.size()) {
                maxCluster = cluster;
                maxClusterSize = cluster.size();
            }

        }

        Graph<EventVertex,WeightedEdge> sg = FilterUtils.createInducedSubgraph(maxCluster, g);

        displayGraph(g, "Pruned Graph");
        displayGraph(sg, "Largest Pruned SubGraph");

    }

    public void displayGraph(Graph<EventVertex,WeightedEdge> g, String title) {

        Layout<EventVertex,WeightedEdge> layout = new SpringLayout2<EventVertex, WeightedEdge>(g);
        layout.setSize(new Dimension(400,400));
        VisualizationViewer<EventVertex,WeightedEdge> vv =
                new VisualizationViewer<EventVertex,WeightedEdge>(layout);
        vv.setPreferredSize(new Dimension(500,500));
        // Show vertex and edge labels
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        //vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        // Create a graph mouse and add it to the visualization viewer
        //

        Factory<EventVertex> vertexFactory = new Factory<EventVertex>() {
            @Override
            public EventVertex create() {
                EventVertex v = new EventVertex();
                v.setLabel("empty cluster");

                return v;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        Factory<WeightedEdge> edgeFactory = new Factory<WeightedEdge>() {
            @Override
            public WeightedEdge create() {
                return new WeightedEdge(0);  //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        EditingModalGraphMouse gm = new EditingModalGraphMouse(vv.getRenderContext(),
                vertexFactory,
                edgeFactory
        );
        // new EditingModalGraphMouse(vv.getRenderContext(),
        //         sgv.vertexFactory, sgv.edgeFactory);
        vv.setGraphMouse(gm);
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        // Let's add a menu for changing mouse modes
        JMenuBar menuBar = new JMenuBar();
        JMenu modeMenu = gm.getModeMenu(); // Obtain mode menu from the mouse
        modeMenu.setText("Mouse Mode");
        modeMenu.setIcon(null); // I'm using this in a main menu
        modeMenu.setPreferredSize(new Dimension(100,100)); // Change the size
        menuBar.add(modeMenu);
        frame.setJMenuBar(menuBar);
        gm.setMode(ModalGraphMouse.Mode.PICKING); // Start off in editing mode
        frame.pack();
        frame.setVisible(true);


    }


}
