# 1.13.x
## 1.13
### Additions
- [ ] - Blocks
    - [x] - Blue ice
    - [ ] - Bubble columns (magma and soul sand underwater)
    - [x] - Buttons, pressure plates and trapdoors (from all wood)
        - [x] - Buttons
        - [x] - Pressure plates
        - [x] - Trapdoors
    - [x] - Normal pumpkin
    - [ ] - Conduits
    - [ ] - Corals, coral fans, coral blocks + dead versions
    - [ ] - Dried kelp blocks
    - [ ] - Kelp
    - [x] - Prismarine stairs and slabs
    - [ ] - Seagrass
    - [x] - Stripped logs and wood
        - [x] - Logs
        - [x] - Wood
    - [ ] - Turtle eggs
- [ ] - Items
    - [x] - Arrow and potions of Slow Falling and Turtle Master
    - [ ] - Buried treasure exploration maps
    - [ ] - Dried kelp
    - [ ] - Debug stick (*maybe*)
    - [ ] - Buckets of fish
    - [ ] - Heart of the sea and Nautilus shells
    - [ ] - Mushroom blocks and stems (to CreativeTab)
    - [ ] - Petrified oak slab (Easter eggs)
    - [ ] - Phantom membranes
    - [ ] - Scutes
    - [ ] - Smooth quartz
    - [ ] - Trident (weapon)
    - [ ] - Turtle shells
    - [x] - Wood blocks
- [ ] Mobs
    - [ ] - Dolphin
    - [ ] - Drowned
    - [ ] - Fish
    - [ ] - Phantoms
    - [ ] - Turtles
- [ ] - Worldgen
    - [ ] - Biomes
        - [ ] - End
            - [ ] - Small End Island
            - [ ] - End Midlands
            - [ ] - End Highlands
            - [ ] - End Barrens
        - [ ] - Ocean
            - [ ] - Warm Ocean
            - [ ] - Lukewarm Ocean
            - [ ] - Cold Ocean
            - [ ] - Deep Warm Ocean
            - [ ] - Deep Lukewarm Ocean
            - [ ] - Deep Frozen Ocean
        - [ ] - Frozen Ocean
    - [ ] - Buffet (*maybe, low priority*)
    - [ ] - Buried treasures
    - [ ] - Coral reefs
    - [ ] - Icebergs
    - [ ] - Shipwrecks
    - [ ] - Underwater caves
    - [ ] - Ocean ruins
