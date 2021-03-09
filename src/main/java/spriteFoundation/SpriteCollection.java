package spriteFoundation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SpriteCollection {
    private static final ArrayList<Sprite> spriteList = new ArrayList<>();

    public static void addSprite(Sprite spriteToAdd) {
        if (!spriteList.contains(spriteToAdd)) {
            spriteList.add(spriteToAdd);
        }
    }

    public static void removeSprite(Sprite spriteToRemove) {
        spriteList.remove(spriteToRemove);
    }

    public static void removeSprite(long id) {
        spriteList.removeIf(sprite -> sprite.getSpriteId() == id);
    }

    public static Sprite getSprite(long id) {
        for (Sprite sprite : spriteList) {
            if (sprite.getSpriteId() == id) {
                return sprite;
            }
        }
        return null;
    }

    private static final Comparator<Sprite> compareByImageId = Comparator.comparing(Sprite::getImageId);

    public static List<Sprite> getSpriteList() {

        return spriteList.stream().sorted(compareByImageId).collect(Collectors.toList());
    }

    private static final Comparator<Sprite> compareBySpriteId = Comparator.comparing(Sprite::getSpriteId);

    public static List<Sprite> getSpriteList(long idStartIncluded, long idEndIncluded) {
        ArrayList<Sprite> resultList = new ArrayList<>();
        for (Sprite sprite : spriteList) {
            if (sprite.getSpriteId() >= idStartIncluded && sprite.getSpriteId() <= idEndIncluded) {
                resultList.add(sprite);
            }
        }
        return resultList.stream().sorted(compareBySpriteId).collect(Collectors.toList());
    }

    public static boolean containsSprite(Sprite sprite) {
        return spriteList.contains(sprite);
    }

    public static boolean containsSprite(long id) {
        for (Sprite sprite : spriteList) {
            if (sprite.getSpriteId() == id) {
                return true;
            }
        }
        return false;
    }

}
