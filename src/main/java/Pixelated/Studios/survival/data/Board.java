package Pixelated.Studios.survival.data;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import Pixelated.Studios.survival.Survival;
import Pixelated.Studios.survival.util.Utils;
import Pixelated.Studios.survival.util.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a team based scoreboard for a player
 * <p>This class also has a map that holds all player scoreboards</p>
 */
@SuppressWarnings("unused")
public class Board {

    // STATIC STUFF
    private static final Map<Player, Board> BOARD_MAP = new HashMap<>();

    /**
     * Get the Board for a specific player
     * <br>
     * If no Board is available, a new one will be created
     *
     * @param player Player to grab scoreboard for
     * @return Board of player
     */
    public static Board getBoard(Player player) {
        if (BOARD_MAP.containsKey(player)) {
            return BOARD_MAP.get(player);
        } else {
            return createBoard(player);
        }
    }

    /**
     * Create a Board for a player
     * <br>
     * Useful in a join event
     *
     * @param player Player to create Board for
     */
    public static Board createBoard(Player player) {
        Board board = new Board(player, false);
        BOARD_MAP.put(player, board);
        return board;
    }

    /**
     * Remove a Board for a player
     * <br>
     * Useful when the player leaves the server
     *
     * @param player Player to remove Board for
     */
    public static void removeBoard(Player player) {
        if (BOARD_MAP.containsKey(player)) {
            Board board = BOARD_MAP.get(player);
            board.toggle(false);
            board.clearBoard();
        }
        BOARD_MAP.remove(player);
    }

    /**
     * Clear and remove all Boards
     */
    public static void clearBoards() {
        for (Board board : BOARD_MAP.values()) {
            board.clearBoard();
        }
        BOARD_MAP.clear();
    }

    // OBJECT STUFF
    private final Player player;
    private final Scoreboard oldScoreboard;
    private final Scoreboard scoreboard;
    private final Objective board;
    private final Team[] lines = new Team[15];
    private final String[] entries = new String[]{"&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&0", "&a", "&b", "&c", "&d", "&e"};
    private boolean on;

    public Board(Player player, boolean load) {
        this.player = player;
        this.on = true;
        Survival plugin = Survival.getInstance();
        ScoreboardManager scoreboardManager = plugin.getServer().getScoreboardManager();
        oldScoreboard = player.getScoreboard();
        // TODO: Replace Deprecations with new method
        if (!load) {
            //todo: investigate this scoreboardManager warning
            assert scoreboardManager != null;
            scoreboard = scoreboardManager.getNewScoreboard();
            this.player.setScoreboard(scoreboard);
            board = scoreboard.registerNewObjective("Board", "dummy", "Board");
            board.setDisplaySlot(DisplaySlot.SIDEBAR);
            board.setDisplayName(" ");

            for (int i = 0; i < 15; i++) {
                lines[i] = scoreboard.registerNewTeam("line" + (i + 1));
            }

            for (int i = 0; i < 15; i++) {
                lines[i].addEntry(getColString(entries[i]));
            }
        } else {
            scoreboard = player.getScoreboard();
            board = scoreboard.getObjective("Board");

            for (int i = 0; i < 15; i++) {
                lines[i] = scoreboard.getTeam("line" + (i + 1));
            }
        }
    }

    /**
     * Set the title of this Board
     *
     * @param title Title to set
     */
    // TODO: Replace Deprecated with new method
    public void setTitle(String title) {
        board.setDisplayName(getColString(title));
    }

    /**
     * Set a specific line for this Board
     * <p>Lines 1 - 15</p>
     *
     * @param line Line to set (1 - 15)
     * @param text Text to put in line
     */
    // TODO: Replace Deprecated with new method
    public void setLine(int line, String text) {
        Validate.isBetween(line, 1, 15);
        Team t = lines[line - 1];
        t.setPrefix(getColString(text));
        board.getScore(getColString(entries[line - 1])).setScore(line);
    }

    /**
     * Delete a line in this Board
     * <p>Lines 1 - 15</p>
     *
     * @param line Line to delete (1 - 15)
     */
    public void deleteLine(int line) {
        Validate.isBetween(line, 1, 15);
        scoreboard.resetScores(getColString(entries[line - 1]));
    }

    /**
     * Clear all lines of this Board
     */
    public void clearBoard() {
        for (int i = 1; i < 16; i++) {
            deleteLine(i);
        }
    }

    /**
     * Toggle this Board on or off
     * <br>
     * When off, will not be visible to player, but can still update
     *
     * @param on Whether on or off
     */
    public void toggle(boolean on) {
        if (on) {
            player.setScoreboard(this.scoreboard);
            this.on = true;
        } else {
            player.setScoreboard(this.oldScoreboard);
            this.on = false;
        }
    }

    /**
     * Check if this Board is on or off
     *
     * @return True if on else false
     */
    public boolean isOn() {
        return this.on;
    }

    private String getColString(String string) {
        return Utils.getColoredString(string);
    }

}