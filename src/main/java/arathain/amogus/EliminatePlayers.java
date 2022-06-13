package arathain.amogus;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import java.util.ArrayList;
import java.util.UUID;

public class EliminatePlayers implements ModInitializer {
	public static final ArrayList<UUID> bannedUuids = new ArrayList<>();

	@Override
	public void onInitialize(ModContainer mod) {
		bannedUuids.add(UUID.fromString("1ece513b-8d36-4f04-9be2-f341aa8c9ee2"));
	}
}
