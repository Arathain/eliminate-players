package arathain.amogus;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.UUID;

public class EliminatePlayers implements ModInitializer {
	public static final ArrayList<UUID> bannedUuids = new ArrayList<>();

	@Override
	public void onInitialize() {
		bannedUuids.add(UUID.fromString("1ece513b-8d36-4f04-9be2-f341aa8c9ee2"));
	}
}
