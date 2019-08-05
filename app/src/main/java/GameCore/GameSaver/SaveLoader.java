package GameCore.GameSaver;

import android.content.Context;
import android.graphics.Point;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Activities.Game;
import GameCore.Exceptions.ParsingError;
import GameCore.Field;
import GameCore.Figure.Figure;
import GameCore.PlayerTypes.Human;
import GameCore.PlayerTypes.Player;
import GameCore.Recorder;

public class SaveLoader {
    private Game game;
    Figure[][] field;

    public SaveLoader(Game game) {
        this.game = game;
        field = game.getFigureField().getField();
    }

    /**
     * converts necessairy game info to a string
     * <p>
     * dividable into information chunks with ';'
     * information chunks can be divided with ','
     * <p>
     * first information chunk contains field size and current player: {field size, current players name}
     * second and third chunk contains player information(the second chunk is player 1): {player name, player class name}
     * the rest of the chunk contain figure data by order from [0, 0] to [7, 7]: {boolean value if figure is a ghost, figure class name, figure is of player1?, moved state}
     * additionally if figure is a ghost the information chunk is extended: {..., master position x, master position y}
     *
     * @return compressed game as string
     */
    public String buildSaveString() {
        Field field = game.getField();
        Point size = field.getSize();
        game.getRecorder().onNextTurn();
        String out = field.getSize().x + "," + game.getCurrentPlayer().getName() + ";"
                + game.getPlayer1().getName() + "," + game.getPlayer1().getClassName() + ";"
                + game.getPlayer2().getName() + "," + game.getPlayer2().getClassName() + ";"
                + game.getRecorder().toString() + ";";
        for(int line = 0; line < size.y; line++){
            for(int row = 0; row < size.x; row++){
                Figure current = field.getFigure(row, line);
                if (current == null)
                    continue;
                out += current.toString() + ";";
            }
        }
        return out;
    }


    /**
     * saves game in new save file
     *
     * @param context
     * @return
     */
    public boolean saveNew(Context context) {
        String dir = context.getFilesDir() + File.separator + "OpenChess" + File.separator + "Saved_Games";
        String saveName = game.getPlayer1().getName() + " vs " + game.getPlayer2().getName();
        File saveDirectory = new File(dir);
        if (!saveDirectory.exists())
            saveDirectory.mkdirs();
        String saveString = buildSaveString();
        Integer index = 0;
        File[] files = saveDirectory.listFiles();
        Integer i = 0;
        while (i < files.length) {
            String suffix = index > 0 ? index + "" : "";
            if (files[i].getName().equalsIgnoreCase(saveName + suffix)) {
                index++;
                i = 0;
                continue;
            }
            i++;
        }
        try {
            File newFile = new File(dir + File.separator + saveName);
            newFile.createNewFile();
            FileWriter fileWriter = new FileWriter(newFile, false);
            fileWriter.write(saveString);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * saves game in old save file
     *
     * @param context
     * @return
     */
    public boolean saveOld(Context context) {
        String saveName = game.getName();
        File saveDirectory = new File(context.getFilesDir() + File.separator + "OpenChess" + File.separator + "Saved_Games");
        if(!saveDirectory.exists())
            saveDirectory.mkdirs();
        String saveString = buildSaveString();
        try {
            File newFile = new File(saveDirectory + File.separator + saveName);
            newFile.createNewFile();
            FileWriter fileWriter = new FileWriter(newFile, false);
            fileWriter.write(saveString);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    /**
     * loads game from save file
     *
     * @param context  application context
     * @param fileName
     * @return
     */
    public boolean load(Context context, String fileName) throws ParsingError, IOException {
        File loadFile = new File(context.getFilesDir() + File.separator + "OpenChess" + File.separator + "Saved_Games" + File.separator + fileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(loadFile));
            String charBuffer = "";
            String line;
            while ((line = br.readLine()) != null) {
                charBuffer += line;
            }
            br.close();
            parse(charBuffer);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File Not Found");
        } catch (IOException e) {
            throw new IOException("IO error");
        } catch (ParsingError e) {
            throw new ParsingError("Error while parsing");
        }
        if (game.getPlayer1().getKing() == null || game.getPlayer2().getKing() == null)
            throw new ParsingError("Loading failed, corrupted save file");
        return true;
    }

    /**
     * parser for input save string (refer to saveInternal method for details about data structure)
     *
     * @param in string to be parsed
     * @return true if parsing was successful
     */
    public boolean parse(String in) throws ParsingError {
        String[] tokenized = in.split(";");
        String[] gameInfo = tokenized[0].split(",");
        int size = Integer.parseInt(gameInfo[0]);
        Player player1 = null;
        Player player2 = null;
        String[] player1Info = tokenized[1].split(",");
        String[] player2Info = tokenized[2].split(",");
        player1 = new Human(player1Info[0], true);
        player2 = new Human(player2Info[0], false);
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        Player currentPlayer = gameInfo[1].equalsIgnoreCase(player1.getName()) ? player1 : player2;
        Field field = new Field(size);
        game.setFigureField(field);
        Recorder recorder = new Recorder(game, tokenized[3]);
        game.setRecorder(recorder);
        // arraylist for ghost so that they are loaded correctly
        ArrayList<String> ghosts = new ArrayList<>();
        // load all figures except ghosts
        for (int i = 4; i < tokenized.length; i++) {

            if (tokenized[i].startsWith("1")) {
                ghosts.add(tokenized[i]);
                continue;
            }
            Figure.fromString(tokenized[i], player1, player2, field, game);

        }
        // ghost are loaded after all other figures because of their master need to be initiated
        for (String ghost : ghosts) {
            Figure.fromString(ghost, player1, player2, field, game);
        }
        game.setCurrentPlayer(currentPlayer);
        return true;
    }


}
