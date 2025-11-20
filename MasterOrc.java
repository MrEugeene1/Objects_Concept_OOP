// MasterOrc.java
public class MasterOrc extends Master {
    private static MasterOrc instance;
    
    private MasterOrc() {
        super(Population.ORC, Alliance.BAD);
    }
    
    public static MasterOrc getInstance() {
        if (instance == null) {
            instance = new MasterOrc();
        }
        return instance;
    }
}