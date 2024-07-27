package eu.unchat.easyscoreboard.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.TextFormat;
import eu.unchat.easyscoreboard.EasyScoreboard;
import eu.unchat.easyscoreboard.EasyScoreboardHandler;
import eu.unchat.easyscoreboard.EasyScoreboardProvider;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class ScoreboardUpdateTask extends Task {
    private final EasyScoreboardHandler handler;

    public ScoreboardUpdateTask(EasyScoreboardHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onRun(int i) {
        for (Map.Entry<UUID, EasyScoreboard> entry : handler.getScoreboards().entrySet()) {
            Player player = Server.getInstance().getPlayer(entry.getKey()).get();
            EasyScoreboard value = entry.getValue();
            if (player == null || value == null) {
                continue;
            }

            EasyScoreboardProvider provider = handler.getProvider();
            if (provider == null) {
                continue;
            }

            String title = provider.getTitle(player);
            if (title == null) {
                continue;
            }

            value.send(translate(title));

            int line = 0;
            for (String text : provider.getLines(player)) {
                value.setLine(line++, translate(text));
            }
        }
    }

    private String translate(final String text) {
        return TextFormat.colorize('&', text);
    }
}
