package Objects_Concept_OOP;

// SafeZone.java
public class SafeZone {
    private Position center;
    private int radius;
    private Population population;
    
    public SafeZone(Position center, int radius, Population population) {
        this.center = center;
        this.radius = radius;
        this.population = population;
    }
    
    public boolean isInSafeZone(Position position) {
        return center.distanceTo(position) <= radius;
    }
    
    public void restoreEnergy(LivingBeing being) {
        if (being.getPopulation() == population) {
            being.setEnergyPoints(being.getEnergyPoints() + 10); // Restore 10 EP
            if (being.getEnergyPoints() > 100) {
                being.setEnergyPoints(100); // Cap at max
            }
        }
    }
    
    // Getters
    public Position getCenter() { return center; }
    public Population getPopulation() { return population; }
}