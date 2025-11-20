// Master.java
public abstract class Master extends LivingBeing {
    protected List<String> collectedMessages;
    
    public Master(Population population, Alliance alliance) {
        super(population, alliance);
        this.collectedMessages = new ArrayList<>();
    }
    
    @Override
    public void move(Map map) {
        // Masters cannot move
    }
    
    @Override
    public void meet(LivingBeing other) {
        // Masters can receive messages from their population
        if (other.getPopulation() == this.population) {
            collectMessagesFrom(other);
        }
    }
    
    public void collectMessagesFrom(LivingBeing other) {
        for (String message : other.getMessages()) {
            if (!collectedMessages.contains(message)) {
                collectedMessages.add(message);
            }
        }
        // Individual keeps their messages
    }
    
    public void collectMessage(String message) {
        if (!collectedMessages.contains(message)) {
            collectedMessages.add(message);
        }
    }
    
    @Override
    public boolean isMaster() {
        return true;
    }
    
    public List<String> getCollectedMessages() {
        return collectedMessages;
    }
    
    public int getUniqueMessageCount() {
        return collectedMessages.size();
    }
}