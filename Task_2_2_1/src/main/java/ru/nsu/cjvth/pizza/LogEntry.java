package ru.nsu.cjvth.pizza;

public class LogEntry {
    private final int actor;
    private final ActorType type;
    private final int order;
    private final OrderStatus status;


    public enum OrderStatus {
        NOT_STARTED,
        COOK_START,
        COOK_FINISH,
        DELIVERY_START,
        DELIVERY_FINISH
    }

    public enum ActorType {
        COOK,
        DELIVERY
    }

    public LogEntry(int actor, ActorType type, int order, OrderStatus status) {
        this.actor = actor;
        this.type = type;
        this.order = order;
        this.status = status;
    }

    public int getOrder() {
        return order;
    }

    public OrderStatus getOrderStatus() {
        return status;
    }

    public int getActor() {
        return actor;
    }

    public ActorType getActorType() {
        return type;
    }
}
