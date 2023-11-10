package it.thatskai.litehandlervelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import it.thatskai.litehandlervelocity.commands.AcLogsCommand;
import it.thatskai.litehandlervelocity.commands.AlertsCommand;
import it.thatskai.litehandlervelocity.commands.ConfigReloadCommand;
import it.thatskai.litehandlervelocity.config.ConfigCache;
import it.thatskai.litehandlervelocity.database.SQLProvider;
import it.thatskai.litehandlervelocity.listeners.PlayerListener;
import it.thatskai.litehandlervelocity.managers.AlertsManager;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Plugin(
        id= "litehandler-velocity",
        name = "litehandler-velocity",
        description = "Vulcan & GrimAC Handler for velocity",
        version = "1.0",
        authors = "ThatsKai"
)
public class LiteHandlerVelocity {

    @Getter
    @Inject
    private final Logger logger;

    @Getter
    private final ProxyServer proxyServer;

    @Getter
    private static LiteHandlerVelocity instance;

    @Getter
    private YamlDocument config;

    private final Path directory;

    @Getter
    private SQLProvider sqlProvider;

    @Getter
    private boolean isDatabaseConnect = false;

    @Getter
    private AlertsManager alerts;

    @Getter
    private final ChannelIdentifier channel = new LegacyChannelIdentifier("litehandler");

    @Inject
    public LiteHandlerVelocity(Logger logger, ProxyServer proxyServer, @DataDirectory Path directory) {
        this.logger = logger;
        this.proxyServer = proxyServer;
        this.directory = directory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        instance = this;
        loadConfig();
        ConfigCache.load(config);

        //Load and connect database
        sqlProvider = new SQLProvider(
                getConfig().getString("database.driver"),
                getConfig().getString("database.host"),
                getConfig().getString("database.database"),
                getConfig().getString("database.username"),
                getConfig().getString("database.password"));

        if(getConfig().getBoolean("aclogs.enable")){
            logger.info("Trying to connect the database..");
            if(sqlProvider.connect()){
                CompletableFuture<Void> future = sqlProvider.getLogsTable().createTable();
                future.join();
                sqlProvider.disconnect();
                isDatabaseConnect = true;
                logger.info("Database connected successfully!");
            }else{
                logger.error("Database connection incurred in a error!");
                isDatabaseConnect = false;
            }
            proxyServer.getCommandManager().register("aclogs", new AcLogsCommand());
        }

        alerts = new AlertsManager();

        proxyServer.getChannelRegistrar().register(MinecraftChannelIdentifier.from("vulcan:bungee"));
        proxyServer.getChannelRegistrar().register(channel);

        proxyServer.getEventManager().register(this, new PlayerListener());
        proxyServer.getCommandManager().register("alerts", new AlertsCommand());
        proxyServer.getCommandManager().register("litehandlerreload", new ConfigReloadCommand());

    }

    public void loadConfig(){
        try{
            config = YamlDocument.create(new File(directory.toFile(), "config.yml"),
                    Objects.requireNonNull(getClass().getResourceAsStream("/config.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("configuration-version"))
                            .setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());
            config.update();
            config.save();
        } catch (IOException ex){
            ex.printStackTrace();
            Optional<PluginContainer> plugin = proxyServer.getPluginManager().getPlugin("litehandler-velocity");
            plugin.ifPresent(pluginContainer -> pluginContainer.getExecutorService().shutdown());
        }
    }
}
