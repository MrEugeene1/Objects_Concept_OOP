// RandomNumberGenerator.java
import java.util.Random;

public class RandomNumberGenerator {
    private static RandomNumberGenerator instance;
    private Random random;
    private long seed;
    
    private RandomNumberGenerator() {
        this.seed = System.currentTimeMillis();
        this.random = new Random(seed);
    }
    
    public static RandomNumberGenerator getInstance() {
        if (instance == null) {
            instance = new RandomNumberGenerator();
        }
        return instance;
    }
    
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
    
    public boolean nextBoolean() {
        return random.nextBoolean();
    }
    
    public double nextDouble() {
        return random.nextDouble();
    }
    
    public void setSeed(long seed) {
        this.seed = seed;
        this.random.setSeed(seed);
    }
    
    public long getSeed() {
        return seed;
    }
}