package org.darwin_world.model.exceptions;

import org.darwin_world.model.util.Vector2D;

public class IncorrectPositionException extends Exception {
    public IncorrectPositionException(Vector2D position) {
        super("Position at " + position + " is incorrect");
    }
}
