package com.daniily000.android.kodeprojecttwo

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        captureButton.setOnClickListener {

            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (captureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(captureIntent, REQUEST_CAMERA)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(NAME, nameText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        nameText.setText(savedInstanceState?.getString(NAME))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CAMERA -> {
                when (resultCode) {

                    Activity.RESULT_OK -> {
                        Log.i(TAG, "REQUEST_CAMERA -> RESULT_OK")
                        TODO()
                    }

                    Activity.RESULT_CANCELED -> {
                        Log.i(TAG, "REQUEST_CAMERA -> RESULT_CANCELLED")
                        TODO()
                    }

                    else -> {
                        Log.wtf(TAG, "REQUEST_CAMERA -> else!? Crushing result")
                    }

                }
            }
        }
    }


    companion object {

        private const val TAG = "MainActivity"
        private const val NAME = "name"
        private const val REQUEST_CAMERA = 0
    }
}
