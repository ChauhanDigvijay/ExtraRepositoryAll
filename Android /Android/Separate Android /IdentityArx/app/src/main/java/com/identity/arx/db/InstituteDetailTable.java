package com.identity.arx.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.identity.arx.objectclass.InstituteVo;

import java.util.ArrayList;
import java.util.List;

public class InstituteDetailTable extends IdentityAxsOpenHelper {
    SQLiteDatabase db = getWritableDatabase();

    public InstituteDetailTable(Context mContext) {
        super(mContext);
    }

    public long addInstitute_Details(InstituteVo UserVO) {
        long lg = 0;
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(IdentityAxsOpenHelper.KEY_INST_DB, UserVO.getDbName());
            contentValues.put(IdentityAxsOpenHelper.KEY_INSTITUTE_NAME, UserVO.getInstituteName());
            if (isInstituteAvailable(UserVO.getDbName())) {
                lg = this.db.insert(IdentityAxsOpenHelper.INSTITUTE_TABLE, null, contentValues);
            }
            return lg;
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean isInstituteAvailable(String intId) {
        if (this.db.query(IdentityAxsOpenHelper.INSTITUTE_TABLE, null, "inst_db =?", new String[]{intId}, null, null, null).getCount() > 0) {
            return false;
        }
        return true;
    }

    public List<InstituteVo> getInstituteDetails() {
        List<InstituteVo> info = new ArrayList();
        Cursor cursor = this.db.query(IdentityAxsOpenHelper.INSTITUTE_TABLE, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                InstituteVo InstituteVo = new InstituteVo();
                InstituteVo.setDbName(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_INST_DB)));
                InstituteVo.setInstituteName(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_INSTITUTE_NAME)));
                info.add(InstituteVo);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return info;
    }
}
