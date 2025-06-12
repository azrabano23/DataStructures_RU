package rumaps;

import java.util.*;

/**
 * This class represents the information that can be attained from the Rutgers University Map.
 * 
 * The RUMaps class is responsible for initializing the network, streets, blocks, and intersections in the map.
 * 
 * You will complete methods to initialize blocks and intersections, calculate block lengths, find reachable intersections,
 * minimize intersections between two points, find the fastest path between two points, and calculate a path's information.
 * 
 * Provided is a Network object that contains all the streets and intersections in the map
 * 
 * @author Vian Miranda
 * @author Anna Lu
 */
public class RUMaps {
    
    private Network rutgers;

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Constructor for the RUMaps class. Initializes the streets and intersections in the map.
     * For each block in every street, sets the block's length, traffic factor, and traffic value.
     * 
     * @param mapPanel The map panel to display the map
     * @param filename The name of the file containing the street information
     */
    public RUMaps(MapPanel mapPanel, String filename) {
        StdIn.setFile(filename);
        int numIntersections = StdIn.readInt();
        int numStreets = StdIn.readInt();
        StdIn.readLine();
        rutgers = new Network(numIntersections, mapPanel);
        ArrayList<Block> blocks = initializeBlocks(numStreets);
        initializeIntersections(blocks);

        for (Block block: rutgers.getAdjacencyList()) {
            Block ptr = block;
            while (ptr != null) {
                ptr.setLength(blockLength(block));
                ptr.setTrafficFactor(blockTrafficFactor(block));
                ptr.setTraffic(blockTraffic(block));
                ptr = ptr.getNext();
            }
        }
    }

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Overloaded constructor for testing.
     * 
     * @param filename The name of the file containing the street information
     */
    public RUMaps(String filename) {
        this(null, filename);
    }

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Overloaded constructor for testing.
     */
    public RUMaps() { 
        
    }

    /**
     * Initializes all blocks, given a number of streets.
     * the file was opened by the constructor - use StdIn to continue reading the file
     * @param numStreets the number of streets
     * @return an ArrayList of blocks
     */
    public ArrayList<Block> initializeBlocks(int numStreets) {
        ArrayList<Block> blocks = new ArrayList<>();
    
        for (int i = 0; i < numStreets; i++) {
            String streetName = StdIn.readLine(); 
            int numBlocks = StdIn.readInt(); 
            StdIn.readLine(); // consume newline
    
            for (int j = 0; j < numBlocks; j++) {
                int blockNumber = StdIn.readInt(); 
                int numPoints = StdIn.readInt(); 
                double roadSize = StdIn.readDouble();
                StdIn.readLine(); // consume newline
    
                Block block = new Block();
                block.setStreetName(streetName);
                block.setBlockNumber(blockNumber);
                block.setRoadSize(roadSize);
    
                for (int p = 0; p < numPoints; p++) {
                    int x = StdIn.readInt();
                    int y = StdIn.readInt();
                    Coordinate coord = new Coordinate(x, y);
    
                    if (p == 0) {
                        block.startPoint(coord); 
                    } else {
                        block.nextPoint(coord); 
                    }
    
                    StdIn.readLine();
                }
    
                blocks.add(block);
            }
        }
    
        return blocks;
    }
    
    

    /**
     * This method traverses through each block and finds
     * the block's start and end points to create intersections. 
     * 
     * It then adds intersections as vertices to the "rutgers" graph if
     * they are not already present, and adds UNDIRECTED edges to the adjacency
     * list.
     * 
     * Note that .addEdge(__) ONLY adds edges in one direction (a -> b). 
     */
    public void initializeIntersections(ArrayList<Block> blocks) {
        for (Block block : blocks) {
            ArrayList<Coordinate> coords = block.getCoordinatePoints();
            Coordinate startCoord = coords.get(0);
            Coordinate endCoord = coords.get(coords.size() - 1);
    
            int startIndex = rutgers.findIntersection(startCoord.getX(), startCoord.getY());
            Intersection startIntersection;
            if (startIndex == -1) {
                startIntersection = new Intersection(startCoord);
                rutgers.addIntersection(startIntersection);
                startIndex = rutgers.findIntersection(startCoord.getX(), startCoord.getY());
            } else {
                startIntersection = rutgers.getIntersections()[startIndex];
            }
    
            int endIndex = rutgers.findIntersection(endCoord.getX(), endCoord.getY());
            Intersection endIntersection;
            if (endIndex == -1) {
                endIntersection = new Intersection(endCoord);
                rutgers.addIntersection(endIntersection);
                endIndex = rutgers.findIntersection(endCoord.getX(), endCoord.getY());
            } else {
                endIntersection = rutgers.getIntersections()[endIndex];
            }
    
            Block blockA = block.copy();
            blockA.setFirstEndpoint(startIntersection);
            blockA.setLastEndpoint(endIntersection);
    
            Block blockB = block.copy();
            blockB.setFirstEndpoint(endIntersection);
            blockB.setLastEndpoint(startIntersection);
    
            rutgers.addEdge(startIndex, blockA);
            rutgers.addEdge(endIndex, blockB);
        }
    
        for (Block block : rutgers.getAdjacencyList()) {
            Block ptr = block;
            while (ptr != null) {
                ArrayList<Coordinate> points = ptr.getCoordinatePoints();
                double totalLength = 0.0;
                for (int i = 1; i < points.size(); i++) {
                    Coordinate a = points.get(i - 1);
                    Coordinate b = points.get(i);
                    double dx = b.getX() - a.getX();
                    double dy = b.getY() - a.getY();
                    totalLength += Math.sqrt(dx * dx + dy * dy);
                }
    
                ptr.setLength(totalLength);
                ptr.setTrafficFactor(blockTrafficFactor(ptr));
                ptr.setTraffic(blockTraffic(ptr));
                ptr = ptr.getNext();
            }
        }
    }
    

