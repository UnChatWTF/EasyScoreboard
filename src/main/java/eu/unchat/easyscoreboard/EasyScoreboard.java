package eu.unchat.easyscoreboard;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.SetDisplayObjectivePacket;
import cn.nukkit.network.protocol.SetScorePacket;
import cn.nukkit.scoreboard.data.DisplaySlot;
import cn.nukkit.scoreboard.data.SortOrder;

public final class EasyScoreboard {
    private static final String OBJECTIVE_ID = "easy_scoreboard";

    private final Player player;

    public EasyScoreboard(Player player) {
        this.player = player;
    }

    public void send(final String display) {
        if (!player.isConnected()) {
            return;
        }

        player.getSession().sendPacket(new SetDisplayObjectivePacket(DisplaySlot.SIDEBAR, OBJECTIVE_ID, display, "dummy", SortOrder.ASCENDING));
    }

    public void setLine(final int line, final String text) {
        if (!player.isConnected()) {
            return;
        }

        SetScorePacket.ScoreInfo info = new SetScorePacket.ScoreInfo(Integer.valueOf(line).longValue(), OBJECTIVE_ID, line, text);

        SetScorePacket packet = new SetScorePacket();

        packet.action = SetScorePacket.Action.SET;
        packet.infos.add(info);

        player.getSession().sendPacket(packet);
    }

    public void destroy() {
        if (!player.isConnected()) {
            return;
        }

        player.getSession().sendPacket(new SetDisplayObjectivePacket(DisplaySlot.SIDEBAR, OBJECTIVE_ID, "", "dummy", SortOrder.ASCENDING));
    }
}
