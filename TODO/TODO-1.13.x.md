# 1.13.x
## 1.13
- [ ] - Blocks
    - [ ] - Blue ice
    - [ ] - Bubble columns (magma and soul sand underwater)
    - [ ] - Buttons, pressure plates and trapdoors (from all wood)
    - [ ] - Normal pumpkin
    - [ ] - Conduits
    - [ ] - Corals, coral fans, coral blocks + dead versions
    - [ ] - Dried kelp blocks
    - [ ] - Kelp
    - [ ] - Prismarine stairs and slabs
    - [ ] - Seagrass
    - [ ] - Stripped logs and wood
    - [ ] - Turtle eggs
- [ ] - Items
    - [ ] - Arrow and potions of Slow Falling and Turtle Master
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
    - [ ] - Wood blocks
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


P.s. Changelog taken from [Minecraft Wiki](https://minecraft.fandom.com/wiki/Java_Edition_version_history "Java Edition version history")