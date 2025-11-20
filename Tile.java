package Objects_Concept_OOP;

// Tile.java
public class Tile {
    private Position position;
    private LivingBeing occupant;
    private Obstacle obstacle;
    private boolean isSafeZone;
    private Population safeZonePopulation;
    
    public Tile(int x, int y) {
        this.position = new Position(x, y);
        this.isSafeZone = false;
    }
    
    public boolean isEmpty() {
        return occupant == null && obstacle == null;
    }
    
    public boolean canMoveTo() {
        return isEmpty() || (occupant != null && !occupant.isMaster());
    }
    
    // Getters and setters
    public Position getPosition() { return position; }
    public LivingBeing getOccupant() { return occupant; }
    public void setOccupant(LivingBeing occupant) { this.occupant = occupant; }
    public boolean isSafeZone() { return isSafeZone; }
    public void setSafeZone(boolean safeZone) { isSafeZone = safeZone; }
    public Population getSafeZonePopulation() { return safeZonePopulation; }
    public void setSafeZonePopulation(Population population) { 
        this.safeZonePopulation = population; 
    }
}
