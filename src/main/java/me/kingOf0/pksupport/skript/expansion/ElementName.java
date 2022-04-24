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

public class ElementName extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ElementName.class, String.class, ExpressionType.COMBINED, "[the] element name of %player% of %number% element");
    }

    private final String nullBendingPlayer;
    private final String haventElement;

    private final PKSupport main;

    private Expression<Player> player;
    private Expression<Number> elementIndex;

    @SuppressWarnings("ConstantConditions")
    public ElementName() {
        main = PKSupport.getInstance();
        Config config = main.getConfig();

        haventElement = ChatColor.translateAlternateColorCodes('&', config.getString("haventElement","HE"));
        nullBendingPlayer = ChatColor.translateAlternateColorCodes('&', config.getString("notValidBendingPlayer", "NVBP"));
    }

    @Override
    protected String[] get(Event event) {
        Player player = this.player.getSingle(event);
        Integer slot = this.elementIndex.getSingle(event).intValue();

        BendingPlayer bendingPlayer = BendingPlayer.getBendingPlayer(player);
        if (bendingPlayer == null)
            return new String[]{nullBendingPlayer};

        return new String[]{bendingPlayer.getElements().isEmpty() ? haventElement : bendingPlayer.getElements().get(slot).getName()};
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
        return "PKSUP element name expression player: " + player.getSingle(event) + " ";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) expressions[0];
        elementIndex = (Expression<Number>) expressions[1];
        return true;
    }

}
