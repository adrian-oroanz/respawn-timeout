# Respawn Timeout

An open-source Minecraft mod, written for Quilt. Prevents players from instantly respawning by setting a configurable timeout upon death.

## Usage & Configuration

The mod is more or less an automation system. Players will automatically be assigned the configured timeout upon death, and will automatically "respawn" once the timeout has expired.

The following commands can be used to configure the mod's timeout system:

- Run the `/respawntimeout set` command to configure the timeout, up to 7 days. Valid time units are seconds, minutes, hours and days. If set to 0, the timeout will be disabled. *
- Run the `/respawntimeout get` command to get the current timeout configuration. *
- Run the `/respawntimeout clear` command to set the timeout to 0, effectively disabling it. *

The following commands can be used to interact with the mod:

- Run the `/respawntimeout respawn` command to either attempt to revive oneself (if the timeout has expired) or forcefully respawn another player (providing the username of a player). **

\* Requires a permission level of administrator to run.

\** Requires a permission level of moderator to force-respawn players.

_NOTE: To avoid running an unnecessarily amount of checks every game tick, the mod will only check the status of players upon joining the world or performing the `/respawntimeout respawn` command._

## Contributing

**⚠️ Since this mod was made for a very specific case-scenario, contributions might not be accepted if they change or modify the core idea of the mod. ⚠️**

### Feedback and Bug Reporting

To streamline this process, check that your request or problem hasn't already been reported and is active. Otherwise, feel free to open a new Issue over on the **[Issues]((https://github.com/adrian-oroanz/respawn-timeout/issues))** page.

If you want to contribute directly to the mod instead, check the following sections.

### Getting Started

1. Clone the repository.
2. Select the branch in which to commit your changes.
3. Commit your changes and push to the branch.
4. Create a new Pull Request.

I believe the code is pretty straightforward and simple to understand, it really isn't anything too complex or groundbreaking and should be easy to contribute any features.

If you have any doubts as to how something works feel free to contact me.

### Translations

The most simple, but still helpful, way of contributing is via language translations. The mod only gets official support for 2 languages: English and Spanish, any other language is welcome.

To contribute, simply open a new PR with the added language file and titled after the language to include.

## License

This project is licensed under the MIT LICENSE - see the [LICENSE](LICENSE) file for details.
