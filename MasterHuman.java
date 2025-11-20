// MasterHuman.java
public class MasterHuman extends Master {
    private static MasterHuman instance;
    
    private MasterHuman() {
        super(Population.HUMAN, Alliance.GOOD);
    }
    
    public static MasterHuman getInstance() {
        if (instance == null) {
            instance = new MasterHuman();
        }
        return instance;
    }
}