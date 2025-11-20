// Orc.java
public class Orc extends Bad {
    public Orc() {
        super(Population.ORC);
    }
    
    @Override
    public void move(Map map) {
        // Similar movement logic
        Elf elfMovement = new Elf(); // Reuse for simplicity
        elfMovement.move(map);
    }
    
    @Override
    public void meet(LivingBeing other) {
        exchangeMessages(other);
    }
}
