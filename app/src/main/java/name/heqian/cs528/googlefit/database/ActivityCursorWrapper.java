package name.heqian.cs528.googlefit.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import java.util.Date;
import name.heqian.cs528.googlefit.database.ActivityDbSchema.ActivityTable;

/**
 * Created by pluxsuwong on 4/5/16.
 */
public class ActivityCursorWrapper extends CursorWrapper {
    public ActivityCursorWrapper(Cursor cursor) {
        super(cursor);
    }
}
