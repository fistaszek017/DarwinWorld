package org.darwin_world.model.map;

import org.darwin_world.model.util.Vector2D;

public record MoveResult(Vector2D position, MapDirection direction) {
}
