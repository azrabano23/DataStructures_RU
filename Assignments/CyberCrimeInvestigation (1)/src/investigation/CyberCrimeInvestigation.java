package investigation;

import java.util.ArrayList; 

/*  
 * This class represents a cyber crime investigation.  It contains a directory of hackers, which is a resizing
 * hash table. The hash table is an array of HNode objects, which are linked lists of Hacker objects.  
 * 
 * The class contains methods to add a hacker to the directory, remove a hacker from the directory.
 * You will implement these methods, to create and use the HashTable, as well as analyze the data in the directory.
 * 
 * @author Colin Sullivan
 */
public class CyberCrimeInvestigation {
       
    private HNode[] hackerDirectory;
    private int numHackers = 0; 

    public CyberCrimeInvestigation() {
        hackerDirectory = new HNode[10];
    }

    /**
     * Initializes the hacker directory from a file input.
     * @param inputFile
     */
    public void initializeTable(String inputFile) { 
        // DO NOT EDIT
        StdIn.setFile(inputFile);  
        while(!StdIn.isEmpty()){
            addHacker(readSingleHacker());
        }
    }

    /**
     * Reads a single hackers data from the already set file,
     * Then returns a Hacker object with the data, including 
     * the incident data.
     * 
     * StdIn.setFile() has already been called for you.
     * 
     * @param inputFile The name of the file to read hacker data from.
     */
    public Hacker readSingleHacker() {
        String name = StdIn.readLine();
        String ipHash = StdIn.readLine();
        String location = StdIn.readLine();
        String os = StdIn.readLine();
        String webServer = StdIn.readLine();
        String date = StdIn.readLine();
        String urlHash = StdIn.readLine();
    
        Incident incident = new Incident(os, webServer, date, location, ipHash, urlHash);
        Hacker hacker = new Hacker(name);
        hacker.addIncident(incident);
    
        return hacker;
    }
    

    /**
     * Adds a hacker to the directory.  If the hacker already exists in the directory,
     * instead adds the given Hacker's incidents to the existing Hacker's incidents.
     * 
     * After a new insertion (NOT if a hacker already exists), checks if the number of 
     * hackers in the table is >= table length divided by 2. If so, calls resize()
     * 
     * @param toAdd
     */
    public void addHacker(Hacker toAdd) {
        int index = toAdd.hashCode() % hackerDirectory.length;
        if (index < 0) index += hackerDirectory.length;
    
        HNode current = hackerDirectory[index];
    
        while (current != null) {
            if (current.getHacker().getName().equals(toAdd.getName())) {
                current.getHacker().getIncidents().addAll(toAdd.getIncidents());
                return;
            }
            current = current.getNext();
        }
    
        HNode newNode = new HNode(toAdd);
        if (hackerDirectory[index] == null) {
            hackerDirectory[index] = newNode;
        } else {
            HNode tail = hackerDirectory[index];
            while (tail.getNext() != null) {
                tail = tail.getNext();
            }
            tail.setNext(newNode);
        }
    
        numHackers++;
    
        if (numHackers >= hackerDirectory.length / 2) {
            resize();
        }
    }
    
    

    /**
     * Resizes the hacker directory to double its current size.  Rehashes all hackers
     * into the new doubled directory.
     */
    private void resize() {
        HNode[] oldDirectory = hackerDirectory;
    
        hackerDirectory = new HNode[oldDirectory.length * 2];
    
        numHackers = 0;
    
        for (HNode head : oldDirectory) {
            HNode current = head;
            while (current != null) {
                addHacker(current.getHacker());
                current = current.getNext();
            }
        }
    }
    

    /**
     * Searches the hacker directory for a hacker with the given name.
     * Returns null if the Hacker is not found
     * 
     * @param toSearch
     * @return The hacker object if found, null otherwise.
     */
        public Hacker search(String toSearch) {
            int index = Math.abs(toSearch.hashCode()) % hackerDirectory.length;
        
            HNode current = hackerDirectory[index];
        
            while (current != null) {
                if (current.getHacker().getName().equals(toSearch)) {
                    return current.getHacker(); 
                }
                current = current.getNext();
            }
        
            return null;
        }
        
