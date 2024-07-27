package eu.unchat.easyscoreboard;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import eu.unchat.easyscoreboard.task.ScoreboardUpdateTask;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class EasyScoreboardHandler {
    private static EasyScoreboardHandler instance;

    private final PluginBase plugin;

    private EasyScoreboardProvider provider;

    private final Map<UUID, EasyScoreboard> scoreboards;

    public EasyScoreboardHandler(final PluginBase plugin) {
        if (instance != null) {
            throw new IllegalStateException("Instance already exists");
        }

        instance = this;

        this.plugin = plugin;

        this.scoreboards = Maps.newConcurrentMap();

        this.plugin.getServer().getScheduler().scheduleRepeatingTask(new ScoreboardUpdateTask(this), 2);
    }

    public void setProvider(EasyScoreboardProvider provider) {
        Preconditions.checkNotNull(provider, "Provider cannot be null");
        this.provider = provider;
    }

    public void createScoreboard(final UUID uniqueId) {
        if (getScoreboard(uniqueId) != null) {
            throw new IllegalStateException("Scoreboard already exists");
        }

        Optional<Player> optionalPlayer = Server.getInstance().getPlayer(uniqueId);
        optionalPlayer.ifPresent(player -> {
            EasyScoreboard scoreboard = new EasyScoreboard(player);
            this.scoreboards.put(uniqueId, scoreboard);
        });
    }

    public void removeScoreboard(final UUID uniqueId) {
        Optional<Player> optionalPlayer = Server.getInstance().getPlayer(uniqueId);
        optionalPlayer.ifPresent(player -> {
            EasyScoreboard scoreboard = getScoreboard(uniqueId);
            Preconditions.checkNotNull(scoreboard, "Scoreboard does not exist");

            scoreboard.destroy();
            this.scoreboards.remove(uniqueId);
        });
    }

    public EasyScoreboard getScoreboard(final UUID uniqueId) {
        return scoreboards.get(uniqueId);
    }

    public synchronized void cleanUp() {
        this.provider = null;
    }

    public Map<UUID, EasyScoreboard> getScoreboards() {
        return scoreboards;
    }

    public PluginBase getPlugin() {
        return plugin;
    }

    public EasyScoreboardProvider getProvider() {
        return provider;
    }

    public static void init(final PluginBase plugin) {
        new EasyScoreboardHandler(plugin);
    }

    public static EasyScoreboardHandler get() {
        return instance;
    }
}
