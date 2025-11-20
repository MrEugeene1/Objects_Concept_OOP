// Simulation.java
import java.util.*;

public class Simulation {
    private Map map;
    private List<LivingBeing> individuals;
    private List<Master> masters;
    private RandomNumberGenerator randomGenerator;
    private int timeStep;
    private int maxTimeSteps;
    private boolean running;
    
    // Game parameters
    private static final int NUM_INDIVIDUALS_PER_POPULATION = 5;
    private static final int MAP_WIDTH = 20;
    private static final int MAP_HEIGHT = 20;
    private static final int MAX_TIME_STEPS = 1000;
    
    public Simulation() {
        this.map = new Map(MAP_WIDTH, MAP_HEIGHT);
        this.individuals = new ArrayList<>();
        this.masters = new ArrayList<>();
        this.randomGenerator = RandomNumberGenerator.getInstance();
        this.timeStep = 0;
        this.maxTimeSteps = MAX_TIME_STEPS;
        this.running = false;
        
        initialize();
    }
    
    private void initialize() {
        // Create masters
        MasterElf masterElf = MasterElf.getInstance();
        MasterHuman masterHuman = MasterHuman.getInstance();
        MasterOrc masterOrc = MasterOrc.getInstance();
        MasterGoblin masterGoblin = MasterGoblin.getInstance();
        
        masters.add(masterElf);
        masters.add(masterHuman);
        masters.add(masterOrc);
        masters.add(masterGoblin);
        
        // Place masters in their safe zones
        placeMasterInSafeZone(masterElf, Population.ELF);
        placeMasterInSafeZone(masterHuman, Population.HUMAN);
        placeMasterInSafeZone(masterOrc, Population.ORC);
        placeMasterInSafeZone(masterGoblin, Population.GOBLIN);
        
        // Create mobile individuals
        createPopulation(Population.ELF, NUM_INDIVIDUALS_PER_POPULATION);
        createPopulation(Population.HUMAN, NUM_INDIVIDUALS_PER_POPULATION);
        createPopulation(Population.ORC, NUM_INDIVIDUALS_PER_POPULATION);
        createPopulation(Population.GOBLIN, NUM_INDIVIDUALS_PER_POPULATION);
        
        // Give initial messages
        distributeInitialMessages();
    }
    
    private void placeMasterInSafeZone(Master master, Population population) {
        SafeZone zone = map.getSafeZones().stream()
                .filter(z -> z.getPopulation() == population)
                .findFirst()
                .orElse(null);
        
        if (zone != null) {
            Tile tile = map.getTile(zone.getCenter());
            if (tile != null) {
                master.updatePosition(tile);
            }
        }
    }
    
    private void createPopulation(Population population, int count) {
        for (int i = 0; i < count; i++) {
            LivingBeing individual = createIndividual(population);
            
            // Find a random starting position (not in safe zones of other populations)
            Position startPos = findValidStartPosition(population);
            if (startPos != null) {
                Tile tile = map.getTile(startPos);
                individual.updatePosition(tile);
                individuals.add(individual);
            }
        }
    }
    
    private LivingBeing createIndividual(Population population) {
        switch (population) {
            case ELF: return new Elf();
            case HUMAN: return new Human();
            case ORC: return new Orc();
            case GOBLIN: return new Goblin();
            default: return new Elf(); // fallback
        }
    }
    
    private Position findValidStartPosition(Population population) {
        RandomNumberGenerator random = randomGenerator;
        int attempts = 0;
        
        while (attempts < 100) { // Limit attempts to avoid infinite loop
            int x = random.nextInt(map.getWidth());
            int y = random.nextInt(map.getHeight());
            Tile tile = map.getTile(x, y);
            
            if (tile != null && tile.isEmpty() && 
                (!tile.isSafeZone() || tile.getSafeZonePopulation() == population)) {
                return new Position(x, y);
            }
            attempts++;
        }
        return null;
    }
    
    private void distributeInitialMessages() {
        String[] sampleMessages = {
            "Ancient Wisdom", "Hidden Knowledge", "Secret Path", 
            "Magic Spell", "Historical Fact", "Scientific Discovery",
            "Cultural Tradition", "Mysterious Prophecy", "Lost Technology",
            "Natural Law", "Philosophical Insight", "Mathematical Theorem"
        };
        
        RandomNumberGenerator random = randomGenerator;
        
        for (LivingBeing individual : individuals) {
            // Give each individual 1-3 random messages
            int numMessages = random.nextInt(3) + 1;
            for (int i = 0; i < numMessages; i++) {
                String message = sampleMessages[random.nextInt(sampleMessages.length)];
                if (!individual.getMessages().contains(message)) {
                    individual.getMessages().add(message);
                }
            }
        }
    }
    
