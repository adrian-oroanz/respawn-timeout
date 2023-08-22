# Changelog

## [0.1.0](#010)
### Additions
- Added persistent server state to keep track of data.
- Added the `/respawntimeout set` command, allowing administrator level users to configure the timeout.
- Added the `/respawntimeout get` command, allowing administrator level users to query the timeout configuration.
- Added the `/respawntimeout clear` command, allowing administrator level users to disable the timeout.
- Added the `/respawntimeout respawn` command, allowing players to attempt to respawn once their timeout is over.

## [0.1.1](#011)
### Changes
- Updated the `/respawntimeout set` command, adding the time units: `seconds`, `minutes`, `hours` and `days`, with their corresponding conversions; to the timeout argument for a simpler usage.
### Fixes
- Fixed the `/respawntimeout set` command processing negative timeout.

## [0.2.0](#020)
### Additions
- Added support for multiple languages. Official support for English and Spanish is included.
- Added a license to the mod, see the [LICENSE](LICENSE) file for more details.
### Changes
- Updated the `/respawntimeout respawn` command, allowing moderator level users to force-respawn a player.
### Fixes
- Fixed the `/respawntimeout set` command not limiting the right amount of timeout per time unit.
