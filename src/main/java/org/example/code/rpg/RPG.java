package org.example.code.rpg;

import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.code.rpg.Command.*;
import org.example.code.rpg.Event.*;
import org.example.code.rpg.Manager.GuiManager;
import org.example.code.rpg.Manager.JobConfigManager;
import org.example.code.rpg.Manager.MoneyManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class RPG extends JavaPlugin {
    private GuiManager guiManager;
    private JobConfigManager jobConfigManager;
    private MoneyManager moneyManager;
    private HashMap<UUID, BossBar> playerBossBars = new HashMap<>();
    private Map<UUID, Double> playerO2 = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("MinerSurvival Plugin이 적용되었습니다.");
        this.saveDefaultConfig();

        // 커맨드
        this.getCommand("광부").setExecutor(new JobCommand(this));
        this.getCommand("돈").setExecutor(new MoneyCommand(this));
        this.getCommand("도움말").setExecutor(new PluginHelpCommand(this));
        this.getCommand("메뉴").setExecutor(new GuiCommand(this));

        // 기능
        this.guiManager = new GuiManager(this);
        this.moneyManager = new MoneyManager(this);
        this.jobConfigManager = new JobConfigManager(this);

        // 리스너
        getServer().getPluginManager().registerEvents(new RightClickListener(this), this);
        getServer().getPluginManager().registerEvents(new UnableInstallBedListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, playerBossBars, playerO2), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this, playerBossBars, playerO2), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this, playerO2), this);
        getServer().getPluginManager().registerEvents(new PlayerAttackedListener(playerO2), this);
        getServer().getPluginManager().registerEvents(new MonsterDamageListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this, guiManager, moneyManager), this);
        getServer().getPluginManager().registerEvents(new RenameAnvilListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("MinerSurvival Plugin 적용이 해제되었습니다.");
        this.saveDefaultConfig();
    }

    public JobConfigManager getJobConfig() {
        return jobConfigManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public MoneyManager getMoneyManager() {
        return moneyManager;
    }
}
