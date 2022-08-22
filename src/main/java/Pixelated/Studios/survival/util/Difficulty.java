package Pixelated.Studios.survival.util;

import org.bukkit.Chunk;
import org.bukkit.World;

/**
 * Utility class for getting local difficulties
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Difficulty {

	/** Get the regional difficulty of a chunk
	 * <p><b>Note:</b> This is not 100% accurate, still needs more work.</p>
	 * @param chunk Chunk to get regional difficulty from
	 * @return Regional difficulty of chunk
	 */
	public static float getRegionalDifficulty(Chunk chunk) {
		org.bukkit.Difficulty difficulty = chunk.getWorld().getDifficulty();
		long worldTime = chunk.getWorld().getFullTime();
		long inhabitedTime = chunk.getInhabitedTime();
		float moonPhase = getMoonPhase(chunk.getWorld());

		if (difficulty == org.bukkit.Difficulty.PEACEFUL) {
			return 0.0F;
		} else {
			boolean hard = difficulty == org.bukkit.Difficulty.HARD;
			float f1 = 0.75F;
			// TODO: Investigate warning
			float f2 = Math.clamp(((float) worldTime + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;

			f1 += f2;
			float f3 = 0.0F;

			f3 += Math.clamp((float) inhabitedTime / 3600000.0F, 0.0F, 1.0F) * (hard ? 1.0F : 0.75F);
			f3 += Math.clamp(moonPhase * 0.25F, 0.0F, f2);
			if (difficulty == org.bukkit.Difficulty.EASY) {
				f3 *= 0.5F;
			}

			f1 += f3;
			return round((float) difficultyNum(difficulty) * f1, 2);
		}
	}

	public static int difficultyNum(org.bukkit.Difficulty difficulty) {
		// TODO: Replace switch with enhanced switch
		switch (difficulty) {
			case PEACEFUL:
				return 0;
			case EASY:
				return 1;
			case NORMAL:
				return 2;
			case HARD:
				return 3;
			default:
				throw new IllegalStateException("Unexpected value: " + difficulty);
		}
	}

	/** Get the clamped difficulty of a chunk
	 * @param chunk Chunk to get clamped difficulty from
	 * @return Clamped difficulty of chunk
	 */
	public static double getClampedDifficulty(Chunk chunk) {
		double regionalDifficulty = getRegionalDifficulty(chunk);
		double value;

		if ( regionalDifficulty < 2.0 ) {
			value = 0.0;
		} else if ( regionalDifficulty > 4.0 ) {
			value = 1.0;
		} else {
			value = ( regionalDifficulty - 2.0 ) / 2.0;
		}
		return round(value, 2);
	}

	// TODO: Investigate warning
	private static float round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (float) tmp / factor;
	}

	/** Get the moon phase of a world
	 * @param world World to get moon phase from
	 * @return Moon phase of world
	 */
	public static float getMoonPhase(World world) {
		long time = world.getFullTime();
		int phase = (int)(time / 24000L % 8L + 8L) % 8;
		// TODO: replace switch with enhanced switch
		switch (phase) {
			case 1:
			case 7:
				return 0.75f;
			case 2:
			case 6:
				return  0.5f;
			case 3:
			case 5:
				return 0.25f;
			case 4:
				return 0.0f;
			default:
				return 1;
		}
	}

}
