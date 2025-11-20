// Map.java
import java.util.*;

public class Map {
    private int width;
    private int height;
    private Tile[][] grid;
    private List<SafeZone> safeZones;
    private List<Obstacle> obstacles;
    
    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Tile[width][height];
        this.safeZones = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        initializeGrid();
        generateSafeZones();
        generateObstacles();
    }
    
    private void initializeGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Tile(x, y);
            }
        }
    }
    
    private void generateSafeZones() {
        // Create safe zones in corners
        int zoneRadius = 2;
        
        // Elf safe zone - top-left
        SafeZone elfZone = new SafeZone(new Position(zoneRadius, zoneRadius), zoneRadius, Population.ELF);
        markSafeZone(elfZone, Population.ELF);
        safeZones.add(elfZone);
        
        // Human safe zone - top-right
        SafeZone humanZone = new SafeZone(new Position(width - zoneRadius - 1, zoneRadius), zoneRadius, Population.HUMAN);
        markSafeZone(humanZone, Population.HUMAN);
        safeZones.add(humanZone);
        
        // Orc safe zone - bottom-left
        SafeZone orcZone = new SafeZone(new Position(zoneRadius, height - zoneRadius - 1), zoneRadius, Population.ORC);
        markSafeZone(orcZone, Population.ORC);
        safeZones.add(orcZone);
        
        // Goblin safe zone - bottom-right
        SafeZone goblinZone = new SafeZone(new Position(width - zoneRadius - 1, height - zoneRadius - 1), zoneRadius, Population.GOBLIN);
        markSafeZone(goblinZone, Population.GOBLIN);
        safeZones.add(goblinZone);
    }
    
    private void markSafeZone(SafeZone zone, Population population) {
        Position center = zone.getCenter();
        int radius = zone.getRadius();
        
        for (int x = center.getX() - radius; x <= center.getX() + radius; x++) {
            for (int y = center.getY() - radius; y <= center.getY() + radius; y++) {
                if (isValidPosition(x, y)) {
                    grid[x][y].setSafeZone(true);
                    grid[x][y].setSafeZonePopulation(population);
                }
            }
        }
    }
    
    private void generateObstacles() {
        RandomNumberGenerator random = RandomNumberGenerator.getInstance();
        int numObstacles = (width * height) / 10; // 10% obstacles
        
        for (int i = 0; i < numObstacles; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            
            // Don't place obstacles in safe zones
            if (!grid[x][y].isSafeZone() && grid[x][y].isEmpty()) {
                Obstacle obstacle = new Obstacle(new Position(x, y));
                obstacles.add(obstacle);
                // In a full implementation, we'd mark the tile as occupied
            }
        }
    }
    
    public List<Tile> getAvailableTiles(Position position) {
        List<Tile> available = new ArrayList<>();
        int x = position.getX();
        int y = position.getY();
        
        // Check all 8 directions
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // Skip current position
                
                int newX = x + dx;
                int newY = y + dy;
                
                if (isValidPosition(newX, newY) && grid[newX][newY].canMoveTo()) {
                    available.add(grid[newX][newY]);
                }
            }
        }
        
        return available;
    }
    
    public Direction getDirectionToSafeZone(Population population) {
        // Simplified - return random direction
        RandomNumberGenerator random = RandomNumberGenerator.getInstance();
        return Direction.values()[random.nextInt(Direction.values().length)];
    }
    
    public double getDistanceToSafeZone(Position position, Population population) {
        SafeZone zone = getSafeZone(population);
        return position.distanceTo(zone.getCenter());
    }
    
    public boolean isInSafeZone(Position position, Population population) {
        SafeZone zone = getSafeZone(population);
        return zone.isInSafeZone(position);
    }
    
    private SafeZone getSafeZone(Population population) {
        for (SafeZone zone : safeZones) {
            if (zone.getPopulation() == population) {
                return zone;
            }
        }
        return null;
    }
    
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    public boolean isValidPosition(Position position) {
        return isValidPosition(position.getX(), position.getY());
    }
    
    public Tile getTile(int x, int y) {
        if (isValidPosition(x, y)) {
            return grid[x][y];
        }
        return null;
    }
    
    public Tile getTile(Position position) {
        return getTile(position.getX(), position.getY());
    }
    
    // Getters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<SafeZone> getSafeZones() { return safeZones; }
    public List<Obstacle> getObstacles() { return obstacles; }
}