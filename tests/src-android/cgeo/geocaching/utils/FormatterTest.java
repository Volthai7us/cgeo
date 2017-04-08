package cgeo.geocaching.utils;

import cgeo.geocaching.CgeoApplication;
import cgeo.geocaching.R;
import cgeo.geocaching.enumerations.WaypointType;
import cgeo.geocaching.models.Waypoint;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class FormatterTest extends TestCase {

    /**
     * The pattern we get from {@link Formatter#getShortDateFormat()} should be the one used by {@link Formatter#formatShortDate(long)}.
     */
    public static void testShortDateFormat() {
        final long currentTimeMillis = System.currentTimeMillis();
        final String formattedDate = Formatter.formatShortDate(currentTimeMillis);
        final String pattern = Formatter.getShortDateFormat();
        assertThat(new SimpleDateFormat(pattern, Locale.getDefault()).format(currentTimeMillis)).isEqualTo(formattedDate);
    }

    public static void testParkingWaypoint() {
        assertFormatting(new Waypoint("you can park here", WaypointType.PARKING, false), WaypointType.PARKING.getL10n());
    }

    public static void testOriginalWaypoint() {
        assertFormatting(new Waypoint("an original", WaypointType.ORIGINAL, false), WaypointType.ORIGINAL.getL10n());
    }

    public static void testOwnWaypoint() {
        final Waypoint own = new Waypoint("my own", WaypointType.OWN, true);
        own.setPrefix(Waypoint.PREFIX_OWN);
        assertFormatting(own, CgeoApplication.getInstance().getString(R.string.waypoint_custom));
    }

    private static void assertFormatting(final Waypoint waypoint, final String expected) {
        assertThat(Formatter.formatWaypointInfo(waypoint)).isEqualTo(expected);
    }

    @SuppressLint("SdCardPath")
    public static void testTruncateCommonSubdir() {
        final List<CharSequence> dirs = new ArrayList<>();
        dirs.add("/sdcard/Android/data/cgeo.geocaching/files");
        dirs.add("/storage/emulated/0/Android/data/cgeo.geocaching/files");
        dirs.add("/storage/extSdCard/Android/data/cgeo.geocaching/files");
        dirs.add("/mnt/sdcard/Android/data/cgeo.geocaching/files");
        dirs.add("/storage/sdcard1/Android/data/cgeo.geocaching/files");

        final List<CharSequence> truncated = Formatter.truncateCommonSubdir(dirs);
        assertThat(truncated.get(0)).isEqualTo("/sdcard/\u2026");
        assertThat(truncated.get(1)).isEqualTo("/storage/emulated/0/\u2026");
        assertThat(truncated.get(2)).isEqualTo("/storage/extSdCard/\u2026");
        assertThat(truncated.get(3)).isEqualTo("/mnt/sdcard/\u2026");
        assertThat(truncated.get(4)).isEqualTo("/storage/sdcard1/\u2026");
    }

    @SuppressLint("SdCardPath")
    public static void testTruncateCommonSubdirSingleEntry() {
        final List<CharSequence> dirs = new ArrayList<>();
        dirs.add("/data/data/cgeo.geocaching/files");

        final List<CharSequence> truncated = Formatter.truncateCommonSubdir(dirs);
        assertThat(truncated.get(0)).isEqualTo("/data/data/cgeo.geocaching/files");
    }

}
