package me.kingOf0.pksupport.skript.expansion;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.projectkorra.projectkorra.BendingPlayer;
import me.kingOf0.pksupport.PKSupport;
import me.kingOf0.pksupport.file.Config;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.HashMap;

public class AbilityCooldownMs extends SimpleExpression<String> {

    static {
        Skript.registerExpression(AbilityCooldownMs.class, String.class, ExpressionType.COMBINED, "[the] ability cooldown of %player% of %number% slot in ms");

    }
    private final String nullBendingPlayer;
    private final String noneSet;

    private final PKSupport main;

    private Expression<Player> player;
    private Expression<Number> slot;

    @SuppressWarnings("ConstantConditions")
    public AbilityCooldownMs() {
        main = PKSupport.getInstance();
        Config config = main.getConfig();

        nullBendingPlayer = ChatColor.translateAlternateColorCodes('&', config.getString("notValidBendingPlayer", "NVBP"));
        noneSet = ChatColor.translateAlternateColorCodes('&', config.getString("notSet", "NS"));
    }

    @Override
    protected String[] get(Event event) {
        Player player = this.player.getSingle(event);
        Integer slot = this.slot.getSingle(event).intValue();

        BendingPlayer bendingPlayer = BendingPlayer.getBendingPlayer(player);
        if (bendingPlayer == null)
            return new String[]{nullBendingPlayer};

        HashMap<Integer, String> abilities = bendingPlayer.getAbilities();
        if (!abilities.containsKey(slot))
            return new String[]{noneSet.replace("%slot%", Integer.toString(slot))};

        String ability = abilities.get(slot);
        long cooldown = bendingPlayer.getCooldown(ability);
        long ms = cooldown - System.currentTimeMillis();

        if (cooldown == -1L)
            return new String[]{""};

        return new String[]{Long.toString(ms)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "PKSUP ability status expression player: " + player.getSingle(event) + " ";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {

        if (expressions[0] == null)
            return false;
        if (expressions[1] == null)
            return false;

        player = (Expression<Player>) expressions[0];
        slot = (Expression<Number>) expressions[1];
        return true;
    }

}
