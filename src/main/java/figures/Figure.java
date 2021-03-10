package figures;

import combinatedFields.Position;
import combinatedFields.SpriteDirection;

public interface Figure {

    Position getPosition();
    void setPosition(Position position);
    Position getPositionInTiles();
    Position getPositionInPixels();
    void setDirection(SpriteDirection spriteDirection);
    SpriteDirection getDirection();

}
