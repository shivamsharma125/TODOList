package in.co.softwaresolution.list;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InfoOpenHelper extends SQLiteOpenHelper{

    public static final int VERSION=1;

    public InfoOpenHelper(Context context) {
        super(context, Contract.Info.TABLE_NAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String infosql="CREATE TABLE "+Contract.Info.TABLE_NAME+" ( "+
                                       Contract.Info.CLOUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+
                                       Contract.Info.COLUMN_TITLE+" TEXT , "+
                                       Contract.Info.COLUMN_DESCRIPTION+ " TEXT )";
        sqLiteDatabase.execSQL(infosql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
