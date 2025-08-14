# HopperLimitUpgrades
## Description
* HopperLimitUpgrades allows the upgrading of hopper limits for BentoBox Islands and the Limits addon.
## Features
* Allows the purchasing of additional offsets for hopper limits through the BentoBox limits addon.
## Required Dependencies
* SkyLib
* Vault
## Optional Dependencies
* BentoBox
* Limits Addon
* SkyLib
* Vault
## Commands
- /hopperlimitupgrades - The base command and opens the upgrade GUI.
  - Aliases: `/upgrade`, `/upgrades`, `/hopperupgrade` and `/hopperupgrades`.
- /hopperlimitupgrades reload - Command to reload the plugin
- /hopperlimitupgrades help - View the plugin's help message
- /hopperlimitupgrades reset <player_name> - Reset the player's island's hopper limit offset to 0.
- /hopperlimitupgrades set <player_name> <amount> - Set the player's island's hopper limit offset.
- /hopperlimitupgrades add <player_name> <amount> - Add to the player's island's hopper limit offset.
- /hopperlimitupgrades remove <player_name> <amount> - Remove from the player's island's hopper limit offset.
- /hopperlimitupgrades get <player_name> - Get the player's island's hopper limit offset.
## Permisisons
- `hopperlimitupgrades.commands.hopperlimitupgrades` - The permission to access the /hopperlimitupgrades command.
- `hopperlimitupgrades.commands.hopperlimitupgrades.reload` - The permission to access /hopperlimitupgrades reload.
- `hopperlimitupgrades.commands.hopperlimitupgrades.help` - The permission to access /hopperlimitupgrades help.
- `hopperlimitupgrades.commands.hopperlimitupgrades.reset` - The permission to access /hopperlimitupgrades reset.
- `hopperlimitupgrades.commands.hopperlimitupgrades.set` - The permission to access /hopperlimitupgrades set.
- `hopperlimitupgrades.commands.hopperlimitupgrades.add` - The permission to access /hopperlimitupgrades add.
- `hopperlimitupgrades.commands.hopperlimitupgrades,remove` - The permission to access /hopperlimitupgrades remove.
- `hopperlimitupgrades.commands.hopperlimitupgrades.get` - The permission to access /hopperlimitupgrades get.
## Issues, Bugs, or Suggestions
* Please create a new [Github Issue](https://github.com/lukesky19/HopperLimitUpgrades/issues) with your issue, bug, or suggestion.
* If an issue or bug, please post any relevant logs containing errors related to HopperLimitUpgrades and your configuration files.
* I will attempt to solve any issues or implement features to the best of my ability.
## FAQ
Q: What versions does this plugin support?

A: 1.21.4, 1.21.5, 1.21.6, 1.21.7, and 1.21.8.

Q: Are there any plans to support any other versions?

A: The plugin will always support the latest version of the game at the time.

Q: Does this work on Spigot and Paper?

A: This plugin only works with Paper, it makes use of many newer API features that don't exist in the Spigot API. There are no plans to support Spigot.

Q: Is Folia supported?

A: There is no Folia support at this time. I may look into it in the future though.

## For Server Admins/Owners
* Download the plugin [SkyLib](https://github.com/lukesky19/SkyLib/releases).
* Download the plugin from the releases tab and add it to your server.

## Building
* Go to [SkyLib](https://github.com/lukesky19/SkyLib) and follow the "For Developers" instructions.
* Then run:
  ```./gradlew build```

## Why AGPL3?
I wanted a license that will keep my code open source. I believe in open source software and in-case this project goes unmaintained by me, I want it to live on through the work of others. And I want that work to remain open source to prevent a time when a fork can never be continued (i.e., closed-sourced and abandoned).
