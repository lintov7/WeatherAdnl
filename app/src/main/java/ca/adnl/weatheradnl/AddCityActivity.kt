package ca.adnl.weatheradnl

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ca.adnl.weatheradnl.databinding.ActivityAddCityBinding
import ca.adnl.weatheradnl.models.City


class AddCityActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddCityBinding
    lateinit var dbHelper: DBHelper
    var city: City? = null

    companion object {
        const val CITY_PAYLOAD = "CITY_PAYLOAD"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCityBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "City Details"
        setContentView(binding.root)
        city = intent.getSerializableExtra(CITY_PAYLOAD) as City?
        if(city != null){
            binding.etCity.setText(city!!.name)
            binding.etDescription.setText(city!!.description)
        }
        dbHelper = DBHelper(this)
        binding.btnSave.setOnClickListener {
            val cityName = binding.etCity.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()
            if(cityName.isEmpty()){
                Toast.makeText(this, "Please enter city", Toast.LENGTH_SHORT).show()
            }
            if(description.isEmpty()){
                Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show()
            }
            if(city == null){
                city = City(id = -1, cityName, description)
                dbHelper.insertCity(city!!)
                Toast.makeText(this, "City inserted successfully", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }else{
                city!!.name  = cityName
                city!!.description  = description
                dbHelper.updateCity(city!!)
                Toast.makeText(this, "City updated successfully", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(city != null){
            menu?.add(0,0,0,"Delete")?.setIcon(R.drawable.ic_delete_24dp)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            0 -> {
                AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete ${city!!.name}?") // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes
                    ) { dialog, _ ->
                        dbHelper.deleteCity(city!!)
                        dialog.dismiss()
                        Toast.makeText(this, "City deleted successfully", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .show()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}