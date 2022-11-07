package figures;

import combinatedFields.Position;
import drawEngine.DrawEngine;
import logic.GeneralGameLogic;
import logic.Gifts;
import setups.TilesSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GiftCollection {
    public  List<GiftFigure> giftFigureList;
    private TilesSetup tilesSetup;

    public GiftCollection(TilesSetup tilesSetup) {
        this.tilesSetup = tilesSetup;
        zombieCollectionSetup();
    }

    public  void zombieCollectionSetup() {
        giftFigureList = new ArrayList<>();

        for (int count = 0; count < 8;count++) {
            GiftFigure giftFigure = new GiftFigure();
            giftFigure.setPosition(getRandomFreePosition());
            switch (count) {
                case 0 -> giftFigure.setGifts(Gifts.ammo);
                case 1 -> giftFigure.setGifts(Gifts.chest);
                case 2 -> giftFigure.setGifts(Gifts.diamond);
                case 3 -> giftFigure.setGifts(Gifts.gift);
                case 4 -> giftFigure.setGifts(Gifts.gun);
                case 5 -> giftFigure.setGifts(Gifts.grenade);
                case 6 -> giftFigure.setGifts(Gifts.princess);
                case 7 -> giftFigure.setGifts(Gifts.question);
            }

            giftFigureList.add(giftFigure);
        }

    }

    private Position getRandomFreePosition() {
        Random random = new Random();
        int col;
        int row;
        do {
            col = random.nextInt(TilesSetup.TILE_MAP_COLS-2)+1;
            row = random.nextInt(TilesSetup.TILE_MAP_ROWS-2)+1;

        } while (!GeneralGameLogic.canWalkOnIdList.contains(tilesSetup.tileMapId[col][row]));
        return DrawEngine.getTilesTranslatedToPixels(new Position(col,row));
    }


}
