package name.heqian.cs528.googlefit.database;

/**
 * Created by pluxsuwong on 4/5/16.
 */
public class ActivityDbSchema {
    public static final class ActivityTable {
        public static final String NAME = "activities";

        public static final class Cols {
            public static final String STARTTIME = "starttime";
            public static final String ACTIVITYTYPE = "activitytype";
        }
    }
}
