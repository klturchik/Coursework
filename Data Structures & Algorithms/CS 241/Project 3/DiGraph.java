//
//	Name: Turchik, Kyle
//	Project: 3
//	Due: 6/5/2015
//	Course: cs-241-02-s15
//

import java.util.*;

public class DiGraph {
	private static final int NUMBER_OF_CITIES = 20;
	Map<String, Integer> cityNumbers = new Hashtable<String, Integer>();
	Map<Integer, String> cityCodes = new Hashtable<Integer, String>();
	Map<String, String> cityNames = new Hashtable<String, String>();
    private int[][] edges; // edges[i][j] contains the weight of each path from i to j, or 0 if no path exists
    private String[] labels; // labels[i] contains the information for city i

    /** 
     * Constructor: initialize a DiGraph with the default number of vertices, no edges, and null labels
     */
    public DiGraph() {
        edges = new int[NUMBER_OF_CITIES][NUMBER_OF_CITIES]; // All values initially 0
        labels = new String[NUMBER_OF_CITIES]; // All values initially null
    }

    /** 
     * Sets the initial data for all maps and labels
     * @param vertex	the number of a city contained in city.dat
     * @param newLabel	all of the information about the city contained in a single String
     */
    public void setData(int vertex, String newLabel) {
		String[] cityId = newLabel.trim().split(" +");
		int number = Integer.parseInt(cityId[0]) - 1;
		String key = cityId[1];
		cityNumbers.put(key, number);
		cityCodes.put(number, key);
		String name = cityId[2];
		try {
			@SuppressWarnings("unused")
			Integer x = Integer.parseInt(cityId[3]);
		} catch (NumberFormatException e) {
			name = name + " " + cityId[3];
		}
		cityNames.put(key, name);
		
        labels[vertex] = newLabel.trim().replaceAll(" +", " ");
    }

    /** 
     * Accessor method to get all of the information about a city 
     * @param cityCode	the two letter code for the city 
     * @return			a String containing the information
     */
    public String getLabel(String cityCode) {
    	// return error message for invalid city code
       	if(!cityNumbers.containsKey(cityCode))
    		return ("ERROR: " + cityCode + " is not a valid city code");
       	
    	int cityNum = cityNumbers.get(cityCode);
    	
    	return labels[cityNum];
    }

    /** 
     * Add an edge between two vertices
     * @param source	the two letter code for the starting city
     * @param target	the two letter code for the target city
     * @param distance	the weight/distance of the road between both cities
     * @return			a confirmation message (or error message)
     */
    public String addEdge(String source, String target, int distance) {
    	// return error message for invalid city code
    	if(!cityNumbers.containsKey(source))
    		return ("ERROR: " + source + " is not a valid city code");
    	if(!cityNumbers.containsKey(target))
    		return ("ERROR: " + target + " is not a valid city code");
    	
    	// convert city code to vertex number
    	int sourceVertex = cityNumbers.get(source);
    	int targetVertex = cityNumbers.get(target);
    	
    	if(isEdge(sourceVertex, targetVertex)){
    		return ("ERROR: A road already exists from " + cityNames.get(source) + " to " + cityNames.get(target));
    	}
    	else{
    		edges[sourceVertex][targetVertex] = distance;
    		return ("Successfully added a road from " + cityNames.get(source) + " to " + cityNames.get(target) + " with a distance of " + distance);
    	}
    }
    
    /** 
     * Add an edge between two vertices (Used for file input rather than user input!)
     * @param source	the vertex number for the starting city
     * @param target	the vertex number for the target city
     * @param distance	the weight/distance of the road between both cities
     * @return			a confirmation message (or error message)
     */
    public void addEdge(int source, int target, int distance) {
    	edges[source][target] = distance;
    }

    /** 
     * Remove an edge between two vertices
     * @param source	the two letter code for the starting city
     * @param target	the two letter code for the target city
     * @return			a confirmation message (or error message)
     */
    public String removeEdge(String source, String target) {
    	// return error message for invalid city code
    	if(!cityNumbers.containsKey(source))
    		return ("ERROR: " + source + " is not a valid city code");
    	if(!cityNumbers.containsKey(target))
    		return ("ERROR: " + target + " is not a valid city code");
    	
    	// convert city code to vertex number
    	int sourceVertex = cityNumbers.get(source);
    	int targetVertex = cityNumbers.get(target);
    	
    	if(!isEdge(sourceVertex, targetVertex)){
    		return ("ERROR: No road found from " + cityNames.get(source) + " to " + cityNames.get(target));
    	}
    	else{
    		edges[sourceVertex][targetVertex] = 0;
    		return ("Successfully removed the road from " + cityNames.get(source) + " to " + cityNames.get(target));
    	}
    }

