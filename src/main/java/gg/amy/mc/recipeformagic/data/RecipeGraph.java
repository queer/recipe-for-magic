package gg.amy.mc.recipeformagic.data;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import gg.amy.mc.recipeformagic.RecipeForMagic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;

/**
 * @author amy
 * @since 5/20/23.
 */
@SuppressWarnings("UnstableApiUsage")
public class RecipeGraph {
    private MutableValueGraph<Item, Recipe<?>> graph;
    
    public RecipeGraph reset() {
        graph = ValueGraphBuilder.directed().allowsSelfLoops(true).build();
        return this;
    }
    
    public void buildGraph(final RecipeManager manager) {
        for(final Recipe<?> recipe : manager.values()) {
            final var output = recipe.getOutput();
            if(!graph.nodes().contains(output.getItem())) {
                graph.addNode(output.getItem());
            }
            for(final Ingredient ingredient : recipe.getIngredients()) {
                for(final ItemStack input : ingredient.getMatchingStacks()) {
                    if(!graph.nodes().contains(input.getItem())) {
                        graph.addNode(input.getItem());
                    }
                    graph.putEdgeValue(input.getItem(), output.getItem(), recipe);
                    RecipeForMagic.LOGGER.debug("added edge {} -> {} for recipe {}", input, output, recipe.getId());
                }
            }
        }
        
        RecipeForMagic.LOGGER.info("built recipe graph with {} vertices and {} edges", graph.nodes().size(), graph.edges().size());
    }
    
    public RecipeSearchResults queryGraphWithDepth(final Identifier query, final int depth) {
        final var subgraph = new RecipeSearchResults(query, doQueryGraph(query.getPath(), true, true));
        if(!subgraph.layers().isEmpty()) {
            if(subgraph.layers().size() == 1) {
                // TODO: Figure out if this is an up layer or a down layer
                throw new UnsupportedOperationException("TODO: Figure out single layer; layer = " + subgraph.layers().get(0));
            } else {
                final var upLayer = subgraph.layers().get(0);
                final var downLayer = subgraph.layers().get(subgraph.layers().size() - 1);
                var upDepth = 0;
                var downDepth = 0;
    
                while(upDepth < depth) {
                    for(final var node : upLayer) {
                        final var nextQuery = Registry.ITEM.getId(node.node).getPath();
                        final var nextGraph = doQueryGraph(nextQuery, true, false);
                        if(subgraph.results.edges().isEmpty()) {
                            continue;
                        }
                        for(final var nextNode : nextGraph.nodes()) {
                            subgraph.results.addNode(nextNode);
                        }
                        for(final EndpointPair<Item> edge : nextGraph.edges()) {
                            subgraph.results.putEdgeValue(edge, nextGraph.edgeValue(edge).orElseThrow());
                        }
                    }
                    ++upDepth;
                }
    
                while(downDepth < depth) {
                    for(final var node : downLayer) {
                        final var nextQuery = Registry.ITEM.getId(node.node).getPath();
                        final var nextGraph = doQueryGraph(nextQuery, false, true);
                        if(subgraph.results.edges().isEmpty()) {
                            continue;
                        }
                        for(final var nextNode : nextGraph.nodes()) {
                            subgraph.results.addNode(nextNode);
                        }
                        for(final EndpointPair<Item> edge : nextGraph.edges()) {
                            subgraph.results.putEdgeValue(edge, nextGraph.edgeValue(edge).orElseThrow());
                        }
                    }
                    ++downDepth;
                }
            }
        }
        
        // Tag every edge that directly connects to the query node
        final var queryNode = Registry.ITEM.get(query);
        for(final var edge : subgraph.results.edges()) {
            if(edge.nodeU().equals(queryNode) || edge.nodeV().equals(queryNode)) {
                final var oldValue = subgraph.results.edgeValue(edge).orElseThrow();
                subgraph.results.putEdgeValue(edge, new RecipeMetadata(oldValue.recipe(), true));
            }
        }
        
        return subgraph;
    }
    
