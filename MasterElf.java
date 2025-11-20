// MasterElf.java
public class MasterElf extends Master {
    private static MasterElf instance;
    
    private MasterElf() {
        super(Population.ELF, Alliance.GOOD);
    }
    
    public static MasterElf getInstance() {
        if (instance == null) {
            instance = new MasterElf();
        }
        return instance;
    }
}