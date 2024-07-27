package eu.unchat.easyscoreboard;

import cn.nukkit.Player;

import java.util.Collection;

public interface EasyScoreboardProvider {
    String getTitle(final Player player);

    Collection<String> getLines(final Player player);
}
