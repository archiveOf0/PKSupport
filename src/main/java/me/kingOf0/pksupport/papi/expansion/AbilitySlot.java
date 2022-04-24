package me.kingOf0.pksupport.papi.expansion;

import com.projectkorra.projectkorra.BendingManager;
import com.projectkorra.projectkorra.BendingPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.kingOf0.pksupport.PKSupport;
import me.kingOf0.pksupport.file.Config;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AbilitySlot extends PlaceholderExpansion {

    private final String nullBendingPlayer;
    private final String notValidSlot;
    private final String noneSet;
    private final String cooldownPrefix;
    private final String notValidArgs;
    private final String haventElement;

    private final int lengthLimit;
    private final PKSupport main;

    @SuppressWarnings("ConstantConditions")
    public AbilitySlot() {
        main = PKSupport.getInstance();
        Config config = main.getConfig();

        nullBendingPlayer = ChatColor.translateAlternateColorCodes('&', config.getString("notValidBendingPlayer", "NVBP"));
        notValidSlot = ChatColor.translateAlternateColorCodes('&', config.getString("notValidSlot", "NVS"));
        noneSet = ChatColor.translateAlternateColorCodes('&', config.getString("notSet", "NS"));
        cooldownPrefix = ChatColor.translateAlternateColorCodes('&', config.getString("cooldownPrefix","§m"));
        notValidArgs = ChatColor.translateAlternateColorCodes('&', config.getString("notValidParams","NVP"));
        haventElement = ChatColor.translateAlternateColorCodes('&', config.getString("haventElement","HE"));

        lengthLimit = config.getInt("abilityNameLengthLimit", 10);
    }

    @Override
    public String getIdentifier() {
        return "pk";
    }

    @Override
    public String getAuthor() {
        return main.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return main.getDescription().getVersion();
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        BendingPlayer bendingPlayer = BendingPlayer.getBendingPlayer(player);
        if (bendingPlayer == null)
            return nullBendingPlayer;

        String[] args = params.split("_");
        if (args.length < 2)
            return notValidArgs.replace("%params%", params);

        int slot;
        try {
            slot = Integer.parseInt(args[1]);
        } catch (Exception e) {
            return notValidSlot;
        }

        if (args.length == 2)
            if (args[0].equals("elementPrefix"))
                return bendingPlayer.getElements().isEmpty() ? haventElement : bendingPlayer.getElements().get(slot).getPrefix();
             else if (args[0].equals("elementName"))
                return bendingPlayer.getElements().isEmpty() ? haventElement : bendingPlayer.getElements().get(slot).getName();

        HashMap<Integer, String> abilities = bendingPlayer.getAbilities();
        if (abilities.containsKey(slot)) {
            if (args.length == 2 && args[0].equalsIgnoreCase("name"))
                return applyLimit(abilities.get(slot)) + "§r";
        } else {
            if (args.length == 2 && args[0].equalsIgnoreCase("name"))
                return noneSet.replace("%slot%", Integer.toString(slot));
            else return "";
        }

        String ability = abilities.get(slot);
        long cooldown = bendingPlayer.getCooldown(ability);
        long ms = cooldown - System.currentTimeMillis();

        //pk _ AbilityName _ <slot>
        //       0       1
        if (args.length == 2 && args[0].equalsIgnoreCase("status")) {
            if (cooldown == -1L)
                return "";
            else
                return cooldownPrefix;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("cooldown")) {
            if (args[2].equalsIgnoreCase("ms")) {
                if (cooldown == -1L)
                    return "";

                return Long.toString(ms);
            } else if (args[2].equalsIgnoreCase("double")) {
                if (cooldown == -1L)
                    return "";

                return Double.toString((ms) / 100);
            } else if (args[2].equalsIgnoreCase("tick")) {
                if (cooldown == -1L)
                    return "";
                // 50 ms = 1 tick
                return Long.toString(ms / 50);
            } else if (args[2].equalsIgnoreCase("second")) {
                if (cooldown == -1L)
                    return "";
                long cd = ms / 1000;
//                if (cd == 0)
//                    return "1";
                return Long.toString(cd);
            }
        }
        return notValidArgs.replace("%params%", params);
    }

    private String applyLimit(String name) {
        if (name.length() < lengthLimit)
            return name;
        return name.substring(0, lengthLimit);
    }

}
