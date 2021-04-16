package figures;

import combinatedFields.Position;
import combinatedFields.SpriteDirection;
import setups.TilesSetup;

import java.util.ArrayList;
import java.util.List;

public class SkullCollection {
    public List<SkullFigure> skullFigureList;
    private TilesSetup tilesSetup;

    public SkullCollection(TilesSetup tilesSetup) {
        this.tilesSetup = tilesSetup;
        skullFigureList = new ArrayList<>();
        addNewSkull(new Position(500, 200));
        addNewSkull(new Position(1500, 200));
        addNewSkull(new Position(800, 300));
        addNewSkull(new Position(2000, 10));
    }

    public void addNewSkull(Position position) {
        SkullFigure skullFigure = new SkullFigure();
        skullFigure.setPosition(position);
        skullFigure.setDirection(SpriteDirection.DOWN);
        skullFigureList.add(skullFigure);
    }

    public void handleAllSkulls(Position positionToGoTo) {
        final int speed = 1;
        for (SkullFigure skullFigure : skullFigureList) {

            if (skullFigure.isTimeForNextPosition() || Math.random() < 0.5) {
                if (!skullFigure.isInFreeze()) {

                    int x = skullFigure.getPosition().getxPos();
                    int y = skullFigure.getPosition().getyPos();
                    boolean xChange = false;
                    boolean yChange = false;
                    if (skullFigure.getPosition().getxPos() < positionToGoTo.getxPos()) {
                        x += speed;
                        xChange = true;
                    } else {
                        if (skullFigure.getPosition().getxPos() > positionToGoTo.getxPos()) {
                            x -= speed;
                            xChange = true;
                        }
                    }
                    if (skullFigure.getPosition().getyPos() < positionToGoTo.getyPos()) {
                        y += speed;
                        yChange = true;
                    } else {
                        if (skullFigure.getPosition().getyPos() > positionToGoTo.getyPos()) {
                            y -= speed;
                            yChange = true;
                        }
                    }

                    if (Math.random() > 0.99) { // make small irregulate move
                        int add = Math.random() > 0.5 ? speed * 3 : -speed *3;
                        if (!xChange) {
                            x += add;
                            System.out.println("plus X");
                        }
                        if (!yChange) {
                            y += add;
                            System.out.println("plus Y");
                    }
                }

                skullFigure.setPosition(new Position(x, y));
            }
        }
    }

    freezeSkullsOnSameTile();

}

    public void freezeSkullsOnSameTile() {
        for (SkullFigure skullFigure : skullFigureList) {
            for (SkullFigure skullFigureToCompare : skullFigureList) {
                if (!skullFigure.isInFreeze() && !skullFigureToCompare.isInFreeze()) {
                    if (skullFigure.getIDNr() != skullFigureToCompare.getIDNr()) {
                        if (skullFigure.getPositionInTiles().equals(skullFigureToCompare.getPositionInTiles())) {
                            skullFigure.freeze();
                            return;
                        }
                    }
                }
            }
        }
    }
}