    private MutableValueGraph<Item, RecipeMetadata> doQueryGraph(final String query, final boolean inputs, final boolean outputs) {
        final var matchingStacks = graph.nodes().stream()
                .filter(stack -> Registry.ITEM.getId(stack).toString().contains(query)
                        || stack.getName().asString().contains(query))
                .toList();
        
        final List<? extends Recipe<?>> matchingInputs = inputs ? matchingStacks.stream()
                // get all nodes that can be incoming
                .flatMap(stack -> graph.predecessors(stack).stream().map(predecessor -> new RecipeNodeHolder(stack, predecessor)))
                // get the edge between the input nodes and the desired node
                .map(input -> graph.edgeValue(input.predecessor, input.vertex).orElseThrow())
                // remove duplicates
                .distinct()
                .toList() : List.of();
        final List<? extends Recipe<?>> matchingOutputs = outputs ? matchingStacks.stream()
                // get all nodes that can be outgoing
                .flatMap(stack -> graph.successors(stack).stream().map(successor -> new RecipeNodeHolder(stack, successor)))
                // get the edge between the input nodes and the desired node
                .map(output -> graph.edgeValue(output.vertex, output.predecessor).orElseThrow())
                // remove duplicates
                .distinct()
                .toList() : List.of();
        
        final var response = new GraphQueryResponse(matchingStacks, matchingInputs, matchingOutputs);
        
        final MutableValueGraph<Item, RecipeMetadata> out = ValueGraphBuilder.directed().allowsSelfLoops(true).build();
        for(final var stack : response.matchingStacks()) {
            out.addNode(stack);
        }
        
        // For each matching input, add its ingredients to the graph, pointing to its output
        for(final var input : response.inputs()) {
            for(final var ingredient : input.getIngredients()) {
                for(final var stack : ingredient.getMatchingStacks()) {
                    out.addNode(stack.getItem());
                    out.putEdgeValue(stack.getItem(), input.getOutput().getItem(), new RecipeMetadata(input, false));
                }
            }
        }
        
        // For each matching input, add its outputs to the graph, pointing to its ingredients
        for(final var output : response.outputs) {
            out.addNode(output.getOutput().getItem());
            for(final var ingredient : output.getIngredients()) {
                for(final var stack : ingredient.getMatchingStacks()) {
                    out.putEdgeValue(stack.getItem(), output.getOutput().getItem(), new RecipeMetadata(output, false));
                }
            }
        }
        
        return out;
    }
    
    private record GraphQueryResponse(List<Item> matchingStacks, List<? extends Recipe<?>> inputs,
                                      List<? extends Recipe<?>> outputs) {
    }
    
    public record RecipeSearchResults(Identifier target, MutableValueGraph<Item, RecipeMetadata> results) {
        public List<List<LayerNode>> layers() {
            final var graph = Graphs.copyOf(results);
            final List<List<LayerNode>> layers = new LinkedList<>();
            while(!graph.nodes().isEmpty()) {
                final var nodesWithNoOutputs = graph.nodes()
                        .stream()
                        .filter(node -> graph.incidentEdges(node)
                                .stream()
                                .allMatch(edge -> edge.nodeV().equals(node)))
                        .toList();
                
                final List<LayerNode> layer = new LinkedList<>();
                for(final var node : nodesWithNoOutputs) {
                    final var outputs = graph.incidentEdges(node)
                            .stream()
                            .map(graph::edgeValue)
                            .map(Optional::orElseThrow)
                            .toList();
                    layer.add(new LayerNode(node, outputs));
                }
                
                if(layer.isEmpty() && !graph.nodes().isEmpty()) {
                    layer.addAll(graph.nodes().stream().map(node -> new LayerNode(node, new ArrayList<>())).toList());
                    break;
                }
                
                layers.add(layer);
                layer.forEach(node -> graph.removeNode(node.node()));
            }
            // Since we collect layers from the no-output direction, we have to
            // then reverse the list so the tree renders as expected.
            Collections.reverse(layers);
            
            return layers;
        }
    }
    
    public record RecipeMetadata(Recipe<?> recipe, boolean isPrimary) {
    }
    
    private record RecipeNodeHolder(Item vertex, Item predecessor) {
    }
    
    public record LayerNode(Item node, List<RecipeMetadata> edges) {
    }
}
