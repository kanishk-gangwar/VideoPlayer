import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.videoapp.R
import com.example.videoapp.Video
import org.json.JSONObject


class VideoListAdapter(private val context: Context, private var videos: List<Video>) : BaseAdapter() {

    override fun getCount(): Int {
        return videos.size
    }

    override fun getItem(position: Int): Any {
        return videos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val video = videos[position]

        // Inflate the layout for the video item
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.video_item, parent, false)

        // Set the title of the video
        val titleTextView = view.findViewById<TextView>(R.id.title_text_view)
        titleTextView.text = video.title

        // Set up the ExoPlayer instance to play the video
        val videoView = view.findViewById<VideoView>(R.id.video_view)
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(Uri.parse(video.url))
        videoView.requestFocus()

        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.setOnVideoSizeChangedListener { mediaPlayer, width, height ->
                videoView.setMediaController(mediaController)
                mediaController.setAnchorView(videoView)
            }
            videoView.start()
        }

        return view
    }


        private val API_KEY = "Your API KEY"

    //API KEY 35866324-8715c6d870e15be8a9f290a8a

        fun fetchVideos(context: Context, query: String, listener: OnFetchVideosListener) {
            val url = "https://pixabay.com/api/videos/?key=$API_KEY&q=$query"
            val request = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val videos = mutableListOf<Video>()
                    val hits = response.getJSONArray("hits")
                    for (i in 0 until hits.length()) {
                        val hit = hits.getJSONObject(i)
                        val videoUrl = hit.getString("videos")
                        val videoObject = JSONObject(videoUrl)
                        val videoUrlString = videoObject.getJSONObject("medium").getString("url")
                        val videoTitle = hit.getString("tags")
                        videos.add(Video(videoTitle, videoUrlString))
                    }
                    listener.onFetchVideosSuccess(videos)
                },
                { error ->
                    listener.onFetchVideosError(error.localizedMessage)
                }
            )
            Volley.newRequestQueue(context).add(request)
        }

    fun updateVideos(videos: List<Video>) {
        this.videos = videos
        notifyDataSetChanged()
    }


    interface OnFetchVideosListener {
        fun onFetchVideosSuccess(videos: List<Video>)
        fun onFetchVideosError(error: String)
    }
}

