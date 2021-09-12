package veth.vetheon.survival.item;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.util.Utils;

import java.io.File;
import java.io.IOException;

class ItemConfig {

    static ItemConfig INSTANCE;
    private final Survival plugin = Survival.getInstance();
    private final String prefix = plugin.getLang().prefix;
    private FileConfiguration settings;
    private File configFile;

    ItemConfig() {
        INSTANCE = this;
        loadDefaultSettings();
        Nutrition.setup();
        save();
        Utils.sendColoredConsoleMsg(prefix + "&7items.yml &aloaded");
    }

    private void loadDefaultSettings() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "items.yml");
        }
        if (!configFile.exists()) {
            plugin.saveResource("items.yml", false);
            settings = YamlConfiguration.loadConfiguration(configFile);
            Utils.sendColoredConsoleMsg(prefix + "&aNew items.yml created");
        } else {
            settings = YamlConfiguration.loadConfiguration(configFile);
        }
    }

    int getModelData(String key, int defaultValue) {
        int data;
        String path = "items." + key + ".model_data";
        if (settings.contains(path)) {
            data = settings.getInt(path);
        } else {
            data = defaultValue;
            settings.set(path, data);
            save();
        }
        return data;
    }

    int[] getNutritionValues(String key, int carbs, int proteins, int vitamins) {
        String path = "nutritions." + key + ".";
        String[] paths = new String[]{path + "carbs", path + "proteins", path + "vitamins"};
        int[] nutritions = new int[]{carbs, proteins, vitamins};
        if (settings.contains(paths[0])) {
            nutritions[0] = settings.getInt(paths[0]);
        } else {
            settings.set(paths[0], carbs);
        }
        if (settings.contains(paths[1])) {
            nutritions[1] = settings.getInt(paths[1]);
        } else {
            settings.set(paths[1], proteins);
        }
        if (settings.contains(paths[2])) {
            nutritions[2] = settings.getInt(paths[2]);
        } else {
            settings.set(paths[2], vitamins);
        }
        return nutritions;
    }

    double getRepairCost(String key, double defaultValue) {
        String path = "items." + key + ".repair_cost_multiplier";
        if (settings.contains(path)) {
            return settings.getDouble(path);
        } else {
            settings.set(path, defaultValue);
            return defaultValue;
        }
    }

    double getRepairPercent(String key, double defaultValue) {
        String path = "items." + key + ".repair_percent";
        if (settings.contains(path)) {
            return settings.getDouble(path);
        } else {
            settings.set(path, defaultValue);
            return defaultValue;
        }
    }

    void save() {
        try {
            settings.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
