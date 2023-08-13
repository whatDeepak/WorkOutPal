package com.vyarth.workoutpal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vyarth.workoutpal.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch


// TODO(Step 6 : Adding the History Screen Activity.)
class HistoryActivity : AppCompatActivity() {
    //Todo 7 create a binding for the layout
    private var binding: ActivityHistoryBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//Todo  8 inflate the layout
        binding = ActivityHistoryBinding.inflate(layoutInflater)
//Todo 9 bind the layout to this activity
        setContentView(binding?.root)

//TODO(Step 11 : Setting up the action bar in the History Screen Activity and
// adding a back arrow button and click event for it.)
// START
        setSupportActionBar(binding?.toolbarHistoryActivity)

        val actionbar = supportActionBar//actionbar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true) //set back button
            actionbar.title = "History" // Setting a title in the action bar.
        }

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        val dao = (application as WorkOutApp).db.historyDao()
        getAllCompletedDates(dao)
// END
    }

    /**
     * Function is used to get the list exercise completed dates.
     */
    private fun getAllCompletedDates(historyDao: HistoryDao) {
        lifecycleScope.launch {
            historyDao.fetchALlDates().collect { allCompletedDatesList->
                // TODO(Step 3 :here the dates which were printed in log.
                //  We will pass that list to the adapter class which we have created and bind it to the recycler view.)
                // START
                if (allCompletedDatesList.isNotEmpty()) {
                    // Here if the List size is greater then 0 we will display the item in the recycler view or else we will show the text view that no data is available.
                    binding?.tvHistory?.visibility = View.VISIBLE
                    binding?.rvHistory?.visibility = View.VISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.GONE

                    // Creates a vertical Layout Manager
                    binding?.rvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)

                    // History adapter is initialized and the list is passed in the param.
                    val dates = ArrayList<String>()
                    for (date in allCompletedDatesList){
                        dates.add(date.date)
                    }
                    val historyAdapter = HistoryAdapter(ArrayList(dates))

                    // Access the RecyclerView Adapter and load the data into it
                    binding?.rvHistory?.adapter = historyAdapter
                } else {
                    binding?.tvHistory?.visibility = View.GONE
                    binding?.rvHistory?.visibility = View.GONE
                    binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                }
                // END
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
//Todo 12 reset the binding to null to avoid memory leak
        binding = null
    }
}