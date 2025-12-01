// Elf.java

import java.util.List;

public class Elf extends Good {
    public Elf() {
        super(Population.ELF);
    }
    
    @Override
    public void move(Map map) {
        if (energyPoints <= 0 || currentTile == null) return;
        
        RandomNumberGenerator random = RandomNumberGenerator.getInstance();
        List<Tile> possibleMoves = map.getAvailableTiles(currentTile.getPosition());
        
        if (energyPoints < 20) {
            // Low energy - move toward safe zone
            moveTowardSafeZone(map);
        } else {
            // Random movement
            if (!possibleMoves.isEmpty()) {
                Tile targetTile = possibleMoves.get(random.nextInt(possibleMoves.size()));
                executeMove(map, targetTile);
            }
        }
    }
    
    @Override
    public void meet(LivingBeing other) {
        exchangeMessages(other);
    }
    
    private void moveTowardSafeZone(Map map) {
        Direction safeDirection = map.getDirectionToSafeZone(population);
        // Simplified movement toward safe zone
        List<Tile> possibleMoves = map.getAvailableTiles(currentTile.getPosition());
        Tile bestTile = null;
        double bestDistance = Double.MAX_VALUE;
        
        for (Tile tile : possibleMoves) {
            double distance = map.getDistanceToSafeZone(tile.getPosition(), population);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestTile = tile;
            }
        }
        
        if (bestTile != null) {
            executeMove(map, bestTile);
        }
    }
    
    private void executeMove(Map map, Tile targetTile) {
        // Consume energy
        energyPoints -= 1;
        
        // Update position
        updatePosition(targetTile);
        
        // Check if in safe zone for energy restoration
        if (map.isInSafeZone(targetTile.getPosition(), population)) {
            energyPoints = Math.min(100, energyPoints + 5);
        }
    }
}