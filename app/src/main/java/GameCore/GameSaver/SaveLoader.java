package GameCore.GameSaver;

import android.content.Context;
import android.graphics.Point;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import GameCore.Field;
import GameCore.Figure.Figure;
import GameCore.Figure.Ghost;
import GameCore.Figure.Ghostable;
import GameCore.Game;
import GameCore.GameModes.NormalGame;
import GameCore.PlayerTypes.Human;
import GameCore.PlayerTypes.Player;

public class StandardLoadSaver implements SaveLoader{
    private Game game;
    Figure[][] field;

    public String saveInternal(){
        Field field = game.getField();
        Point size = field.getSize();
        String out = field.getSize().x + "," + game.getCurrentPlayer().getName() + ";"
                + game.getPlayer1().getName() + "," + game.getPlayer1().getClassName() + ";"
                + game.getPlayer2() + "," + game.getPlayer2().getClassName() + ";";
        for(int line = 0; line < size.y; line++){
            for(int row = 0; row < size.x; row++){
                Figure current = field.getFigure(row, line);
                if(current == null){
                    out += "n;";
                }else{
                    String moved = current.hasMoved() ? "1" : "0";
                    String ghost = (current instanceof Ghost) ? "1" : "0";
                    out += ghost + "," + current.getClass().getName() + "," + current.getOwner().getName() + "," + moved;
                    if(current instanceof Ghost){
                        out += "," + ((Ghost) current).getMaster().getPos().x + "," + ((Ghost) current).getMaster().getPos().y;
                    }
                }
            }
        }
        return out;
    }


    public boolean save(Context context){
        String saveName = game.getPlayer1().getName() + "  vs " + game.getPlayer2().getName();
        File saveDirectory = new File(context.getFilesDir() + File.separator + "OpenChess" + File.separator + "Saved_Games");
        if(!saveDirectory.exists())
            saveDirectory.mkdirs();
        File[] saves = saveDirectory.listFiles();
        Integer Index = 0;
        for(File saveFile : saves){
            String suffix = Index > 0 ? Index + "" : "";
            if(saveFile.getName().equalsIgnoreCase(saveName))
                Index++;
            else
                break;
        }
        String saveString = saveInternal();
        try {
            FileWriter fileWriter = new FileWriter(new File(saveName));
            fileWriter.write(saveString);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean load(Context context, String fileName){
        File loadFile = new File(context.getFilesDir() + File.separator + "OpenChess" + File.separator + "Saved_Games" + File.separator + fileName);
        return true;
    }


    public boolean loadInt(String in){
        NormalGame newGame = new NormalGame();
        String[] tokenized = in.split(";");
        String[] gameInfo = tokenized[0].split(",");
        int size = Integer.parseInt(gameInfo[0]);
        Player player1 = null;
        Player player2 = null;
        String[] player1Info = tokenized[1].split(",");
        String[] player2Info = tokenized[2].split(",");
        if(player1Info[1] == "Human"){
            player1 = new Human(player1Info[0], true);
        }else{

        }
        if (player2Info[1] == "Human"){
            player2 = new Human(player2Info[0], false);
        }

        Player currentPlayer = gameInfo[1] == player1.getName() ? player1 : player2;
        Field field = new Field(size);
        ArrayList<String> ghosts = new ArrayList<>();
        for(int i = 3; i < tokenized.length; i++){
            String[] token = tokenized[i].split(",");
            if(token[0] == "n")
                continue;
            else{
                int x = (i - 3) % 8;
                int y = (i - 3) / 8;
                Player owner = player1.getName() == token[2] ? player1 : player2;
                boolean moved = token[3] == "1" ? true : false;
                Figure newFigure = null;
                if(token[0] == "0"){
                    newFigure = getFigure(token[1], owner, x, y, newGame);
                }else{
                    ghosts.add(tokenized[i].concat("," + x + "," + y));
                }
                if(newFigure != null) {
                    newFigure.setMoved(moved);
                    field.setField(newFigure, x, y);
                }
            }
        }
        for(String ghost : ghosts){
            String[] token = ghost.split(",");
            Player owner = player1.getName() == token[2] ? player1 : player2;
            Figure master = field.getFigure(Integer.parseInt(token[4]), Integer.parseInt(token[5]));
            Integer x = Integer.parseInt(token[6]);
            Integer y = Integer.parseInt(token[7]);
            Ghost newGhost = getGhost(token[1], owner, x, y, (Ghostable)master, newGame);
            field.setField(newGhost, x, y);
        }
        newGame.setFigureField(field);
        newGame.setCurrentPlayer(currentPlayer);
        return true;
    }

    private Figure getFigure(String className, Player owner, Integer x, Integer y, Game newGame){
        try {
            Class figClass = Class.forName(className);
            Constructor constructor = figClass.getConstructor(Player.class, Integer.class, Integer.class, Game.class);
            return (Figure) constructor.newInstance(owner, x, y, newGame);
        } catch (ClassNotFoundException e) {

        } catch (NoSuchMethodException e) {

        } catch (IllegalAccessException e) {

        } catch (InstantiationException e) {

        } catch (InvocationTargetException e) {

        }
        return null;
    }

    private Ghost getGhost(String className, Player owner, Integer x, Integer y, Ghostable master, Game newGame){
        try {
            Class figClass = Class.forName(className);
            Constructor constructor = figClass.getConstructor(Player.class, Integer.class, Integer.class, Ghostable.class, Game.class);
            return (Ghost) constructor.newInstance(owner, x, y, master, newGame);
        } catch (ClassNotFoundException e) {

        } catch (NoSuchMethodException e) {

        } catch (IllegalAccessException e) {

        } catch (InstantiationException e) {

        } catch (InvocationTargetException e) {

        }
        return null;
    }
}
