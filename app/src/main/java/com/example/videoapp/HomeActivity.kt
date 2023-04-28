package com.example.videoapp

import VideoListAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.example.videoapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {


    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val videoListAdapter = VideoListAdapter(this, emptyList())
        binding.videoListView.adapter = videoListAdapter

        val query = "yellow+flowers"

        videoListAdapter.fetchVideos(this, query, object : VideoListAdapter.OnFetchVideosListener {
            override fun onFetchVideosSuccess(videos: List<Video>) {
                videoListAdapter.updateVideos(videos)
            }

            override fun onFetchVideosError(error: String) {
                Toast.makeText(this@HomeActivity, error, Toast.LENGTH_SHORT).show()
            }
        })
    }
}