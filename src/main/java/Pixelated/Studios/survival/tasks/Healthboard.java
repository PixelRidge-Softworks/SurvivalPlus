package Pixelated.Studios.survival.tasks;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import Pixelated.Studios.survival.Survival;
import Pixelated.Studios.survival.config.Config;
import Pixelated.Studios.survival.data.Board;
import Pixelated.Studios.survival.data.Info;
import Pixelated.Studios.survival.data.PlayerData;
import Pixelated.Studios.survival.managers.PlayerManager;
import Pixelated.Studios.survival.util.Utils;

public class Healthboard extends BukkitRunnable {

    private final Config config;
    private final PlayerManager pm;
    private final Player player;
    private final PlayerData playerData;
    private final Board board;
    private boolean temperature;

    // Board stuff
    private boolean hunger;
    private boolean thirst;
    private boolean energy;
    private boolean nutrients;

    public Healthboard(Survival plugin, Player player) {
        this.config = plugin.getSurvivalConfig();
        this.pm = plugin.getPlayerManager();
        this.player = player;
        this.playerData = plugin.getPlayerManager().getPlayerData(player);
        this.board = Board.getBoard(player);
        this.hunger = playerData.isInfoDisplayed(Info.HUNGER);
        this.thirst = playerData.isInfoDisplayed(Info.THIRST);
        this.energy = playerData.isInfoDisplayed(Info.ENERGY);
        this.nutrients = playerData.isInfoDisplayed(Info.NUTRIENTS);
        this.temperature = playerData.isInfoDisplayed(Info.TEMPERATURE);

        this.board.setTitle(Utils.getColoredString(plugin.getLang().healthboard_title));

        this.runTaskTimer(plugin, -1, 10);
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            this.cancel();
            return;
        }

        GameMode mode = player.getGameMode();
        if (mode == GameMode.CREATIVE || mode == GameMode.SPECTATOR) {
            // If the player is in creative/spectator and board is on, turn it off
            if (board.isOn()) {
                board.toggle(false);
            }
        } else {
            // Else if player is in survival/adventure and board is off, turn it on
            if (!board.isOn()) {
                board.toggle(true);
            }
        }

        // Refresh board options
        this.hunger = playerData.isInfoDisplayed(Info.HUNGER);
        this.thirst = playerData.isInfoDisplayed(Info.THIRST);
        this.energy = playerData.isInfoDisplayed(Info.ENERGY);
        this.nutrients = playerData.isInfoDisplayed(Info.NUTRIENTS);
        this.temperature = playerData.isInfoDisplayed(Info.TEMPERATURE);

        // If all options on the board are disabled, turn board off
        if (!hunger && !thirst && !energy && !nutrients && !temperature) {
            if (board.isOn()) {
                board.toggle(false);
            }
            return;
        }

        if (temperature) {
            board.setLine(12, pm.ShowTemperature(player));
        } else {
            board.deleteLine(12);
        }
        if (hunger) {
            board.setLine(11, pm.ShowHunger(player).get(0));
            board.setLine(10, pm.ShowHunger(player).get(1));
            board.setLine(9, pm.ShowHunger(player).get(2));
        } else {
            board.deleteLine(11);
            board.deleteLine(10);
            board.deleteLine(9);
        }

        if (config.MECHANICS_THIRST_ENABLED && thirst) {
            board.setLine(8, pm.ShowThirst(player).get(0));
            board.setLine(7, pm.ShowThirst(player).get(1));
            board.setLine(6, pm.ShowThirst(player).get(2));
        } else {
            board.deleteLine(8);
            board.deleteLine(7);
            board.deleteLine(6);
        }

        if (config.MECHANICS_ENERGY_ENABLED && energy) {
            board.setLine(5, pm.showEnergy(player).get(0));
            board.setLine(4, pm.showEnergy(player).get(1));
        } else {
            board.deleteLine(5);
            board.deleteLine(4);
        }

        if (config.MECHANICS_FOOD_DIVERSITY_ENABLED && nutrients) {
            board.setLine(3, pm.ShowNutrients(player).get(0));
            board.setLine(2, pm.ShowNutrients(player).get(1));
            board.setLine(1, pm.ShowNutrients(player).get(2));
        } else {
            board.deleteLine(3);
            board.deleteLine(2);
            board.deleteLine(1);
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        Board.removeBoard(player);
    }
}

