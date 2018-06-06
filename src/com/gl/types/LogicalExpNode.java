package com.gl.types;

import java.io.Serializable;
import java.util.List;

public class LogicalExpNode implements ColorExp, Serializable {

    private boolean isAnd;

    private ColorExp first;
    private ColorExp second;

    public void setFirst(ColorExp exp) {
        first = exp;
    }

    public void setOperation(boolean isAnd) {
        this.isAnd = isAnd;
    }

    public void setSecond(ColorExp exp) {
        second = exp;
    }

    public String toString() {
        return "(" + first + (isAnd ? " && " : " || ") + second + ")";
    }

    public boolean matches(List<TileColor> colors) {
        boolean firstMatch = first.matches(colors);
        boolean secondMatch = second.matches(colors);

        return (isAnd && firstMatch && secondMatch) || (!isAnd && (firstMatch || secondMatch));
    }

    public boolean isAndOp() {
        return isAnd;
    }

    public ColorExp getFirst() {
        return first;
    }

    public ColorExp getSecond() {
        return second;
    }
}