- [ ] - Gameplay
    - [ ] - Enchantments
        - [ ] - [Channeling](https://minecraft.fandom.com/wiki/Channeling)
        - [ ] - [Impaling](https://minecraft.fandom.com/wiki/Impaling)
        - [ ] - [Loyalty](https://minecraft.fandom.com/wiki/Loyalty)
        - [ ] - [Riptide](https://minecraft.fandom.com/wiki/Riptide)
    - [ ] - Map markers
        > * Added the ability to put markers on maps.
        > * Use on a banner with a map to add it to the map.
        > * Right click on the same banner again to remove it.
        > * That map will show the base color of the banner at that spot.
        > * Named banners will show their name on the map.
        > * If a banner is destroyed, it will disappear when the player gets close while holding the map.
        > * Uses the new banners nbt for maps.
    - [ ] - Movement
        - [ ] - Swimming
            > * When sprinting while in water, the player will now swim on the surface.
            > * Much faster than walking/running in water before.
            > * Pressing shift causes the player to rapidly dive down.
            > * Sprinting at the surface of water doesn't make the player swim, instead the player will stay at the same altitude constantly.
            > * The players hitbox is only as large as 0.6×0.6 blocks (same as while flying with an elytra) while swimming.
            > * Vertically and horizontally, the player can fit through a one block gap like this.
    - [ ] - Status effects
        - [ ] - [Conduit Power](https://minecraft.fandom.com/wiki/Conduit_Power)
        - [ ] - [Dolphin's Grace](https://minecraft.fandom.com/wiki/Dolphin%27s_Grace)
        - [ ] - [Slow Falling](https://minecraft.fandom.com/wiki/Slow_Falling)
- [ ] - Commands
    - [ ] - General
        > * Added a command UI that shows when commands are typed in the chat.
        > * Different components of commands will be displayed in different colors.
        > * Errors will be displayed in red without having to run the command.
        > * Added command suggestions for entity selectors.
        > * An nbt argument in target selectors.
        > * A new command parsing library known as "Brigadier".
    - [ ] - Coordinates
        > Added a new local coordinate type in commands using `^`.
        >    * When specifying coordinates in a command, the player can now use `^` to specify local coordinates instead of world coordinates.
        >      * The axes used for local coordinates are relative to the execution rotation, defaulting to `0,0` (south).
        >      * Like world coordinates, they are by default measured from the base of an entity.
        >      * The syntax is: `^left ^up ^forwards`.
        >      * `left/up/forwards` is the amount of blocks in the specified direction.
    - [ ] - Specific commands
        - [ ] - `/bossbar`
            - [ ] - `/bossbar create <id> <name>` will create a boss bar.
                > * `id` is used to target the boss bar and is in the form `namespace:name`, for example: `foo:bar`. If no `namespace` is specified it defaults to `minecraft`.
                > * `name` is the display name of the boss bar and only accepts a JSON text component.
            - [ ] - `/bossbar set <id> name <name>` will change the name of the boss bar.
            - [ ] - `/bossbar set <id> color <color>` will change the color of the text (if no color was specified as part of a text component) and the boss bar, defaults to `white`.
            - [ ] - `/bossbar set <id> style <style>` will change the style of the boss bar, defaults to `progress`
                > * Available options are: `notched_6`, `notched_10`, `notched_12`, `notched_20`, and `progress`.
                > * `notched` will set the amount of segments.
                > * `progress` will set the amount of segments to 1.
            - [ ] - `/bossbar set <id> value <value>` will change the current value of the boss bar, defaults to `0`.
            - [ ] - `/bossbar set <id> max <max>` will change the maximum value of the boss bar, defaults to `100`.
            - [ ] - `/bossbar set <id> visible <visible>` will change the visibility of the boss bar, defaults to `true`.
            - [ ] - `/bossbar set <id> players <players>` will change which players can see the boss bar, defaults to none.
            - [ ] - `/bossbar remove <id>` will remove the boss bar.
            - [ ] - `/bossbar list` will display a list of created boss bars.
            - [ ] - `/bossbar get <id> (max|players|value|visible)` will return the requested setting as a `result` of the command.
        - [ ] - `/data`
            > A command that allows the player to get, merge, and remove entity and block nbt data.
            - [ ] - `/data get block <pos> [<path>] [<scale>]`
                > Will return the NBT data from the block at `pos`. A `path` can be specified to only retrieve that nbt data. Numeric values will be set as the `result` of the command, strings will set the length of the string as the `result`, lists will set the number of elements in the list as the `result`, and compounds will set the number of tags that are directly in that compound as the `result`. An optional `scale` can be provided to scale the number retrieved.
            - [ ] - `/data get entity <target> [<path>] [<scale>]`
                > Will return the NBT data from one `target` entity. A `path` can be specified to only retrieve that nbt data. Numeric values will be set as the `result` of the command, strings will set the length of the string as the `result`, lists will set the number of elements in the list as the `result`, and compounds will set the number of tags that are directly in that compound as the `result`. An optional `scale` can be provided to scale the number retrieved.
            - [ ] - `/data merge block <pos> <nbt>`
                > Will merge the block nbt data at `pos` with the specified `nbt` data.
            - [ ] - `/data merge entity <target> <nbt>`
                > Will merge the entity nbt data from `target` with the specified `nbt` data. Merging player nbt data is not allowed.
            - [ ] - `/data remove block <pos> <path>`
                > Will remove nbt data at `path` from the block at `pos`.
            - [ ] - `/data remove entity <target> <path>`
                > Will remove nbt data at `path` from one `target` entity. Removing player nbt data is not allowed.
                    
            > Data paths look like this: `foo.bar[0]."A [crazy name]".baz`.
            >    * `foo.bar` means foo's child called bar.
            >      * `bar[0]` means element 0 of bar.
            >      * "quoted strings" may be used if a name of a key needs to be escaped.
            
            > Examples of old commands:
            >    * `/entitydata <target> {}` is now `/data get entity <target>`
            >      * `/blockdata <pos> <nbt>` is now `/data merge block <pos> <nbt>`

            > Examples of new functionalities:
            >    * `/data get entity @e[type=pig,limit=1] Saddle 2`
            >      * `/data remove block 17 45 34 Items`
        - [ ] - `/datapack`
            > * A command to control loaded data packs.
            > * Has the following subcommands:
            >   * `enable <name>` - will enable the specific pack.
            >   * `disable <name>` - will disable the specific pack.
            >   * `list [available|enabled]` - will list all data packs, or only the available/enabled ones.
            > * Data packs are enabled by default, but if the player disables it, they can re-enable it with these commands:
            >   * `enable <name>` - will enable the specific pack, putting it in its default position.
            >   * `enable <name> first` - will enable the specific pack, putting it before any other pack (lowest priority).
            >   * `enable <name> last` - will enable the specific pack, putting it after any other pack (highest priority).
            >   * `enable <name> before <existing>` - will enable the specific pack, putting it before (lower priority) <existing> pack.
            >   * `enable <name> after <existing>` - will enable the specific pack, putting it after (higher priority) <existing> pack.
        - [ ] - `/locate`
            > Added a clickable teleport link to the command output.
        - [ ] - `/scoreboard`
            > * Added /scoreboard objectives modify <objective> rendertype hearts.
            >   * Makes health bars display as hearts, like this: ♥♥♥♥♥♥.
            > * Added /scoreboard objectives modify <objective> rendertype integer.
            >   * Makes health bars display as yellow numbers, like this: 12.
        - [ ] - `/teleport`
            > Added facing.
            >    * `/teleport [<targets>] (<location>|<destination>) facing (<facingEntity>|<facingLocation>)`.
            >      * Will rotate an entity to face either an entity or a location.
        - [ ] - `/time`
            > Added `noon` and `midnight` to `/time set`.
- [ ] - General
    - [ ] - Advancements
        > * Added 4 new advancements.
        >   * Fishy Business: Catch a fish.
        >   * Tactical fishing: Catch a fish... without a fishing rod!
        >   * A Throwaway Joke: Throw a trident at something.
        >   * Very Very Frightening: Strike a Villager with lightning.
        > * Added three new advancement triggers.
        >   * `minecraft:fishing_rod_hooked` triggers when a player reels in an item or entity.
        >   * `minecraft:channeled_lightning` triggers when a player uses the channeling enchantment to strike a mob.
        >   * `minecraft:filled_bucket` triggers when a player fills a bucket.
    - [ ] - Data packs `P.s. idk, maybe I'll do it`
        > * Like resource packs, but for loot tables, advancements, functions, structures, recipes and tags.
        >   * Used by placing them into the `datapacks` folder of a world.
        > * Data packs are `.zip` files or folders, with a `pack.mcmeta` in the root. See: [Tutorials/Creating a resource pack#pack.mcmeta](https://minecraft.fandom.com/wiki/Tutorials/Creating_a_resource_pack#pack.mcmeta). The packs are located in `(world)/datapacks/`.
        > * Structures will load from `(world)/generated/structures/(namespace)/(file).nbt` before checking data packs.
        >   * However, this directory should not be used to distribute structures. Instead, move these files into data packs.
        > * Reloadable using `/reload`.
        > * Structure: `pack.mcmeta`, `data` folder containing a namespace folder determining the namespace of its contents.
        >   * A namespace should only contain the following symbols: `0123456789abcdefghijklmnopqrstuvwxyz-_`.
        >   * Inside the namespace folder, there can be folders for `functions`, `loot_tables`, `advancements`, `structures`, `recipes` and `tags`.
- [ ] - Death messages
    > * Added a death message for when the player is blown up by a bed in the Nether or the End.
    >   * `<player> was killed by [Intentional Game Design]`
    >     * Clicking on "[Intentional Game Design]" opens a link to MCPE-28723.
    > * Added a death message for when a mob/player pushes someone into the void or when someone uses `/kill` after being attacked by a mob/player.
    >   * `<player> didn't want to live in the same world as <killer>`
    > * Added a death message for when the player is killed by somebody using a trident.
    >   * `<player> was impaled by <killer>`
- [ ] - Debug screen `P.s. idk, maybe I'll do it`
    > * `F3`+`C` will now copy the player's current location to clipboard.
    >   * Now gives a warning before forcing a debug crash.
    > * F3 debug overlay now shows the fluid the player is looking at, separately from blocks.
    >   * Player can be up to twenty blocks away for this to work now.
    > * Added "Targeted Entity". Displays information for entities up to 4 blocks away (counting from the entities hitbox).
    > * Added `⇧ Shift`+`F3`+`I` to copy the client-side data of targeted block or entity. It can be used by anyone.
    > * Added `F3`+`I` to copy targeted block or entity server-side data to clipboard. It can only be used by operators.
    > * Added information about the time it takes for a tick on the integrated server (singleplayer only), server brand (multiplayer only), number of packets sent by the client (tx), and number of packets received by the client (rx).
- [ ] - NBT tags
    >* Added the `TreasurePosX`, `TreasurePosY`, `TreasurePosZ`, `GotFish`, and `CanFindTreasure` NBT tags for dolphins.
    >* Added the `AX`, `AY`, `AZ`, and `Size` NBT tags for phantoms.
    >* Added the `HomePosX`, `HomePosY`, `HomePosZ`, `TravelPosX`, `TravelPosY`, `TravelPosZ`, and `HasEgg` NBT tags for turtles.
- [ ] - Particles
    - [ ] - Added the `minecraft:bubble_column_up`, `minecraft:bubble_pop`, `minecraft:current_down`, and `minecraft:squid_ink particles`.
- [] - Sounds
  - [ ] - Added a new sound effect of squid shooting ink.
  - [ ] - Added sound for Husks converting to Zombies
  - [ ] - Added underwater ambience sounds.
  - [ ] - New cave ambience sound: Cave19.ogg.
  - [ ] - Added new sound events:
      > * `block.coral_block.break`, `block.coral_block.fall`, `block.coral_block.hit`, `block.coral_block.place`, and `block.coral_block.step`
      >     * Used for the living coral blocks.
      >     * Most of these sounds like mixture of rocky and high-pitched slimy sounds.
      > * `block.wet_grass.break`, `block.wet_grass.fall`, `block.wet_grass.hit`, `block.wet_grass.place`, and `block.wet_grass.step`
      >     * Used for seagrass, kelp, coral and coral fans.
- [ ] - Statistics
    - [ ] - Added the `time_since_rest` statistic.
        > * Used by phantoms.
        > * Reset when the player leaves their bed, and when the player dies.
- [ ] - Waterlogging
    > * Water can now be placed in the following blocks: chests, trapped chests, stairs, slabs, fences, walls, iron bars, glass panes, ender chests, trapdoors, ladders, and signs.
    >   * Water can flow out of these blocks, but cannot flow into them.
    >   * When full of water, they will count as water blocks for all gameplay (such as swimming).
    >   * Water will flow out of all faces of the block except for solid faces.
    > * Removed the blocks `flowing_water` and `flowing_lava`.
    > * All of the blocks that water can be placed in now have the block state `waterlogged`.
    > * Blocks such as bubble column or kelp will always count as a water source.
    > * When water spreads and would later turn into a source block, it now immediately just places a source block.
### Changes
- [ ] - Blocks
    - [ ] - General
        - [ ] - Blocks which used to have no bottom texture (like repeaters, comparators, etc.) now have a bottom texture, not including redstone wire.
        - [ ] - Blocks with a collision box now have matching bounding boxes.
            > * Affected: anvils, cauldrons, hoppers, fences, iron bars, glass panes, filled end portal frames, vines, lily pads, stairs, brewing stands, and pistons.
            > * Updated the collision box of anvils and hoppers.
            > * Does not affect blocks with a collision box smaller than their model, such as soul sand and snow layers.
    - [ ] - Beacons
        - [ ] - Added new sounds.
    - [ ] - Beds
        - [ ] - Changed message shown when failing to use a bed to say "You can sleep only at night and during thunderstorms."
    - [ ] - Cactus
        - [ ] - Now break if signs or banners are placed directly next to them.
            > Previously, these blocks couldn't be placed like this.
    - [ ] - Chests and trapped chests
        - [ ] - They can be put directly next to their double variants instead of requiring one block between them.
            > Shift right-clicking a chest or trapped chest will only make it try to connect to the clicked chest or trapped chest if possible, else it'll become a single chest or trapped chest.
    - [ ] - Dispensers
        > Crafting no longer requires a fully repaired bow.
    - [ ] - Ender Chests
        > Will now change their texture when the computer time is set to the 24th to 26th of December to suit Christmas.
    - [ ] - Fence gates
        > Placing them no longer requires a block below them.
    - [ ] - Leaves
        - [ ] - Naturally-generated leaves now survive at a distance of up to 6 blocks from logs, instead of 4.
            > The block state for leaves changed from a `check_decay` and `decayable` Booleans to distance (ranging from 1 to 7) and a `persistent` Boolean.
    - [ ] - Levers
        > Flicking a lever on now displays redstone particles.
    - [ ] - Magma blocks
        > Now generate at the bottom of ocean ravines, creating downward bubble columns.
    - [ ] - Monster eggs (Infected blocks)
        > They will now break instantly, no matter the tool.
        >> When broken with Silk Touch, the non-infested counterpart of the block will drop.
    - [ ] - Packed ice
        > Now can be crafted from 9 ice.
    - [ ] - Pumpkins
        > Placing them no longer requires a block below them.
    - [ ] - Shulker Boxes
        - [ ] - Changed the purple shulker box to the 1.12 snapshots' purple color.
        - [ ] - Dyed shulker boxes can now be undyed in a cauldron.
            > * Use a shulker box on a filled cauldron.
            > * The water level in the cauldron will decrease by 1.
    - [ ] - Vines
        > Multiple vines facing different directions, including on the bottom of blocks, can now be placed in the same block space.
    - [ ] - Water
        - [ ] - Has new colors, depending on the biome.
            > * Dark purple for frozen, indigo for cold, blue/regular for medium (lush), light green for warm/dry biomes.
            >> *  Swamps have a light green-gray hue, and lukewarm ocean a light teal.
        - [ ] - Now only blocks 1 light per block, instead of 3.
            > This only affects newly placed water (for now).
- [ ] - Items
    - [ ] - Carrot on a stick
        > Can now be crafted with a fishing rod that does not have full durability.
    - [ ] - Elytra
        > Now require phantom membrane to be repaired instead of leather.
    - [ ] - Fish
        > Item textures changed.
    - [ ] - Fishing rods
        > Now make sounds when reeled back in.
    - [ ] - Iron horse armor
        > Changed the texture when equipped.
    - [ ] - Maps
        > [Maps changed slightly in regards to which blocks are shown and which blocks are not](https://www.reddit.com/r/Minecraft/comments/8xo1ex/minecraft_113_map_rendering_changes/)
- [ ] - Mobs
    - [ ] - General
        - [ ] - Zombies, skeletons, ocelots and wolves will naturally attack baby turtles, and zombies and zombie pigmen will seek out and trample turtle eggs.
        - [ ] - Undead mobs will now sink in water.
    - [ ] - Horses
        - [ ] - [The model has been changed to be more consistent with other mobs](https://www.minecraft.net/en-us/article/meet-horse).
        - [ ] - Some animations like opening its mouth when grazing have been removed from the model as well.
    - [ ] - Husks
        > Husks now become a zombie instead of dying from drowning.
    - [ ] - Parrots
        > They will now imitate phantoms and drowned.
    - [ ] - Polar bears
        > They can now spawn on top of ice.
    - [ ] - Skeleton horses
        - [ ] - Are now rideable underwater.
        - [ ] - Updated model to fix minor texture z-fighting, a glitch where textures overlap in an obtrusive and unintentional way.
    - [ ] - Squid
        > Squid now shoot ink and flee quickly in response to being attacked.
    - [ ] - Zombie horses
        > Updated model to fix extreme texture z-fighting, just like the skeleton horse.
    - [ ] - Zombies
        - [ ] - Zombies now become a drowned instead of dying from drowning.
        - [ ] - Baby zombies now burn in sunlight.
        - [ ] - Chicken jockeys now spawn rightly (also valid for zombie pigmen).
- [ ] - Non-mob entities
    - [ ] - General
        - [ ] - Items and experience orbs will now float up in water.
        - [ ] - Changed the name of several entities: `idk maybe`
        > * `Block of TNT`        ->  `Primed TNT   `
        > * `Bolt of Lightning`   ->  `Lightning Bol`t 
        > * `Ender Crystal`       ->  `End Crystal  ` 
        > * `Eye of Ender Signal` ->  `Eye of Ender ` 
        > * `Evocation Fangs`     ->  `Evoker Fangs ` 
        - [ ] - Item frames
            > Item frames can now be put on floors and ceilings.
        - [ ] - Paintings
            > Paintings now use a resource location for their motive.
- [ ] - World generation
    - [ ] - General
        - [ ] - Rewrote the world generation system.
        - [ ] - In the Nether, vertical air cavities — stretching from bedrock level to as far as Y=35, and filled with lava from bedrock level to Y=10 – now occur in chains across the bottom of the Nether, often forming extensive ravines.
        - [ ] - In newly generated chunks, the player is less likely to find frozen oceans next to warm oceans, etc.
    - [ ] - Biomes
        - [ ] - The `F3` menu now shows the biome ID, rather than its name.
        - [ ] - Biome names are now translatable.
        - [ ] - Updated some biome names:
            > * `Cold Beach`            ->    `Snowy Beach`
            > * `DesertHills`  	        ->    `Desert Hills`
            > * `Extreme Hills`	        ->    `Mountains`
            > * `Extreme Hills+`	    ->    `Wooded Mountains`
            > * `ForestHills` 	        ->    `Wooded Hills`
            > * `FrozenOcean` 	        ->    `Frozen Ocean`
            > * `FrozenRiver` 	        ->    `Frozen River`
            > * `Hell`        	        ->    `Nether`
            > * `Ice Plains`  	        ->    `Snowy Tundra`
            > * `Ice Mountains` 	    ->    `Snowy Mountains`
            > * `JungleEdge`  	        ->    `Jungle Edge`
            > * `JungleHills` 	        ->    `Jungle Hills`
            > * `Mesa`        	        ->    `Badlands`
            > * `Mesa Plateau F`	    ->    `Wooded Badlands Plateau`
            > * `Mesa Plateau`	        ->    `Badlands Plateau`
            > * `MushroomIsland`	    ->    `Mushroom Fields`
            > * `MushroomIslandShore` 	->    `Mushroom Field Shore`
            > * `Birch Forest M`	    ->    `Tall Birch Forest`
            > * `Birch Forest Hills M`	->    `Tall Birch Hills`
            > * `Desert M`	            ->    `Desert Lakes`
            > * `Extreme Hills M`	    ->    `Gravelly Mountains`
            > * `Extreme Hills+ M`	    ->    `Gravelly Mountains+`
            > * `Ice Plains Spikes`	    ->    `Ice Spikes`
            > * `Jungle M`	            ->    `Modified Jungle`
            > * `JungleEdge M`	        ->    `Modified Jungle Edge`
            > * `Mesa (Bryce)`	        ->    `Eroded Badlands`
            > * `Mesa Plateau M`	    ->    `Modified Badlands Plateau`
            > * `Mesa Plateau F M`	    ->    `Modified Wooded Badlands Plateau`
            > * `Mega Spruce Taiga`	    ->    `Giant Spruce Taiga`
            > * `Redwood Taiga Hills M`	->    `Giant Spruce Taiga Hills`
            > * `Roofed Forest M`	    ->    `Dark Forest Hills`
            > * `Savanna M`	            ->    `Shattered Savanna`
            > * `Savanna Plateau M`	    ->    `Shattered Savanna Plateau`
            > * `Swampland M`	        ->    `Swamp Hills`
            > * `Taiga M`	            ->    `Taiga Mountains`
            > * `Cold Taiga M`	        ->    `Snowy Taiga Mountains`
            > * `Mega Taiga`	        ->    `Giant Tree Taiga`
            > * `Mega Taiga Hills`      ->    `Giant Tree Taiga Hills`
            > * `Roofed Forest`	        ->    `Dark Forest`
            > * `Extreme Hills Edge`	->    `Mountain Edge`
            > * `Stone Beach`	        ->    `Stone Shore`
            > * `Swampland`	            ->    `Swamp`
            > * `Cold Taiga`	        ->    `Snowy Taiga`
            > * `Cold Taiga Hills`	    ->    `Snowy Taiga Hills`
            > * `TaigaHills`	        ->    `Taiga Hills`
    - [ ] - [Customized world type](https://minecraft.fandom.com/wiki/Old_Customized)
        > Removed.
    - [ ] - Trees
        > Large spruce trees now transform nearby grass blocks into podzol when they grow.
    - [ ] - Witch Huts
        > Now generate with a mushroom in the flower pot.
        >> Previously, the flower pot was completely empty.
- [ ] - Gameplay
    - [ ] - Movement
        > Pressing the jump button in flowing water at `level=1`, `level=2` and `level=3` will now do normal jumps instead of swimming up.
    - [ ] - Oxygen bar
        > The player's oxygen bar no longer regenerates instantly when they get out of water.
    - [ ] - Sleep
        > Players in creative mode can now sleep even if monsters are nearby.
    - [ ] - Visibility
        > Changed natural water visibility.
        >> - [ ] - The longer a player stays underwater, the better they will be able to see.
        >> - [ ] - Water is darker at lower depths.
        >> - [ ] - The Water Breathing potion & Respiration enchantment no longer grant enhanced vision underwater.
        >> - [ ] - Every ocean biome has a unique water color, and the swamp water color has been changed.
        >> - [ ] - Visibility changes per biome.
- [ ] - Command format
    - [ ] - Added commands to the profiler ([`/debug`](https://minecraft.fandom.com/wiki/Commands/debug)).
    - [ ] - [Functions](https://minecraft.fandom.com/wiki/Function)
        > Functions are now completely parsed and cached on load.
        >> This means if a command is incorrect for any reason, the player will know about it on load.
    - [ ] - `/difficulty`
        > Players can now query for the current difficulty by using `/difficulty` without any arguments.
    - [ ] - `/locate`
        - [ ] - The y-coordinate is now returned as `~` instead of `?`.
        - [ ] - Now accepts different structure names for all structures previously grouped under `Temple`: `Desert_Pyramid`, `Igloo`, `Jungle_Pyramid`, and `Swamp_Hut`.
    - [ ] - `/playsound`
        > Will `Tab ↹` auto-complete custom sound events.
    - [ ] - `/seed`
        > The output can now be copied.
    - [ ] - `/weather`
        > If a time isn't specified, it now defaults to 5 minutes (previously random).
- [ ] - General
    - [ ] - Advancements
        > Advancement descriptions now have colors:
        >> * Normal and goal advancements have green descriptions.
        >> * Challenge advancements have purple descriptions.
    - [ ] - Music
        > Three brand new pieces of underwater music by C418 have been added:
        >> * Shuniji, Dragon Fish and Axolotl.
        >> * Their ID for commands is `music.under_water`.
    - [ ] - Options
        > Removed 3D Anaglyph completely.
    - [ ] - Particles
        > Dripping
        >> * Changed which blocks show dripping liquids.
        >> * Drip particles are now generated by waterlogged blocks where appropriate.
        >> * Drip particles now snap to the hitbox of the block they appear on.
        >> * Added a new block tag to prevent all solid glass blocks from showing dripping liquid particles. See Tags.
    - [ ] - Recipes
        - [ ] - Custom recipes can now be loaded from data packs in `data/(namespace)/recipes/(name).json`
        - [ ] - Added a recipe book for the furnace.
        - [ ] - Furnace recipes have been moved to JSON files.
            > * They use `"type": "smelting"`.
            > * `cookingtime` is used to determine the time it should take to smelt an item in the furnace.
            > * `experience` is used to determine the amount of experience a player should get when picking the resulting item out of the furnace manually.
            > * Fuel is not included and is still hardcoded.
    - [ ] - Statistics
        > Statistics are being updated.
        >> * `stat.(stat)` is now `minecraft.custom:minecraft.(stat)`.
        >> * `stat.(stat).minecraft.(block/item/entity ID)` is now `minecraft.(stat):minecraft.(block/item/entity ID)`.
    - [ ] - Tooltips
        - [ ] - Added (and fixed) rarity values for certain items. Items with a rarity value will have their hotbar tooltips, which are displayed when scrolling over them in the hotbar, displayed as their respective colors when highlighted, rather than just being white.
        > * `Beacon`                         	 -> `Rare`
        > * `Chain Command Block`	             -> `Epic`
        > * `Command Block`	                     -> `Epic`
        > * `Conduit`	                         -> `Rare`
        > * `Creeper Head`	                     -> `Uncommon`
        > * `Dragon Egg`	                     -> `Epic`
        > * `Dragon Head`	                     -> `Uncommon`
        > * `Dragon's Breath`	                 -> `Uncommon`
        > * `Elytra`	                         -> `Uncommon`
        > * `Enchanted Golden Apple`	         -> `Epic`
        > * `Enchanted Books`	                 -> `Uncommon`
        > * `Enchanted Items (excluding books)`  -> `Rare`
        > * `End Crystal`	                     -> `Rare`
        > * `Bottle o' Enchanting`	             -> `Uncommon`
        > * `Golden Apple`	                     -> `Rare`
        > * `Heart of the Sea`	                 -> `Uncommon`
        > * `Music Discs`	                     -> `Rare`
        > * `Nether Star`	                     -> `Uncommon`
        > * `Player Head`	                     -> `Uncommon`
        > * `Repeating Command Block`	         -> `Epic`
        > * `Skeleton Skull`	                 -> `Uncommon`
        > * `Structure Block`	                 -> `Epic`
        > * `Totem of Undying`	                 -> `Uncommon`
        > * `Wither Skeleton Skull`	             ->  `Uncommon`
        > * `Zombie Head`	                     -> `Uncommon`
        - [ ] - Additionally, attack speed and attack damage in tools' info are now `dark green`.
    - [ ] - Other.
        - [ ] -Loading or creating a world shows the percentages of the loading stages.
            > `Preparing spawn area` now shows as a loading stage.
        - [ ] - Crash reports now list what data packs are enabled.
        - [ ] - Data generators are now exposed, players can get a dump of all blocks/items/commands/etc from the game without opening it up.

## 1.13.1
### Additions





\
\
\
P.s. Changelog taken from [Minecraft Wiki](https://minecraft.fandom.com/wiki/Java_Edition_version_history "Java Edition version history")