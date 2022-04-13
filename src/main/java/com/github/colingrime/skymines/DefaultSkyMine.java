package com.github.colingrime.skymines;

import com.github.colingrime.SkyMines;
import com.github.colingrime.cache.Cooldown;
import com.github.colingrime.cache.SkyMineCooldownCache;
import com.github.colingrime.skymines.structure.MineStructure;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import com.github.colingrime.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DefaultSkyMine implements SkyMine {

	private final UUID uuid;
	private final UUID owner;
	private final MineStructure structure;
	private final SkyMineUpgrades upgrades;
	private Location home;
	private Cooldown cooldown = new SkyMineCooldownCache(this, 0, TimeUnit.SECONDS);

	public DefaultSkyMine(UUID owner, MineStructure structure, Location home, SkyMineUpgrades upgrades) {
		this(UUID.randomUUID(), owner, structure, home, upgrades);
	}

	public DefaultSkyMine(UUID uuid, UUID owner, MineStructure structure, Location home, SkyMineUpgrades upgrades) {
		this.uuid = uuid;
		this.owner = owner;
		this.structure = structure;
		this.home = home;
		this.upgrades = upgrades;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public UUID getOwner() {
		return owner;
	}

	@Override
	public int getId() {
		List<SkyMine> skyMines = SkyMines.getInstance().getSkyMineManager().getSkyMines(owner);
		for (int i=0; i<skyMines.size(); i++) {
			if (skyMines.get(i).equals(this)) {
				return i + 1;
			}
		}

		return -1;
	}

	@Override
	public MineStructure getStructure() {
		return structure;
	}

	@Override
	public Location getHome() {
		return home;
	}

	@Override
	public void setHome(Location home) {
		this.home = home;
		save();
	}

	@Override
	public SkyMineUpgrades getUpgrades() {
		return upgrades;
	}

	@Override
	public boolean reset() {
		if (!cooldown.isCooldownFinished()) {
			return false;
		}

		resetCooldown();
		structure.buildInside(upgrades.getBlockVarietyUpgrade().getBlockVariety());
		return true;
	}

	private void resetCooldown() {
		cooldown = new SkyMineCooldownCache(this, getUpgrades().getResetCooldownUpgrade().getResetCooldown(), TimeUnit.SECONDS);
		SkyMines.getInstance().addCooldown(cooldown);
	}

	@Override
	public Cooldown getCooldown() {
		return cooldown;
	}

	@Override
	public boolean pickup(Player player) {
		if (!owner.equals(player.getUniqueId())) {
			return false;
		}

		ItemStack token = SkyMines.getInstance().getSkyMineManager().getToken().getToken(structure.getSize(), upgrades);
		if (!player.getInventory().addItem(token).isEmpty()) {
			return false;
		}

		structure.destroy();
		SkyMines.getInstance().getSkyMineManager().removeSkyMine(player, this);
		return true;
	}

	@Override
	public void save() {
		Bukkit.getScheduler().runTaskAsynchronously(SkyMines.getInstance(), () -> {
			try {
				SkyMines.getInstance().getStorage().saveMine(this);
			} catch (Exception e) {
				Logger.severe("SkyMine has failed to save. Please report this to the developer.");
				e.printStackTrace();
			}
		});
	}
}
