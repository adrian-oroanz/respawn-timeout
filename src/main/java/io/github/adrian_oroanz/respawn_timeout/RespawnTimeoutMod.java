package io.github.adrian_oroanz.respawn_timeout;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RespawnTimeoutMod implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("Respawn Timeout");


	@Override
	public void onInitialize (ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
	}
	
}
