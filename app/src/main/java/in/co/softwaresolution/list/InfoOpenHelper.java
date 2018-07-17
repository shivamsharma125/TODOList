package in.co.softwaresolution.list;

import android.content.Context;
import android.database.ContentObservable;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.PublicKey;

public class InfoOpenHelper extends SQLiteOpenHelper{

    public static final int VERSION=1;
    public static InfoOpenHelper instance;

    private InfoOpenHelper(Context context) {
        super(context, Contract.Info.TABLE_NAME,null, VERSION);

    }

    public static InfoOpenHelper getInstance(Context context)
    {
        if(instance == null)
        {
            instance=new InfoOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String infosql="CREATE TABLE "+Contract.Info.TABLE_NAME+" ( "+
                                       Contract.Info.CLOUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+
                                       Contract.Info.COLUMN_TITLE+" TEXT , "+
                                       Contract.Info.COLUMN_DESCRIPTION+" TEXT , "+
                                       Contract.Info.COLUMN_DAY+ " TEXT , "+
                                       Contract.Info.COLUMN_MONTH+ " TEXT , "+
                                       Contract.Info.COLUMN_YEAR+ " TEXT , "+
                                       Contract.Info.COLUMN_MIN+ " TEXT , "+
                                       Contract.Info.COLUMN_HOUR+ " TEXT , "+
                                       Contract.Info.COLUMN_SECOND+ " TEXT , "+
                                       Contract.Info.IS_ALARMED + " TEXT )";
        sqLiteDatabase.execSQL(infosql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
