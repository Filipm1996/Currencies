import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.currencies.data.db.Currency
import java.lang.Exception

class DataBaseHelper(context: Context, private val table_name : String ) :SQLiteOpenHelper(context , DATABASE_NAME, null, DATEBASE_VERSION ) {

    companion object {
        const val DATEBASE_VERSION = 2
        const val table1_name = "my_currencies"
        const val table2_name = "list"
        const val DATABASE_NAME = "CurrencyDatebase"
        const val CurrencyName = "CurrencyName"
        const val rate = "rate"
    }

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $table_name"

    override fun onCreate(db: SQLiteDatabase?) {
        val SQL_CREATE_ENTRIES1 = (
                "CREATE TABLE IF NOT EXISTS " +  table1_name + " (" +
                        CurrencyName + " TEXT, " +
                        rate + " TEXT" + ")")
        val SQL_CREATE_ENTRIES2 = (
                "CREATE TABLE IF NOT EXISTS " +  table2_name + " (" +
                        CurrencyName + " TEXT, " +
                        rate + " TEXT" + ")")
        db?.execSQL(SQL_CREATE_ENTRIES1)
        db?.execSQL(SQL_CREATE_ENTRIES2)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.version = oldVersion
    }
    fun insertActivity(currency: Currency): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CurrencyName, currency.name)
        contentValues.put(rate, currency.rate)
        val success = db.insert(table_name, null, contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllActivites():ArrayList<Currency>{
        val activityList : ArrayList<Currency> = ArrayList()
        val selectQuery = "SELECT * FROM $table_name"
        val db =this.readableDatabase

        val cursor : Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e : Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var CurrencyName : String
        var rate : String

        if(cursor.moveToFirst()){
            do{
                CurrencyName = cursor.getString(cursor.getColumnIndex("CurrencyName"))
                rate = cursor.getString(cursor.getColumnIndex("rate"))
                var currency = Currency(CurrencyName,rate)
                activityList.add(currency)
            } while(cursor.moveToNext())
        }
        return activityList
    }

    fun deleteActivity(currencyName : String) : Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CurrencyName,currencyName)
        val success = db.delete(table_name, "CurrencyName=?", arrayOf(currencyName))
        db.close()
        return success
    }
    fun clearDatabase() {
        val db = this.writableDatabase
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}