package com.fastandfood.commons;

/**
 * Usage:
 *   Pair<Object, Object> pair = Pair.createPair(0x0F, 0x0F);
 *   pair.getFirst();
 *   pair.getSecond();
 *
 * @author Borja
 */
public class Pair<L, R> {

    private final L leftElement;
    private final R rightElement;

    public static <L, R> Pair<L, R> createPair(L left, R right) {
        return new Pair<>(left, right);
    }

    public Pair(L left, R right) {
        this.leftElement = left;
        this.rightElement = right;
    }

    public L getFirst() {
        return leftElement;
    }

    public R getSecond() {
        return rightElement;
    }
}