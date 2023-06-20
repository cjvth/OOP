package ru.nsu.cjvth.pizza;

/**
 * Event representation to store in logs.
 */
public class LogEntry {
    private final int actor;
    private final ActorType type;
    private final int order;
    private final OrderStatus status;

    /**
     * Status of an order.
     */
    public enum OrderStatus {
        NOT_STARTED,
        COOK_START,
        COOK_FINISH,
        DELIVERY_START,
        DELIVERY_FINISH
    }

    /**
     * Enum to know whether the actor is a cook or a delivery man.
     */
    public enum ActorType {
        COOK,
        DELIVERY
    }

    /**
     * Constructor.
     *
     * @param actor  id of the actor
     * @param type   is actor a cook or a delivery man
     * @param order  id of the order
     * @param status status of the order
     */
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