    /**
     * Removes a hacker from the directory.  Returns the removed hacker object.
     * If the hacker is not found, returns null.
     * 
     * @param toRemove
     * @return The removed hacker object, or null if not found.
     */
    public Hacker remove(String toRemove) {
        int index = Math.abs(toRemove.hashCode()) % hackerDirectory.length;
    
        HNode current = hackerDirectory[index];
        HNode previous = null;
    
        while (current != null) {
            if (current.getHacker().getName().equals(toRemove)) {
                if (previous == null) {
                    hackerDirectory[index] = current.getNext();
                } else {
                    previous.setNext(current.getNext());
                }
    
                numHackers--; 
                return current.getHacker(); 
            }
    
            previous = current;
            current = current.getNext();
        }
    
        return null; 
    }
    
    /**
     * Merges two hackers into one. The first hacker is the one that will be kept.
     * The second hacker is the one that will be removed. The incidents of the second
     * hacker will be added to the first hacker. The second hacker will be removed from
     * the directory.
     * 
     * @param hacker1 The hacker to merge into
     * @param hacker2 The hacker to merge from
     * @return True if the merge was successful, false otherwise.
     */
    public boolean mergeHackers(String hacker1, String hacker2) {
        Hacker h1 = search(hacker1);
        Hacker h2 = search(hacker2);

        if (h1 == null || h2 == null) {
            return false;
        }
    
        Hacker keep, remove;
    
        if (h1.numIncidents() > h2.numIncidents()) {
            keep = h1;
            remove = h2;
        } else if (h2.numIncidents() > h1.numIncidents()) {
            keep = h2;
            remove = h1;
        } else {
            keep = h1;
            remove = h2;
        }
    
        for (Incident inc : remove.getIncidents()) {
            keep.addIncident(inc);
        }
    
        keep.addAlias(remove.getName());
    
        remove(remove.getName());
    
        return true;
    }
    
    /**
     * Gets the top n most wanted Hackers from the directory, and
     * returns them in an arraylist. 
     * 
     * You should use the provided MaxPQ class to do this. You can
     * add all hackers, then delMax() n times, to get the top n hackers.
     * 
     * @param n
     * @return Arraylist containing top n hackers
     */
    public ArrayList<Hacker> getNMostWanted(int n) {
        MaxPQ<Hacker> pq = new MaxPQ<>(); 
        ArrayList<Hacker> mostWanted = new ArrayList<>();
    
        for (int i = 0; i < hackerDirectory.length; i++) {
            HNode current = hackerDirectory[i];
            while (current != null) {
                pq.insert(current.getHacker());
                current = current.getNext();
            }
        }
    
        for (int i = 0; i < n && !pq.isEmpty(); i++) {
            mostWanted.add(pq.delMax());
        }
    
        return mostWanted;
    }
    
    /**
     * Gets all hackers that have been involved in incidents at the given location.
     * 
     * You should check all hackers, and ALL of each hackers incidents.
     * You should not add a single hacker more than once.
     * 
     * @param location
     * @return Arraylist containing all hackers who have been involved in incidents at the given location.
     */
    public ArrayList<Hacker> getHackersByLocation(String location) {
        ArrayList<Hacker> result = new ArrayList<>();
    
        for (int i = 0; i < hackerDirectory.length; i++) {
            HNode current = hackerDirectory[i];
    
            while (current != null) {
                Hacker hacker = current.getHacker();
                ArrayList<Incident> incidents = hacker.getIncidents();
    
                for (Incident inc : incidents) {
                    if (inc.getLocation().equals(location)) {
                        result.add(hacker); // Add hacker if incident matches location
                        break; // Only add hacker once
                    }
                }
    
                current = current.getNext();
            }
        }
    
        return result;
    }    

    /**
     * PROVIDED--DO NOT MODIFY!
     * Outputs the entire hacker directory to the terminal. 
     */
     public void printHackerDirectory() { 
        System.out.println(toString());
    } 

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.hackerDirectory.length; i++) {
            HNode headHackerNode = hackerDirectory[i];
            while (headHackerNode != null) {
                if (headHackerNode.getHacker() != null) {
                    sb.append(headHackerNode.getHacker().toString()).append("\n");
                    ArrayList<Incident> incidents = headHackerNode.getHacker().getIncidents();
                    for (Incident incident : incidents) {
                        sb.append("\t" +incident.toString()).append("\n");
                    }
                }
                headHackerNode = headHackerNode.getNext();
            } 
        }
        return sb.toString();
    }

    public HNode[] getHackerDirectory() {
        return hackerDirectory;
    }
}
