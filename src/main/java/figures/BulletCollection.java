package figures;

import combinatedFields.Position;
import drawEngine.DrawEngine;
import javafx.scene.control.skin.TextInputControlSkin;
import setups.TilesSetup;

import java.util.ArrayList;
import java.util.List;

public class BulletCollection {
    public List<BulletFigure> bulletFigureList;
    private TilesSetup tilesSetup;

    public BulletCollection(TilesSetup tilesSetup) {
        this.tilesSetup = tilesSetup;
        bulletFigureList = new ArrayList<>();
    }

    public void addNewBullet(Figure originForBullet) {
        BulletFigure bulletFigure = new BulletFigure();
        bulletFigure.setPosition(originForBullet.getPosition());
        bulletFigure.setDirection(originForBullet.getDirection());
        bulletFigureList.add(bulletFigure);
    }

    public void handleAllBullets() {
        final int speed = 30;
        for (BulletFigure bulletFigure : bulletFigureList) {
            int x = bulletFigure.getPositionInPixels().getxPos();
            int y = bulletFigure.getPositionInPixels().getyPos();
            switch (bulletFigure.getDirection()) {
                case LEFT -> x -= speed;
                case RIGHT -> x += speed;
                case UP -> y -= speed;
                case DOWN -> y += speed;
            }
            bulletFigure.setPosition(new Position(x, y));
        }
        eraseBulletsOutOfBoundary();

    }

    public void eraseBulletsOutOfBoundary() {
        List<BulletFigure> bulletFigureListToErase = new ArrayList<>();
        for (BulletFigure bulletFigure : bulletFigureList) {
            if ((bulletFigure.getPosition().getxPos() <= 0) || //
                    (bulletFigure.getPosition().getyPos() <= 0) || //
                    (bulletFigure.getPositionInTiles().getxPos() >= TilesSetup.TILE_MAP_COLS) || //
                    (bulletFigure.getPositionInTiles().getyPos() >= TilesSetup.TILE_MAP_ROWS)) {
                bulletFigureListToErase.add(bulletFigure);
            }
        }
        bulletFigureList.removeAll(bulletFigureListToErase);
    }


    public boolean isCollision(Position positionScaledToTiles) {
        for (BulletFigure bulletFigure : bulletFigureList) {
            // todo
        }
        return false;
    }
}