    /**
     * Calculates the length of a block by summing the distances between consecutive points for all points in the block.
     * 
     * @param block The block whose length is being calculated
     * @return The total length of the block
     */
public double blockLength(Block block) {
            ArrayList<Coordinate> points = block.getCoordinatePoints();
            double totalLength = 0.0;
        
            for (int i = 1; i < points.size(); i++) {
                Coordinate a = points.get(i - 1);
                Coordinate b = points.get(i);
                double dx = b.getX() - a.getX();
                double dy = b.getY() - a.getY();
                totalLength += Math.sqrt(dx * dx + dy * dy);
            }
        
            return totalLength;
        }
        
        //return 0.0; // Replace this line, it is provided so the code compiles
    
    /**
     * Use a DFS to traverse through blocks, and find the order of intersections
     * traversed starting from a given intersection (as source).
     * 
     * Implement this method recursively, using a helper method.
     */
    public ArrayList<Intersection> reachableIntersections(Intersection source) {
        ArrayList<Intersection> visited = new ArrayList<>();
        dfs(source, visited);
        return visited;
    }
    
    private void dfs(Intersection current, ArrayList<Intersection> visited) {
        if (visited.contains(current)) return;
    
        visited.add(current);
        int index = rutgers.findIntersection(current.getCoordinate().getX(), current.getCoordinate().getY());
        Block neighbor = rutgers.adj(index);  
    
        while (neighbor != null) {
            Intersection next = neighbor.other(current);
            dfs(next, visited); 
            neighbor = neighbor.getNext();  
        }
    }
    

    /**
     * Finds and returns the path with the least number of intersections (nodes) from the start to the end intersection.
     * 
     * - If no path exists, return an empty ArrayList.
     * - This graph is large. Find a way to eliminate searching through intersections that have already been visited.
     * 
     * @param start The starting intersection
     * @param end The destination intersection
     * @return The path with the least number of turns, or an empty ArrayList if no path exists
     */
    public ArrayList<Intersection> minimizeIntersections(Intersection start, Intersection end) {
        Queue<Intersection> queue = new Queue<>();
        HashSet<Intersection> visited = new HashSet<>();
        HashMap<Intersection, Intersection> edgeTo = new HashMap<>();
    
        queue.enqueue(start);
        visited.add(start);
        edgeTo.put(start, null); 
    
        while (!queue.isEmpty()) {
            Intersection current = queue.dequeue();
            int index = rutgers.findIntersection(current.getCoordinate().getX(), current.getCoordinate().getY());
            Block neighbor = rutgers.adj(index);
    
            while (neighbor != null) {
                Intersection next = neighbor.other(current);
                if (!visited.contains(next)) {
                    visited.add(next);
                    edgeTo.put(next, current);
                    queue.enqueue(next);
                    if (next.equals(end)) break; 
                }
                neighbor = neighbor.getNext();
            }
        }
    
        ArrayList<Intersection> path = new ArrayList<>();
        if (!edgeTo.containsKey(end)) return path; 
    
        for (Intersection at = end; at != null; at = edgeTo.get(at)) {
            path.add(at);
        }
    
        Collections.reverse(path);
        return path;
    }
    

