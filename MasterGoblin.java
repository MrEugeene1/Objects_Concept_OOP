// MasterGoblin.java
public class MasterGoblin extends Master {
    private static MasterGoblin instance;
    
    private MasterGoblin() {
        super(Population.GOBLIN, Alliance.BAD);
    }
    
    public static MasterGoblin getInstance() {
        if (instance == null) {
            instance = new MasterGoblin();
        }
        return instance;
    }
}