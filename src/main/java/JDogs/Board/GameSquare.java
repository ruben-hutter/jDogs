package JDogs.Board;

//represents a square on which a Indentation is placed
public class GameSquare extends Square {
    Point coordinate;
    Indentation indentation;

    GameSquare(Point coordinate, Indentation indentation) {
        this.coordinate = coordinate;
        this.indentation = indentation;
    }

    @Override
    public boolean isIndentation() {
        return true;
    }
}
