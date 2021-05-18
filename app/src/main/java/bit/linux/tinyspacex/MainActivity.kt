package bit.linux.tinyspacex

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.shape.CornerFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    var rockets : MutableList<Content> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rocketListView = findViewById<ListView>(R.id.RocketsListView)
        var contentAdapter = ContentAdapter()
        rocketListView.adapter = contentAdapter
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        
        // coreRoutine
        GlobalScope.launch(Dispatchers.IO){
            val urlConnection : HttpURLConnection =
                URL("https://api.spacexdata.com/v3/rockets").openConnection()
                        as HttpURLConnection
            urlConnection.setRequestProperty("User-Agent", "Test")
            urlConnection.setRequestProperty("Connection", "close")
            urlConnection.connectTimeout = 1500

            urlConnection.connect()

            val stream = urlConnection.inputStream
            val isReader = InputStreamReader(stream)
            val brin = BufferedReader(isReader)

            var str : String = ""

            var keepReading = true
            while (keepReading){
                val line = brin.readLine()

                if(line == null){
                    keepReading = false
                }else{
                    str += line
                }
            }

            // Request ao json
            val jsonObject = JSONArray(str)

                for (index in 0 until jsonObject.length()){
                    val jsonRocket = jsonObject.get(index) as JSONObject
                    val content = Content.fromJSon(jsonRocket)
                    rockets.add(content)
                }

            GlobalScope.launch(Dispatchers.Main){
                contentAdapter.notifyDataSetChanged()
            }
        }

    }

    inner class ContentAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return rockets.size
        }

        override fun getItem(position: Int): Any {
            return rockets[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView = layoutInflater.inflate(R.layout.row_content, parent, false)
            
            val textViewRocketName = rowView.findViewById<TextView>(R.id.textViewRocketName)
            val textViewCostPerLaunch = rowView.findViewById<TextView>(R.id.textViewCostPerLaunch)
            val textViewCountry = rowView.findViewById<TextView>(R.id.textViewCountry)
            val textViewStages = rowView.findViewById<TextView>(R.id.textViewStages)
            val imageViewRocket = rowView.findViewById<ImageView>(R.id.RocketImageView)
            
            textViewRocketName.text = rockets[position].rocketName
            textViewCostPerLaunch.text =  rockets[position].costPerLaunch.toString() + "€"
            textViewStages.text = rockets[position].stages
            textViewCountry.text = rockets[position].country


            rockets[position].imageFront?.let{
                Helpers.getImageUrl(it, imageViewRocket)
            }

            rowView.setOnClickListener {
                Toast.makeText(this@MainActivity, rockets[position].rocketName, Toast.LENGTH_LONG).show()

                val intent = Intent(this@MainActivity, RocketContentActivity::class.java)
                intent.putExtra(RocketContentActivity.EXTRA_ARTICLE, rockets[position].toJson().toString())
                startActivity(intent)
            }
            
            return  rowView
            
        }

    }
}