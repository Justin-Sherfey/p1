package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.view.View
import android.widget.*


class SecondActivity : AppCompatActivity() {

    var simpleVideoView: VideoView? = null
    var mediaControls: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // button that will bring to third screen with recording
        val secondPageButton = findViewById<Button>(R.id.button2)
        secondPageButton.setOnClickListener {
            val intent = Intent(this@SecondActivity, ThirdActivity::class.java)
            startActivity(intent)
        }

        // gets value from drop down and saves in nameg
        val bundle: Bundle? = intent.extras
        val nameG: String? = bundle?.getString("gesture")

        // text value under button
        val tv = findViewById<TextView>(R.id.textView3)
        tv.text = nameG

        //plays video when play button pressed
        val playVideoButton = findViewById<Button>(R.id.button5)
        playVideoButton.setOnClickListener {

            var gestureVid = R.raw.fandown

            //parses input from dropdown menu to see which video to play
            when(nameG) {
                "Turn On Lights" -> gestureVid = R.raw.lighton
                "Turn Off Lights" -> gestureVid = R.raw.lightoff
                "Turn On Fan" -> gestureVid = R.raw.fanon
                "Turn Off Fan" -> gestureVid = R.raw.fanoff
                "Increase Fan Speed" -> gestureVid = R.raw.fanup
                "Decrease Fan Speed" -> gestureVid = R.raw.fandown
                "Set Thermostat to specified temperature" -> gestureVid = R.raw.setthermo
                "0" -> gestureVid = R.raw.h0
                "1" -> gestureVid = R.raw.h1
                "2" -> gestureVid = R.raw.h2
                "3" -> gestureVid = R.raw.h3
                "4" -> gestureVid = R.raw.h4
                "5" -> gestureVid = R.raw.h5
                "6" -> gestureVid = R.raw.h6
                "7" -> gestureVid = R.raw.h7
                "8" -> gestureVid = R.raw.h8
                "9" -> gestureVid = R.raw.h9
                else -> gestureVid = R.raw.fandown
            }

            //plays video example
            //https://www.geeksforgeeks.org/videoview-in-kotlin/
            simpleVideoView = findViewById<View>(R.id.videoView2) as VideoView
            if (mediaControls == null) {
                mediaControls = MediaController(this)
                mediaControls!!.setAnchorView(this.simpleVideoView)
            }
            simpleVideoView!!.setMediaController(mediaControls)
            simpleVideoView!!.setVideoURI(Uri.parse("android.resource://"
                    + packageName + "/" + gestureVid))
            simpleVideoView!!.requestFocus()
            simpleVideoView!!.start()
            simpleVideoView!!.setOnErrorListener { mp, what, extra ->
                Toast.makeText(applicationContext, "An Error Occured " +
                        "While Playing Video !!!", Toast.LENGTH_LONG).show()
                false
            }
        }





    }
}