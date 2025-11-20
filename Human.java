// Human.java
public class Human extends Good {
    public Human() {
        super(Population.HUMAN);
    }
    
    @Override
    public void move(Map map) {
        // Similar implementation to Elf, can be customized
        Elf elfMovement = new Elf(); // Reuse logic for simplicity
        elfMovement.move(map);
    }
    
    @Override
    public void meet(LivingBeing other) {
        exchangeMessages(other);
    }
}