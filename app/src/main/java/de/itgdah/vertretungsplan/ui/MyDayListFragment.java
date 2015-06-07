package de.itgdah.vertretungsplan.ui;

import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import de.itgdah.vertretungsplan.data.VertretungsplanContract;

/**
 * Created by moritz on 26.05.15.
 */
public class MyDayListFragment extends BaseDayListFragment {
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        SharedPreferences preferences = getActivity().getSharedPreferences
                (BaseActivity.SHARED_PREFERENCES_FILENAME, Context
                        .MODE_PRIVATE);
        String classesOfUser = preferences.getString(MyDataActivity
                .CLASS_OF_USER, "");
        String subjectsOfUser = preferences.getString(MyDataActivity
                .SUBJECTS_OF_USER, "");
        // necessary because preferences.getString() might throw type exception
        if (classesOfUser != null && subjectsOfUser != null) {
            if (id == BaseDayListFragment.VERTRETUNGEN_LOADER_ID && !classesOfUser
                    .isEmpty() && !subjectsOfUser.isEmpty()) {
                    String[] subjectsArray = subjectsOfUser.split("\\s*,\\s*");
                    String[] classesArray = classesOfUser.split("\\s*,\\s*");
                    String inClauseSubjectsContent = "";
                    String inClauseClassesContent = "";
                    for (int i = 0; i < subjectsArray.length; i++) {
                        if (i > 0) {
                            inClauseSubjectsContent += ",";
                        }
                        inClauseSubjectsContent += "? ";
                    }

                    for (int i = 0; i < classesArray.length; i++) {
                        if (i > 0) {
                            inClauseClassesContent += ",";
                        }
                        inClauseClassesContent += "? ";
                    }

                    mCustomSelection = VertretungsplanContract.Vertretungen
                            .COLUMN_CLASS + " IN (" + inClauseClassesContent + ")"
                            + " AND " + VertretungsplanContract
                            .Vertretungen.COLUMN_SUBJECT + " IN ( " +
                            inClauseSubjectsContent + " ) ";
                    mCustomSelection += " AND " + mSelection;

                    mCustomSelectionArgs = new String[1 + subjectsArray.length +
                            classesArray.length];
                    // N classes, N subjects, day_id
                    for (int i = 0; i < classesArray.length; i++) {
                        mCustomSelectionArgs[i] = classesArray[i];
                    }
                    for (int i = 0; i < subjectsArray.length; i++) {
                        mCustomSelectionArgs[classesArray.length + i] =
                                subjectsArray[i];
                    }
            }
        }
        return super.onCreateLoader(id, args);
    }
}
