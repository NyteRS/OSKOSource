package com.zenyte.game.content.preset;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.testinterfaces.PresetManagerInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.plugins.events.InitializationEvent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tommeh | 20/04/2020 | 00:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class PresetManager {
    private final transient WeakReference<Player> player;
    private final List<Preset> presets;
    private int defaultPreset;
    private int unlockedSlots;
    public String getName() {
        return getName();
    }


    public PresetManager(@NotNull final Player player) {
        this.player = new WeakReference<>(player);
        this.presets = new ObjectArrayList<>();
    }

    @Subscribe
    public static final void onInitialization(final InitializationEvent event) {

        final Player player = event.getPlayer();
        final Player savedPlayer = event.getSavedPlayer();
        final PresetManager manager = savedPlayer.getPresetManager();
        if (manager == null) {
            return;
        }
        final PresetManager thisManager = player.getPresetManager();
        thisManager.defaultPreset = manager.defaultPreset;
        thisManager.unlockedSlots = manager.unlockedSlots;
        if (manager.presets != null) {
            for (final Preset preset : manager.presets) {
                thisManager.presets.add(new Preset(preset));
            }
        }
        player.getPresetManager().ensureDefaultPresets();

    }

    public void ensureDefaultPresets() {
        final Player player = this.player.get();

        Optional<Preset> existing = this.presets.stream().filter(p -> "[pre-made]126 Max Melee".equalsIgnoreCase(p.getName())).findFirst();
        if (!existing.isPresent() || existing.get().getSkillLevels() == null || existing.get().getSkillLevels().isEmpty()) {
            System.err.println("[FIX] Replacing missing or broken Max Melee preset");
            existing.ifPresent(this.presets::remove);
            Map<Integer, Item> meleeEquipment = new HashMap<>();
            meleeEquipment.put(EquipmentSlot.HELMET.getSlot(), new Item(ItemId.HELM_OF_NEITIZNOT));
            meleeEquipment.put(EquipmentSlot.CAPE.getSlot(), new Item(ItemId.CAPE_OF_LEGENDS));
            meleeEquipment.put(EquipmentSlot.AMULET.getSlot(), new Item(ItemId.AMULET_OF_GLORY));
            meleeEquipment.put(EquipmentSlot.WEAPON.getSlot(), new Item(ItemId.DRAGON_SCIMITAR));
            meleeEquipment.put(EquipmentSlot.PLATE.getSlot(), new Item(ItemId.RUNE_PLATEBODY));
            meleeEquipment.put(EquipmentSlot.SHIELD.getSlot(), new Item(ItemId.RUNE_DEFENDER));
            meleeEquipment.put(EquipmentSlot.LEGS.getSlot(), new Item(ItemId.RUNE_PLATELEGS));
            meleeEquipment.put(EquipmentSlot.HANDS.getSlot(), new Item(ItemId.BARROWS_GLOVES));
            meleeEquipment.put(EquipmentSlot.BOOTS.getSlot(), new Item(ItemId.RUNE_BOOTS));
            meleeEquipment.put(EquipmentSlot.RING.getSlot(), new Item(ItemId.RING_OF_RECOIL));

            Map<Integer, Item> meleeInventory = new HashMap<>();
            meleeInventory.put(0, new Item(ItemId.SARADOMIN_BREW4));
            meleeInventory.put(1, new Item(ItemId.SARADOMIN_BREW4));
            meleeInventory.put(2, new Item(ItemId.SHARK));
            meleeInventory.put(3, new Item(ItemId.SHARK));
            meleeInventory.put(4, new Item(ItemId.SANFEW_SERUM4));
            meleeInventory.put(5, new Item(ItemId.SANFEW_SERUM4));
            meleeInventory.put(6, new Item(ItemId.SHARK));
            meleeInventory.put(7, new Item(ItemId.SHARK));
            meleeInventory.put(8, new Item(ItemId.SUPER_STRENGTH4));
            meleeInventory.put(9, new Item(ItemId.SUPER_ATTACK4));
            meleeInventory.put(10, new Item(ItemId.SHARK));
            meleeInventory.put(11, new Item(ItemId.SHARK));
            meleeInventory.put(12, new Item(ItemId.SHARK));
            meleeInventory.put(13, new Item(ItemId.SHARK));
            meleeInventory.put(14, new Item(ItemId.SHARK));
            meleeInventory.put(15, new Item(ItemId.SHARK));
            meleeInventory.put(16, new Item(ItemId.SHARK));
            meleeInventory.put(17, new Item(ItemId.SHARK));
            meleeInventory.put(18, new Item(ItemId.SHARK));
            meleeInventory.put(19, new Item(ItemId.SHARK));
            meleeInventory.put(20, new Item(ItemId.DRAGON_DAGGER));
            meleeInventory.put(21, new Item(ItemId.SHARK));
            meleeInventory.put(22, new Item(ItemId.SHARK));
            meleeInventory.put(23, new Item(ItemId.SHARK));
            meleeInventory.put(24, new Item(ItemId.SHARK));
            meleeInventory.put(25, new Item(ItemId.DEATH_RUNE, 250));
            meleeInventory.put(26, new Item(ItemId.ASTRAL_RUNE, 250));
            meleeInventory.put(27, new Item(ItemId.EARTH_RUNE, 500));

            Map<Integer, Integer> meleeStats = new HashMap<>();
            meleeStats.put(SkillConstants.ATTACK, 99);
            meleeStats.put(SkillConstants.STRENGTH, 99);
            meleeStats.put(SkillConstants.DEFENCE, 99);
            meleeStats.put(SkillConstants.HITPOINTS, 99);
            meleeStats.put(SkillConstants.PRAYER, 99);

            Preset meleePreset = new Preset("[pre-made]126 Max Melee", meleeEquipment, meleeInventory, Spellbook.LUNAR, meleeStats);

            meleePreset.setLocked(true);
            if (meleePreset.getSkillLevels() == null && meleePreset.isLocked()) {
                System.err.println("[BUG] Locked preset created without skill levels: " + meleePreset.getName());
            }
            presets.add(meleePreset);
        }

        Optional<Preset> tribrid = this.presets.stream()
                .filter(p -> "[pre-made]126 Tribrid".equalsIgnoreCase(p.getName()))
                .findFirst();

        if (!tribrid.isPresent() || tribrid.get().getSkillLevels() == null || tribrid.get().getSkillLevels().isEmpty()) {
            System.err.println("[FIX] Replacing missing or broken Tribrid preset");
            tribrid.ifPresent(this.presets::remove);

            Map<Integer, Item> tribridEquipment = new HashMap<>();
            tribridEquipment.put(EquipmentSlot.HELMET.getSlot(), new Item(ItemId.HELM_OF_NEITIZNOT));
            tribridEquipment.put(EquipmentSlot.CAPE.getSlot(), new Item(ItemId.SARADOMIN_CAPE));
            tribridEquipment.put(EquipmentSlot.AMULET.getSlot(), new Item(ItemId.AMULET_OF_GLORY));
            tribridEquipment.put(EquipmentSlot.WEAPON.getSlot(), new Item(ItemId.ANCIENT_STAFF));
            tribridEquipment.put(EquipmentSlot.PLATE.getSlot(), new Item(ItemId.MYSTIC_ROBE_TOP));
            tribridEquipment.put(EquipmentSlot.SHIELD.getSlot(), new Item(ItemId.SPIRIT_SHIELD));
            tribridEquipment.put(EquipmentSlot.LEGS.getSlot(), new Item(ItemId.MYSTIC_ROBE_BOTTOM));
            tribridEquipment.put(EquipmentSlot.HANDS.getSlot(), new Item(ItemId.BARROWS_GLOVES));
            tribridEquipment.put(EquipmentSlot.BOOTS.getSlot(), new Item(ItemId.CLIMBING_BOOTS));
            tribridEquipment.put(EquipmentSlot.RING.getSlot(), new Item(ItemId.RING_OF_RECOIL));
            tribridEquipment.put(EquipmentSlot.AMMUNITION.getSlot(), new Item(ItemId.DRAGONSTONE_BOLTS_E, 100));

            Map<Integer, Item> tribridInventory = new HashMap<>();
            tribridInventory.put(0, new Item(ItemId.SANFEW_SERUM4));
            tribridInventory.put(1, new Item(ItemId.SANFEW_SERUM4));
            tribridInventory.put(2, new Item(ItemId.SANFEW_SERUM4));
            tribridInventory.put(3, new Item(ItemId.SANFEW_SERUM4));
            tribridInventory.put(4, new Item(ItemId.SARADOMIN_BREW4));
            tribridInventory.put(5, new Item(ItemId.SARADOMIN_BREW4));
            tribridInventory.put(6, new Item(ItemId.SARADOMIN_BREW4));
            tribridInventory.put(7, new Item(ItemId.SARADOMIN_BREW4));
            tribridInventory.put(8, new Item(ItemId.RUNE_CROSSBOW));
            tribridInventory.put(9, new Item(ItemId.DRAGON_SCIMITAR));
            tribridInventory.put(10, new Item(ItemId.SARADOMIN_BREW4));
            tribridInventory.put(11, new Item(ItemId.SARADOMIN_BREW4));
            tribridInventory.put(12, new Item(ItemId.BLACK_DHIDE_BODY));
            tribridInventory.put(13, new Item(ItemId.RUNE_DEFENDER));
            tribridInventory.put(14, new Item(ItemId.SARADOMIN_BREW4));
            tribridInventory.put(15, new Item(ItemId.SARADOMIN_BREW4));
            tribridInventory.put(16, new Item(ItemId.RUNE_PLATELEGS));
            tribridInventory.put(17, new Item(ItemId.DRAGON_DAGGERP));
            tribridInventory.put(18, new Item(ItemId.SHARK));
            tribridInventory.put(19, new Item(ItemId.SARADOMIN_BREW4));
            tribridInventory.put(20, new Item(ItemId.MAGIC_POTION4));
            tribridInventory.put(21, new Item(ItemId.SUPER_STRENGTH4));
            tribridInventory.put(22, new Item(ItemId.SUPER_ATTACK4));
            tribridInventory.put(23, new Item(ItemId.SHARK));
            tribridInventory.put(24, new Item(ItemId.SHARK));
            tribridInventory.put(25, new Item(ItemId.BLOOD_RUNE, 10000));
            tribridInventory.put(26, new Item(ItemId.WATER_RUNE, 10000));
            tribridInventory.put(27, new Item(ItemId.DEATH_RUNE, 10000));

            Map<Integer, Integer> tribridStats = new HashMap<>();
            tribridStats.put(SkillConstants.ATTACK, 95);
            tribridStats.put(SkillConstants.STRENGTH, 99);
            tribridStats.put(SkillConstants.DEFENCE, 99);
            tribridStats.put(SkillConstants.HITPOINTS, 99);
            tribridStats.put(SkillConstants.RANGED, 99);
            tribridStats.put(SkillConstants.PRAYER, 99);
            tribridStats.put(SkillConstants.MAGIC, 99);

            Preset tribridPreset = new Preset("[pre-made]126 Tribrid", tribridEquipment, tribridInventory, Spellbook.ANCIENT, tribridStats);
            tribridPreset.setLocked(true);
            if (tribridPreset.getSkillLevels() == null && tribridPreset.isLocked()) {
                System.err.println("[BUG] Locked preset created without skill levels: " + tribridPreset.getName());
            }
            presets.add(tribridPreset);
        }


        Optional<Preset> hybrid = this.presets.stream()
                .filter(p -> "[pre-made]126 Hybrid".equalsIgnoreCase(p.getName()))
                .findFirst();
        if (!hybrid.isPresent() || hybrid.get().getSkillLevels() == null || hybrid.get().getSkillLevels().isEmpty()) {
            System.err.println("[FIX] Replacing missing or broken Hybrid preset");
            hybrid.ifPresent(this.presets::remove);


            Map<Integer, Item> hybridEquipment = new HashMap<>();
            hybridEquipment.put(EquipmentSlot.HELMET.getSlot(), new Item(ItemId.HELM_OF_NEITIZNOT));
            hybridEquipment.put(EquipmentSlot.CAPE.getSlot(), new Item(ItemId.SARADOMIN_CAPE));
            hybridEquipment.put(EquipmentSlot.AMULET.getSlot(), new Item(ItemId.AMULET_OF_GLORY));
            hybridEquipment.put(EquipmentSlot.WEAPON.getSlot(), new Item(ItemId.ANCIENT_STAFF));
            hybridEquipment.put(EquipmentSlot.PLATE.getSlot(), new Item(ItemId.MYSTIC_ROBE_TOP));
            hybridEquipment.put(EquipmentSlot.SHIELD.getSlot(), new Item(ItemId.SPIRIT_SHIELD));
            hybridEquipment.put(EquipmentSlot.LEGS.getSlot(), new Item(ItemId.MYSTIC_ROBE_BOTTOM));
            hybridEquipment.put(EquipmentSlot.HANDS.getSlot(), new Item(ItemId.BARROWS_GLOVES));
            hybridEquipment.put(EquipmentSlot.BOOTS.getSlot(), new Item(ItemId.CLIMBING_BOOTS));
            hybridEquipment.put(EquipmentSlot.RING.getSlot(), new Item(ItemId.RING_OF_RECOIL));
            // DRAGONSTONE_BOLTS_E intentionally omitted

            Map<Integer, Item> hybridInventory = new HashMap<>();
            hybridInventory.put(0, new Item(ItemId.SANFEW_SERUM4));
            hybridInventory.put(1, new Item(ItemId.SANFEW_SERUM4));
            hybridInventory.put(2, new Item(ItemId.SANFEW_SERUM4));
            hybridInventory.put(3, new Item(ItemId.SANFEW_SERUM4));
            hybridInventory.put(4, new Item(ItemId.SARADOMIN_BREW4));
            hybridInventory.put(5, new Item(ItemId.SARADOMIN_BREW4));
            hybridInventory.put(6, new Item(ItemId.SARADOMIN_BREW4));
            hybridInventory.put(7, new Item(ItemId.SARADOMIN_BREW4));
            hybridInventory.put(8, new Item(ItemId.SHARK)); // was RUNE_CROSSBOW
            hybridInventory.put(9, new Item(ItemId.DRAGON_SCIMITAR));
            hybridInventory.put(10, new Item(ItemId.SARADOMIN_BREW4));
            hybridInventory.put(11, new Item(ItemId.SARADOMIN_BREW4));
            hybridInventory.put(12, new Item(ItemId.BLACK_DHIDE_BODY));
            hybridInventory.put(13, new Item(ItemId.RUNE_DEFENDER));
            hybridInventory.put(14, new Item(ItemId.SARADOMIN_BREW4));
            hybridInventory.put(15, new Item(ItemId.SARADOMIN_BREW4));
            hybridInventory.put(16, new Item(ItemId.RUNE_PLATELEGS));
            hybridInventory.put(17, new Item(ItemId.DRAGON_DAGGERP));
            hybridInventory.put(18, new Item(ItemId.SHARK));
            hybridInventory.put(19, new Item(ItemId.SARADOMIN_BREW4));
            hybridInventory.put(20, new Item(ItemId.MAGIC_POTION4));
            hybridInventory.put(21, new Item(ItemId.SUPER_STRENGTH4));
            hybridInventory.put(22, new Item(ItemId.SUPER_ATTACK4));
            hybridInventory.put(23, new Item(ItemId.SHARK));
            hybridInventory.put(24, new Item(ItemId.SHARK));
            hybridInventory.put(25, new Item(ItemId.BLOOD_RUNE, 10000));
            hybridInventory.put(26, new Item(ItemId.WATER_RUNE, 10000));
            hybridInventory.put(27, new Item(ItemId.DEATH_RUNE, 10000));

            Map<Integer, Integer> hybridStats = new HashMap<>();
            hybridStats.put(SkillConstants.ATTACK, 90);
            hybridStats.put(SkillConstants.STRENGTH, 99);
            hybridStats.put(SkillConstants.DEFENCE, 99);
            hybridStats.put(SkillConstants.HITPOINTS, 99);
            hybridStats.put(SkillConstants.RANGED, 99);
            hybridStats.put(SkillConstants.PRAYER, 99);
            hybridStats.put(SkillConstants.MAGIC, 99);

            Preset hybridPreset = new Preset("[pre-made]126 Hybrid", hybridEquipment, hybridInventory, Spellbook.ANCIENT, hybridStats);  hybridPreset.setLocked(true);
            if (hybridPreset.getSkillLevels() == null && hybridPreset.isLocked()) {
                System.err.println("[BUG] Locked preset created without skill levels: " + hybridPreset.getName());
            }

            presets.add(hybridPreset);
        }
        Optional<Preset> pure = this.presets.stream()
                .filter(p -> "[pre-made]Pure Melee".equalsIgnoreCase(p.getName()))
                .findFirst();
        if (!pure.isPresent() || pure.get().getSkillLevels() == null || pure.get().getSkillLevels().isEmpty()) {
            System.err.println("[FIX] Replacing missing or broken Pure Melee preset");
            pure.ifPresent(this.presets::remove);
            Map<Integer, Item> pureMeleeEquipment = new HashMap<>();
            pureMeleeEquipment.put(EquipmentSlot.HELMET.getSlot(), new Item(ItemId.BEARHEAD));
            pureMeleeEquipment.put(EquipmentSlot.CAPE.getSlot(), new Item(ItemId.CAPE_OF_LEGENDS));
            pureMeleeEquipment.put(EquipmentSlot.AMULET.getSlot(), new Item(ItemId.AMULET_OF_GLORY));
            pureMeleeEquipment.put(EquipmentSlot.WEAPON.getSlot(), new Item(ItemId.DRAGON_SCIMITAR));
            pureMeleeEquipment.put(EquipmentSlot.PLATE.getSlot(), new Item(ItemId.MONKS_ROBE_TOP));
            pureMeleeEquipment.put(EquipmentSlot.SHIELD.getSlot(), new Item(ItemId.BOOK_OF_LAW));
            pureMeleeEquipment.put(EquipmentSlot.LEGS.getSlot(), new Item(ItemId.BLACK_DHIDE_CHAPS));
            pureMeleeEquipment.put(EquipmentSlot.HANDS.getSlot(), new Item(ItemId.MITHRIL_GLOVES));
            pureMeleeEquipment.put(EquipmentSlot.BOOTS.getSlot(), new Item(ItemId.CLIMBING_BOOTS));
            pureMeleeEquipment.put(EquipmentSlot.RING.getSlot(), new Item(ItemId.RING_OF_RECOIL));

            Map<Integer, Item> pureMeleeInventory = new HashMap<>();
            pureMeleeInventory.put(0, new Item(ItemId.SARADOMIN_BREW4));
            pureMeleeInventory.put(1, new Item(ItemId.SARADOMIN_BREW4));
            pureMeleeInventory.put(2, new Item(ItemId.SHARK));
            pureMeleeInventory.put(3, new Item(ItemId.SHARK));
            pureMeleeInventory.put(4, new Item(ItemId.SANFEW_SERUM4));
            pureMeleeInventory.put(5, new Item(ItemId.SANFEW_SERUM4));
            pureMeleeInventory.put(6, new Item(ItemId.SHARK));
            pureMeleeInventory.put(7, new Item(ItemId.SHARK));
            pureMeleeInventory.put(8, new Item(ItemId.SUPER_STRENGTH4));
            pureMeleeInventory.put(9, new Item(ItemId.SUPER_ATTACK4));
            pureMeleeInventory.put(10, new Item(ItemId.SHARK));
            pureMeleeInventory.put(11, new Item(ItemId.SHARK));
            pureMeleeInventory.put(12, new Item(ItemId.SHARK));
            pureMeleeInventory.put(13, new Item(ItemId.SHARK));
            pureMeleeInventory.put(14, new Item(ItemId.SHARK));
            pureMeleeInventory.put(15, new Item(ItemId.SHARK));
            pureMeleeInventory.put(16, new Item(ItemId.SHARK));
            pureMeleeInventory.put(17, new Item(ItemId.SHARK));
            pureMeleeInventory.put(18, new Item(ItemId.SHARK));
            pureMeleeInventory.put(19, new Item(ItemId.SHARK));
            pureMeleeInventory.put(20, new Item(ItemId.DRAGON_DAGGER));
            pureMeleeInventory.put(21, new Item(ItemId.SHARK));
            pureMeleeInventory.put(22, new Item(ItemId.SHARK));
            pureMeleeInventory.put(23, new Item(ItemId.SHARK));
            pureMeleeInventory.put(24, new Item(ItemId.DRAGON_2H_SWORD));
            pureMeleeInventory.put(25, new Item(ItemId.SHARK));
            pureMeleeInventory.put(26, new Item(ItemId.SHARK));
            pureMeleeInventory.put(27, new Item(ItemId.SHARK));

            Map<Integer, Integer> pureMeleeStats = new HashMap<>();
            pureMeleeStats.put(SkillConstants.ATTACK, 75);
            pureMeleeStats.put(SkillConstants.STRENGTH, 99);
            pureMeleeStats.put(SkillConstants.DEFENCE, 1);
            pureMeleeStats.put(SkillConstants.HITPOINTS, 99);
            pureMeleeStats.put(SkillConstants.RANGED, 99);
            pureMeleeStats.put(SkillConstants.PRAYER, 52);
            pureMeleeStats.put(SkillConstants.MAGIC, 99);

            Preset pureMeleePreset = new Preset("[pre-made]Pure Melee", pureMeleeEquipment, pureMeleeInventory, Spellbook.NORMAL, pureMeleeStats);
            pureMeleePreset.setLocked(true);

            if (pureMeleePreset.getSkillLevels() == null && pureMeleePreset.isLocked()) {
                System.err.println("[BUG] Locked preset created without skill levels: " + pureMeleePreset.getName());
            }

            presets.add(pureMeleePreset);
        }



        revalidatePresets();

    }



    public void revalidatePresets() {
        final int max = getMaximumPresets();
        final int current = getTotalPresets();
        for (int i = 0; i < current; i++) {
            final Preset preset = presets.get(i);
            preset.setAvailable(i < max);
        }
        if (defaultPreset >= max) {
            defaultPreset = 0;
        }
    }

    public void addPreset(final int index, final String name) {
        final Player player = this.player.get();
        if (player == null) {
            return;
        }
        presets.add(index, new Preset(name, player));
        revalidatePresets();
    }

    public void setPreset(final int index, @NotNull final Preset preset) {
        presets.set(index, preset);
    }

    @Nullable
    public Preset getPreset(final int index) {
        if (index < 0 || index >= getTotalPresets()) {
            return null;
        }
        return presets.get(index);
    }

    public int getTotalPresets() {
        return presets.size();
    }

    public int getMaximumPresets() {
        int availablePresets = 4;
        final Player player = this.player.get();
        if (player == null) {
            throw new IllegalStateException();
        }
        //Donator rank slots
        availablePresets += getSlotsAmount(player);
        return availablePresets;
    }

    public static int getSlotsAmount(Player player) {
        switch (player.getMemberRank()) {
            case PREMIUM:
                return 5;
            case EXPANSION:
                return 10;
            case EXTREME:
                return 15;
            case RESPECTED:
                return 20;
            case LEGENDARY:
                return 30;
            case MYTHICAL:
                return 40;
            case UBER:
            case AMASCUT:
                return 50;
        }
        return 0;
    }

    public void setDefaultPreset(final int slot) {
        if (slot < 0 || slot >= getTotalPresets()) {
            return;
        }
        this.defaultPreset = slot;
    }

    public List<Preset> getPresets() {
        return presets;
    }

    public int getDefaultPreset() {
        return defaultPreset;
    }

    public int getUnlockedSlots() {
        return unlockedSlots;
    }

    public void loadLastPreset() {
        final Player player = this.player.get();
        if (player == null) {
            return;
        }

        int index = player.getNumericAttributeOrDefault("last preset loaded", -1).intValue();
        if (index <= -1) {
            player.sendMessage("You haven't loaded a preset yet.");
            return;
        }



    }

}