    /** 
     * Determine whether an edge exists between two vertices
     * @param sourceVertex	the source vertex number
     * @param targetVertex	the target vertex number
     * @return				true if an edge exists, otherwise false
     */
    public boolean isEdge(int sourceVertex, int targetVertex) {
    	if(edges[sourceVertex][targetVertex] != 0)
    		return true;
    	else
    		return false;
    }

    /** 
     * Obtain a list of neighbors for a specified vertex
     * @param vertex	the vertex number
     * @return			an array containing the numbers of all vertices adjacent to the specified vertex
     */
    public int[] neighbors(int vertex) {
        int i;
        int count = 0;
        int[] answer;

        for (i = 0; i < labels.length; i++) {
            if (isEdge(vertex, i))
                count++;
        }
        answer = new int[count];
        count = 0;
        for (i = 0; i < labels.length; i++) {
            if (isEdge(vertex, i))
                answer[count++] = i;
        }
        return answer;
    }
    
    /** 
     * Find the shortest path from source to target using dijkstra's algorithm 
     * @param source	the two letter code for the starting city
     * @param target	the two letter code for the target city
     * @return			a String containing the distance of the shortest path and all the vertices along that path
     */
    public String dijkstra(String source, String target) {
    	// return error message for invalid city code
    	if(!cityNumbers.containsKey(source))
    		return ("ERROR: " + source + " is not a valid city code");
    	if(!cityNumbers.containsKey(target))
    		return ("ERROR: " + target + " is not a valid city code");
    	
    	// convert city code to vertex number
    	int sourceVertex = cityNumbers.get(source);
    	int targetVertex = cityNumbers.get(target);
    	
    	// priority queue
        List<Integer> q = new ArrayList<>();
    	// dist[] holds the distances of all vertices from the source
    	int[] dist = new int[NUMBER_OF_CITIES];
    	// prev[] holds the previous vertex with shortest distance on the path to each vertex 
    	int[] prev = new int[NUMBER_OF_CITIES];
    	
    	// the initial distance of all vertices is infinity
    	for(int i=0; i<dist.length; i++){
    		dist[i] = Integer.MAX_VALUE;
    	}
    	
    	// the distance from source to itself is 0
    	dist[sourceVertex] = 0; 
    	
    	// get neighbors of the source vertex
    	int[] neighbors = neighbors(sourceVertex);
    	// set distance of neighbors to weight of their edges
    	for(int i=0; i<neighbors.length; i++){
    		dist[neighbors[i]] = edges[sourceVertex][neighbors[i]];
    		// add neighbors to the queue
    		q.add(neighbors[i]);
    		// set previous vertex of all neighbors
    		prev[neighbors[i]] = sourceVertex;
    	}
    	
    	// while the queue is not empty
    	while(!q.isEmpty()){
    		// remove vertex with shortest distance from the queue
    		int minSize = dist[q.get(0)];
    		int minVertex = q.get(0);
    		for(int i=0; i<q.size(); i++){
    			if(dist[q.get(i)] < minSize){
    				minSize = dist[q.get(i)];
    				minVertex = q.get(i);
    			}
    		}
    		q.remove(new Integer(minVertex));
    		
    		// for each neighbor of the vertex with shortest distance 
    		neighbors = neighbors(minVertex);
        	for(int i=0; i<neighbors.length; i++){
        		// if neighbor's distance = infinity
        		if(dist[neighbors[i]] == Integer.MAX_VALUE){
        			dist[neighbors[i]] = dist[minVertex] + edges[minVertex][neighbors[i]];
        			// add neighbors to the queue
        			q.add(neighbors[i]);
        			// set previous vertex of all neighbors
        			prev[neighbors[i]] = minVertex;
        		}
        		// if neighbor's distance > new distance (neighbor has already been visited)
        		else{
        			if(dist[minVertex] + edges[minVertex][neighbors[i]] < dist[neighbors[i]]){
        				dist[neighbors[i]] = dist[minVertex] + edges[minVertex][neighbors[i]];
        				// set previous vertex of all neighbors
        				prev[neighbors[i]] = minVertex;
        			}
        		}
        	}

    	}
    	
    	// if distance to target vector is still infinity (e.g. source is not connected to target)
    	if(dist[targetVertex] == Integer.MAX_VALUE){
    		return ("ERROR: No path found between " + cityNames.get(source) + " and " + cityNames.get(target));
    	}
    	
    	// create list of vertices on path from source to target
    	List<String> path = new ArrayList<>();
    	path.add(cityCodes.get(targetVertex));
    	int i = targetVertex;
    	while(i != sourceVertex){
    		path.add(cityCodes.get(prev[i]));
		i = prev[i];
    	}
    	Collections.reverse(path);
    	
    	return ("The minimum distance between " + cityNames.get(source) + " and " + cityNames.get(target) + " is " + dist[targetVertex] + " through the route: " + path);
    }
}