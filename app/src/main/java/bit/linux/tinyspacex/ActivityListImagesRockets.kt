package bit.linux.tinyspacex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.imageview.ShapeableImageView
import org.json.JSONObject

class ActivityListImagesRockets : AppCompatActivity() {

    var rocketsImages : MutableList<String> = arrayListOf()
    lateinit var content : Content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_images_rockets)

        var listViewImages = findViewById<ListView>(R.id.listViewImages)
        var contentAdapter = ContentAdapter()
        listViewImages.adapter = contentAdapter

        val articleString = intent.getStringExtra(RocketContentActivity.EXTRA_ARTICLE)
        content = Content.fromJSon(JSONObject(articleString))

        for (element in content.imagesUrl) {
            rocketsImages.add(element)
        }

        title = content.rocketName


    }


    inner class ContentAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return rocketsImages.size
        }

        override fun getItem(position: Int): Any {
            return rocketsImages[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView = layoutInflater.inflate(R.layout.row_images, parent, false)

            var imageViewContent = rowView.findViewById<ShapeableImageView>(R.id.imageViewRocket)

            rocketsImages[position].let{
                Helpers.getImageUrl(it, imageViewContent)
            }

            return  rowView

        }

    }

}