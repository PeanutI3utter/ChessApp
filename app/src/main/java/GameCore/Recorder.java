package GameCore;

import android.graphics.Point;

import java.util.LinkedList;

import Activities.Game;
import GameCore.Exceptions.ParsingError;
import GameCore.Figure.Figure;
import GameCore.Movement.MacroMovements.Move;
import GameCore.Movement.atomicMovement.Movements;
import GameCore.Utils.CustomIterator;


/**
 * '*' divides events, '#' divides moves/turns
 */
public class Recorder {
    private LinkedList<String> records;
    private String buffer;
    private Game game;
    private int pointer;

    public Recorder(Game game) {
        records = new LinkedList<>();
        records.add("NIL");
        buffer = "";
        this.game = game;
    }

    public Recorder(Game game, String record) {
        this.game = game;
        this.records = new LinkedList<>();
        for (String string : record.split("#"))
            records.addLast(string);
        buffer = "";
    }

    public void recordMovement(Move move) {
        CustomIterator<Movements> mm = move.getIterator();
        while (mm.hasNext()) {
            buffer += compress(mm.getNext()) + "*";
        }
    }


    public void record(Movements at, int origx, int origy, String moved) {
        String out = "";
        String compress = compress(at);
        String[] data = compress.split(",");
        data[1] = origx + "";
        data[2] = origy + "";
        data[5] = moved;
        for (int i = 0; i < data.length; i++) {
            out += data[i] + ",";
        }
        out += data[data.length - 1];
        buffer += out + "*";
    }


    public void onNextTurn() {
        cut();
        if (buffer.length() > 0)
            records.addFirst(buffer);
        buffer = "";

    }

    public void cut() {
        if (pointer > 0) {
            records = new LinkedList<>(records.subList(pointer, records.size()));
            pointer = 0;
        }
    }

    public String compress(Movements micromovement) {
        Point movingPoint = micromovement.getMoveTo();
        String compressedMove = "move:" + micromovement.getMovingFig().toString() + "," + movingPoint.x + "," + movingPoint.y;
        return compressedMove;
    }

    public void onDelete(Figure figure) {
        buffer += "delete:" + figure.toString() + "*";
    }

    public void onCreate(Figure figure) {
        buffer += "create:" + figure.toString() + "*";
    }

    public String toString() {
        String out = "";
        for (int i = 0; i < records.size(); i++) {
            String string = records.get(i);
            out += string;
            if (i + 1 < records.size())
                out += "#";
        }
        return out;
    }

    public boolean canRewind() {
        return pointer < records.size() - 1 && records.size() > 1;
    }

    public boolean canUndoRewind() {
        return pointer > 0;
    }

    public void rewind(int i) {
        while (i-- > 0 && pointer < records.size()) {
            if (!canRewind())
                break;
            String record = records.get(pointer);
            if (record == null)
                return;
            parser(record, true);
            pointer++;
        }
        buffer = "";
        game.onNextTurn();
    }

    public void undoRewind(int i) {
        while (i-- > 0 && pointer > 0) {
            pointer--;
            String record = records.get(pointer);
            if (record != null)
                parser(record, false);

        }
        buffer = "";
        game.onNextTurn();
    }

    public void parser(String turn, boolean revert) {
        String[] events = turn.split("\\*");
        if (revert)
            for (int i = (events.length - 1); i >= 0; i--) {
                String event = events[i];
                String[] decodedEvent = event.split(":");
                switch (decodedEvent[0]) {
                    case "move":
                        revert(decodedEvent[1]);
                        break;
                    case "create":
                        deleteFigure(decodedEvent[1]);
                        break;
                    case "delete":
                        reanimateFigure(decodedEvent[1]);
                        break;
                }
            }
        else
            for (int i = 0; i < events.length; i++) {
                String event = events[i];
                String[] decodedEvent = event.split(":");
                switch (decodedEvent[0]) {
                    case "move":
                        forward(decodedEvent[1]);
                        break;
                    case "create":
                        reanimateFigure(decodedEvent[1]);
                        break;
                    case "delete":
                        deleteFigure(decodedEvent[1]);
                        break;
                }
            }

    }

    public void revert(String move) {
        String[] moveDescription = move.split(",");
        Figure figure = game.getFigureField().getFigure(Integer.parseInt(moveDescription[6]), Integer.parseInt(moveDescription[7]));
        figure.move(Integer.parseInt(moveDescription[1]), Integer.parseInt(moveDescription[2]));
        figure.setMoved(moveDescription[5].equals("1"));
    }

    public void forward(String move) {
        String[] moveDescription = move.split(",");
        game.getFigureField().getFigure(Integer.parseInt(moveDescription[1]), Integer.parseInt(moveDescription[2])).move(Integer.parseInt(moveDescription[6]), Integer.parseInt(moveDescription[7]));
    }

    public void reanimateFigure(String figure) {
        try {
            Figure.fromString(figure, game.getPlayer1(), game.getPlayer2(), game.getField(), game);
        } catch (ParsingError parsingError) {
            parsingError.printStackTrace();
        }
    }

    public void deleteFigure(String figure) {
        String[] figureData = figure.split(",");
        Figure fig = game.getFigureField().getFigure(Integer.parseInt(figureData[1]), Integer.parseInt(figureData[2]));
        if (fig != null)
            fig.deleteWithoutRecord();
        else
            System.out.println("fig creation failed");
    }

    public void reset() {
        records.clear();
        buffer = "";
        pointer = 0;
    }

}
