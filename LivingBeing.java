// LivingBeing.java
import java.util.*;

public abstract class LivingBeing {
    protected List<String> messages;
    protected Direction lastDirection;
    protected int energyPoints;
    protected Tile currentTile;
    protected Population population;
    protected Alliance alliance;
    
    public LivingBeing(Population population, Alliance alliance) {
        this.messages = new ArrayList<>();
        this.energyPoints = 100; // Start with full energy
        this.population = population;
        this.alliance = alliance;
        this.lastDirection = Direction.NORTH; // Default direction
    }
    
    // Abstract methods to be implemented by subclasses
    public abstract void move(Map map);
    public abstract void meet(LivingBeing other);
    
    // Common implementation for message exchange
    public void exchangeMessages(LivingBeing other) {
        if (this.alliance == other.alliance) {
            // Friendly exchange - share random messages
            shareRandomMessages(other);
        } else {
            // Hostile confrontation
            confrontation(other);
        }
    }
    
    private void shareRandomMessages(LivingBeing other) {
        RandomNumberGenerator random = RandomNumberGenerator.getInstance();
        int numMessages = random.nextInt(3) + 1; // 1-3 messages
        
        for (int i = 0; i < numMessages && !messages.isEmpty(); i++) {
            String message = messages.get(random.nextInt(messages.size()));
            if (!other.messages.contains(message)) {
                other.messages.add(message);
            }
        }
    }
    
    private void confrontation(LivingBeing other) {
        RandomNumberGenerator random = RandomNumberGenerator.getInstance();
        LivingBeing winner = random.nextBoolean() ? this : other;
        LivingBeing loser = (winner == this) ? other : this;
        
        // Transfer 1-2 messages from loser to winner
        int numMessages = random.nextInt(2) + 1;
        List<String> messagesToTransfer = new ArrayList<>();
        
        for (String message : loser.messages) {
            if (!winner.messages.contains(message)) {
                messagesToTransfer.add(message);
                if (messagesToTransfer.size() >= numMessages) break;
            }
        }
        
        // Transfer messages
        winner.messages.addAll(messagesToTransfer);
        loser.messages.removeAll(messagesToTransfer);
    }
    
    public void checkEnergy() {
        if (energyPoints <= 0) {
            // Become obstacle (simplified - just remove from simulation)
            if (currentTile != null) {
                currentTile.setOccupant(null);
            }
        }
    }
    
    public void updatePosition(Tile newTile) {
        if (currentTile != null) {
            currentTile.setOccupant(null);
        }
        this.currentTile = newTile;
        if (newTile != null) {
            newTile.setOccupant(this);
        }
    }
    
    // Getters and setters
    public List<String> getMessages() { return messages; }
    public Direction getLastDirection() { return lastDirection; }
    public void setLastDirection(Direction direction) { this.lastDirection = direction; }
    public int getEnergyPoints() { return energyPoints; }
    public void setEnergyPoints(int energy) { this.energyPoints = energy; }
    public Tile getCurrentTile() { return currentTile; }
    public Population getPopulation() { return population; }
    public Alliance getAlliance() { return alliance; }
    public boolean isMaster() { return false; }
}

