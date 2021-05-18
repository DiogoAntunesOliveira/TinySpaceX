package bit.linux.tinyspacex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import org.json.JSONObject

class RocketContentActivity : AppCompatActivity() {

    lateinit var content : Content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocket_content)

        val articleString = intent.getStringExtra(EXTRA_ARTICLE)
        content = Content.fromJSon(JSONObject(articleString))

        title = content.rocketName

        findViewById<TextView>(R.id.textViewTitle).text = content.rocketName.toString()
        findViewById<TextView>(R.id.textViewDescription).text = content.description.toString()
        findViewById<TextView>(R.id.textViewCostPerLaunch2).text = content.costPerLaunch.toString() + "€"
        findViewById<TextView>(R.id.textViewStages2).text = content.rocketName.toString()


        val imageViewContent = findViewById<ImageView>(R.id.imageView)

        content.imageFront?.let{
            Helpers.getImageUrl(it, imageViewContent)
        }

        findViewById<ImageView>(R.id.imageView).setOnClickListener{

            val intent = Intent(this,ActivityListImagesRockets::class.java)
            intent.putExtra(RocketContentActivity.EXTRA_ARTICLE, content.toJson().toString())
            startActivity(intent)

        }


    }

    companion object{
        val EXTRA_ARTICLE = "article"
    }
}