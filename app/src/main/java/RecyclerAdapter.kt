import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.currencies.data.db.Currency
import com.example.currencies.R
import com.example.currencies.data.db.CurrencyDao
import com.example.currencies.data.db.CurrencyDataBase
import com.example.currencies.data.repositories.repository
import com.example.currencies.ui.currencyViewModel
import com.example.currencies.ui.currencyViewModelFactory
import kotlinx.coroutines.*


class RecyclerAdapter(
    private val dataSource: List<Currency>,
    private val mContext : Context,
    private var lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<RecyclerAdapter.ViewHolder> () {
    private lateinit var repository: repository

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var value: TextView = view.findViewById(R.id.value)
        var addButton : ImageButton = view.findViewById(R.id.addButton)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        repository = repository(mContext)
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun setAlertDialog(currency: Currency) {

                val buildier = AlertDialog.Builder(mContext)
                buildier.setMessage("Do you want to add ${currency.name}?")
                buildier.setCancelable(true)
                buildier.setPositiveButton("yes") { dialog, _ ->

                    repository.getMyAllCurrencies().observeOnce(lifecycleOwner, Observer {
                        if (it!=null){
                    val myList = it
                    if (myList.contains(currency)) {

                        Toast.makeText(
                            mContext,
                            "${currency.name} is in favourites",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            repository.insertMyCurrency(currency)
                        }
                        Toast.makeText(
                            mContext,
                            "Added ${currency.name}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                        }})
                }
                buildier.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                val alert = buildier.create()
                alert.show()

    }
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}