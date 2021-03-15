package JDogs.Board;

public class ScenerySquare extends Square {

    Point coordinate;

    ScenerySquare(Point coordinate) {

        this.coordinate = coordinate;
    }

    @Override
    public boolean isIndentation() {
        return false;
    }

}