    public void run() {
        running = true;
        System.out.println("Simulation started!");
        
        while (running && timeStep < maxTimeSteps) {
            step();
            
            if (checkEndCondition()) {
                break;
            }
            
            // Slow down for visualization
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        determineWinner();
        System.out.println("Simulation ended after " + timeStep + " time steps.");
    }
    
    public void step() {
        timeStep++;
        System.out.println("\n--- Time Step " + timeStep + " ---");
        
        // Randomize processing order
        Collections.shuffle(individuals, new Random(randomGenerator.getSeed() + timeStep));
        
        // Process each mobile individual
        for (LivingBeing individual : individuals) {
            if (individual.getEnergyPoints() > 0) {
                individual.move(map);
                checkEncounters(individual);
                individual.checkEnergy();
                transferMessagesToMaster(individual);
            }
        }
        
        // Display current state
        displayState();
    }
    
    private void checkEncounters(LivingBeing individual) {
        if (individual.getCurrentTile() == null) return;
        
        Position pos = individual.getCurrentTile().getPosition();
        
        // Check adjacent tiles for encounters
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                
                int checkX = pos.getX() + dx;
                int checkY = pos.getY() + dy;
                
                if (map.isValidPosition(checkX, checkY)) {
                    Tile adjacentTile = map.getTile(checkX, checkY);
                    if (adjacentTile.getOccupant() != null && 
                        adjacentTile.getOccupant() != individual) {
                        
                        individual.meet(adjacentTile.getOccupant());
                        System.out.println(individual.getPopulation() + " encountered " + 
                                         adjacentTile.getOccupant().getPopulation());
                    }
                }
            }
        }
    }
    
    private void transferMessagesToMaster(LivingBeing individual) {
        Master master = getMasterForPopulation(individual.getPopulation());
        if (master != null && isNearMaster(individual, master)) {
            master.collectMessagesFrom(individual);
            System.out.println(individual.getPopulation() + " transferred messages to master");
        }
    }
    
    private boolean isNearMaster(LivingBeing individual, Master master) {
        if (individual.getCurrentTile() == null || master.getCurrentTile() == null) {
            return false;
        }
        
        Position individualPos = individual.getCurrentTile().getPosition();
        Position masterPos = master.getCurrentTile().getPosition();
        
        return individualPos.distanceTo(masterPos) <= 2; // Within 2 tiles
    }
    
    private Master getMasterForPopulation(Population population) {
        for (Master master : masters) {
            if (master.getPopulation() == population) {
                return master;
            }
        }
        return null;
    }
    
    public boolean checkEndCondition() {
        for (Master master : masters) {
            if (master.getUniqueMessageCount() >= 10) { // Win condition: 10 unique messages
                System.out.println(master.getPopulation() + " master has collected enough messages to win!");
                running = false;
                return true;
            }
        }
        
        if (timeStep >= maxTimeSteps) {
            System.out.println("Maximum time steps reached!");
            running = false;
            return true;
        }
        
        return false;
    }
    
    public void determineWinner() {
        Master winner = null;
        int maxMessages = -1;
        
        for (Master master : masters) {
            int messageCount = master.getUniqueMessageCount();
            System.out.println(master.getPopulation() + " master collected " + 
                             messageCount + " unique messages");
            
            if (messageCount > maxMessages) {
                maxMessages = messageCount;
                winner = master;
            }
        }
        
        if (winner != null) {
            System.out.println("\n*** " + winner.getPopulation() + " WINS! ***");
            System.out.println("Collected " + maxMessages + " unique messages");
        }
    }
    
    private void displayState() {
        System.out.println("Current state:");
        for (Master master : masters) {
            System.out.println(master.getPopulation() + " Master: " + 
                             master.getUniqueMessageCount() + " messages");
        }
        
        // Count active individuals
        long activeCount = individuals.stream()
                .filter(ind -> ind.getEnergyPoints() > 0)
                .count();
        System.out.println("Active individuals: " + activeCount + "/" + individuals.size());
    }
    
    // Getters for testing and monitoring
    public Map getMap() { return map; }
    public List<LivingBeing> getIndividuals() { return individuals; }
    public List<Master> getMasters() { return masters; }
    public int getTimeStep() { return timeStep; }
    public boolean isRunning() { return running; }
    
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.run();
    }
}