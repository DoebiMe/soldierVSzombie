package figures;

import combinatedFields.Position;
import combinatedFields.SpriteDirection;
import drawEngine.DrawEngine;

public class BaseFigure {
    private Position position;
    private SpriteDirection direction;
    private int spriteWidth;
    private int spriteHeight;

    public BaseFigure() {
        position = new Position(0, 0);
        direction = SpriteDirection.DOWN;
    }

    public Position getPosition() {
        return position;
    }


    public Position getPositionInTiles() {
        return DrawEngine.getPixelsTranslatedToTiles(position);
    }


    public Position getPositionInPixels() {
        return position;
    }


    public void setPosition(Position position) {
        this.position = position;
    }


    public SpriteDirection getDirection() {
        return direction;
    }


    public void setDirection(SpriteDirection newDirection) {
        this.direction = newDirection;
        int x = position.getxPos();
        int y = position.getyPos();
        switch (direction) {
            case LEFT, RIGHT -> x -= (x % DrawEngine.SCALE_FACTOR_SPRITE);
            case UP, DOWN -> y -= (y % DrawEngine.SCALE_FACTOR_SPRITE);
        }
        position = new Position(x, y);
    }

    public void setDirectionUnAltered(SpriteDirection direction) {
        this.direction = direction;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public void setSpriteWidth(int spriteWidth) {
        this.spriteWidth = spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public void setSpriteHeight(int spriteHeight) {
        this.spriteHeight = spriteHeight;
    }

    public Position getCenterPositionInPixels() {
        return new Position(getPosition().getxPos() + DrawEngine.SCALE_FACTOR_SPRITE / 2, getPosition().getyPos() + DrawEngine.SCALE_FACTOR_SPRITE / 2);
    }

    public boolean isInCoalition(BaseFigure baseFigure) {
        int centerXPos = getCenterPositionInPixels().getxPos();
        int centerYPos = getCenterPositionInPixels().getyPos();
        int baseXPos = baseFigure.getPositionInPixels().getxPos();
        int baseYPos = baseFigure.getPositionInPixels().getyPos();
        int baseWidth = DrawEngine.SCALE_FACTOR_SPRITE;
        int baseHeight = DrawEngine.SCALE_FACTOR_SPRITE;
        return //
                (centerXPos > baseXPos) && //
                        (centerXPos < (baseXPos + baseWidth)) && //
                        (centerYPos > baseYPos) && //
                        (centerYPos < (baseYPos + baseHeight));
    }
}