    /**
     * Finds the path with the least traffic from the start to the end intersection using a variant of Dijkstra's algorithm.
     * The traffic is calculated as the sum of traffic of the blocks along the path.
     * 
     * What is this variant of Dijkstra?
     * - We are using traffic as a cost - we extract the lowest cost intersection from the fringe.
     * - Once we add the target to the done set, we're done. 
     * 
     * @param start The starting intersection
     * @param end The destination intersection
     * @return The path with the least traffic, or an empty ArrayList if no path exists
     */
    public ArrayList<Intersection> fastestPath(Intersection start, Intersection end) {

        Map<Intersection, Double> distance = new HashMap<>();    

        Map<Intersection, Intersection> pred = new HashMap<>();

        Set<Intersection> done = new HashSet<>();        

        ArrayList<Intersection> nodesExp = new ArrayList<>();

        distance.put(start, 0.0);    

        pred.put(start, null);

       nodesExp.add(start);         
        
        while (!nodesExp.isEmpty()) {
            Intersection minVertex = nodesExp.get(0);

            for (int i = 1; i < nodesExp.size(); i++) {
                Intersection inte = nodesExp.get(i);
                if (distance.get(inte) < distance.get(minVertex)) {
                    minVertex = inte;
                }
            }

            nodesExp.remove(minVertex);

            done.add(minVertex);

            if (minVertex.equals(end)) {
                break;
            }

            int ind = rutgers.findIntersection(minVertex.getCoordinate().getX(), minVertex.getCoordinate().getY());


            for (Block b = rutgers.adj(ind); b != null; b = b.getNext()) {

                Intersection nb = b.other(minVertex);

                if (done.contains(nb)) {
                    continue;
                }

                double altdis = distance.get(minVertex) + b.getTraffic();

                boolean dcont = distance.containsKey(nb);

                if (!dcont ||  altdis < distance.get(nb)) {

                    distance.put(nb, altdis);

                    pred.put(nb, minVertex);

                    if (!nodesExp.contains(nb)) {

                        nodesExp.add(nb);

                    }
                }


            }



        }
        ArrayList<Intersection> pathway = new ArrayList<>();

        if (!pred.containsKey(end)){
            return pathway;
        } 

        for (Intersection i = end; i != null; i = pred.get(i)) {
            pathway.add(i);
        }

        Collections.reverse(pathway);
        
        return pathway; // Replace this line, it is provided so the code compiles
    }


    /**
     * Calculates the total length, average experienced traffic factor, and total traffic for a given path of blocks.
     * 
     * You're given a list of intersections (vertices); you'll need to find the edge in between each pair.
     * 
     * Compute the average experienced traffic factor by dividing total traffic by total length.
     *  
     * @param path The list of intersections representing the path
     * @return A double array containing the total length, average experienced traffic factor, and total traffic of the path (in that order)
     */
    public double[] pathInformation(ArrayList<Intersection> path) {
        double totalLen = 0.0; //
        double totalTraffic = 0.0;

        if (path == null || path.size() < 2) {
            return new double[] {0.0, 0.0, 0.0};
        }
        for (int i = 0; i < path.size() - 1; i++) {
            Intersection start = path.get(i);

            Intersection  dest = path.get(i + 1);

            int index = rutgers.findIntersection(start.getCoordinate().getX(), start.getCoordinate().getY());

            Block edge = rutgers.adj(index);

            for (Block b = rutgers.adj(index); b != null; b = b.getNext()) {
                if ((b.getFirstEndpoint().equals(start) && b.getLastEndpoint().equals(dest)) ||
                    (b.getFirstEndpoint().equals(dest) && b.getLastEndpoint().equals(start))) {
            
                    totalLen += b.getLength();

                    totalTraffic += b.getTraffic();

                    break;
                }
            }

        }
        double Tf;
        if (totalLen == 0) {
            Tf = 0;
        } 
        else {
            Tf = totalTraffic / totalLen;
        }
        return new double[] {totalLen, Tf, totalTraffic};
    }
        /**
     * Calculates the Euclidean distance between two coordinates.
     * PROVIDED - do not modify
     * 
     * @param a The first coordinate
     * @param b The second coordinate
     * @return The Euclidean distance between the two coordinates
     */
    private double coordinateDistance(Coordinate a, Coordinate b) {
        // PROVIDED METHOD

        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Calculates and returns a randomized traffic factor for the block based on a Gaussian distribution.
     * 
     * This method generates a random traffic factor to simulate varying traffic conditions for each block:
     * - < 1 for good (faster) conditions
     * - = 1 for normal conditions
     * - > 1 for bad (slower) conditions
     * 
     * The traffic factor is generated with a Gaussian distribution centered at 1, with a standard deviation of 0.2.
     * 
     * Constraints:
     * - The traffic factor is capped between a minimum of 0.5 and a maximum of 1.5 to avoid extreme values.
     * 
     * @param block The block for which the traffic factor is calculated
     * @return A randomized traffic factor for the block
     */
    public double blockTrafficFactor(Block block) {
        double rand = StdRandom.gaussian(1, 0.2);
        rand = Math.max(rand, 0.5);
        rand = Math.min(rand, 1.5);
        return rand;
    }

    /**
     * Calculates the traffic on a block by the product of its length and its traffic factor.
     * 
     * @param block The block for which traffic is being calculated
     * @return The calculated traffic value on the block
     */
    public double blockTraffic(Block block) {
        // PROVIDED METHOD
        
        return block.getTrafficFactor() * block.getLength();
    }

    public Network getRutgers() {
        return rutgers;
    }




    
    








}
