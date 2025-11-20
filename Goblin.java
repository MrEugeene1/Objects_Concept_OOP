public class Goblin extends Bad {
    public Goblin() {
        super(Population.GOBLIN);
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