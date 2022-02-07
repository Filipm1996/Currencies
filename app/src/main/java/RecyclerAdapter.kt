import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.currencies.RoomDataBase.Currency
import com.example.currencies.R
import com.example.currencies.RoomDataBase.CurrencyDao
import com.example.currencies.RoomDataBase.CurrencyDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecyclerAdapter(
    private val dataSource: List<Currency>,
    private val mContext : Context
): RecyclerView.Adapter<RecyclerAdapter.ViewHolder> (){
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var value: TextView = view.findViewById(R.id.value)
        var addButton : ImageButton = view.findViewById(R.id.addButton)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_element, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = dataSource[position]
        holder.title.text = currency.name
        holder.value.text = currency.rate
        holder.addButton.setOnClickListener{
            setAlertDialog(currency)
        }
    }

    private fun setAlertDialog(currency: Currency) {
        GlobalScope.launch(Dispatchers.Main) {
            val db =
                Room.databaseBuilder(mContext, CurrencyDataBase::class.java, "Mycurrency_database")
                    .fallbackToDestructiveMigration().build()
            val dao = db.currencyDao()
            val buildier = AlertDialog.Builder(mContext)
            buildier.setMessage("Do you want to add ${currency.name}?")
            buildier.setCancelable(true)
            buildier.setPositiveButton("yes") { dialog, _ ->
                GlobalScope.launch(Dispatchers.IO) {
                    val isIn = checkIfIsIn(dao, currency)
                    if (!isIn) {
                        dao.insertCurrency(currency)
                        db.close()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(mContext, "Added ${currency.name}", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                mContext,
                                "com.example.currencies.RoomDataBase.Currency is in favourites",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
            buildier.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val alert = buildier.create()
            alert.show()
        }
    }

    private suspend fun checkIfIsIn(dao : CurrencyDao, currency: Currency) : Boolean {
            val listOfCurrencies = dao.getAllCurrencies()
            var bool : Boolean = false
        for (i in listOfCurrencies){
            bool = i.name == currency.name
        }
        return bool
    }

}