package com.daniily000.android.kodeprojecttwo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {


    private var mUriFromProvider: Uri? = null
    private lateinit var photoFile: File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        captureButton.setOnClickListener {

            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (captureIntent.resolveActivity(packageManager) != null) {

                try {
                    photoFile = File.createTempFile("photo_${Date().time}", ".jpg", this.filesDir)
                    Log.i(TAG, "file=${photoFile?.absolutePath}")
                    Log.i(TAG, "filesDir=$filesDir")
                    Log.i(TAG, "packageName=$packageName")


                    photoFile.createNewFile()

                    mUriFromProvider = FileProvider.getUriForFile(
                        this,
                        "$packageName.fileprovider",
                        photoFile
                    )

                    Log.i(TAG, "uri=$mUriFromProvider")

                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUriFromProvider)
                    startActivityForResult(captureIntent, REQUEST_CAMERA)
                } catch (e: IOException) {
                    e.printStackTrace()

                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(NAME, nameText.text.toString())
        if (mUriFromProvider != null) {
            outState?.putParcelable(URI, mUriFromProvider)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        nameText.setText(savedInstanceState?.getString(NAME))
        mUriFromProvider = savedInstanceState?.getParcelable(URI)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CAMERA -> {
                when (resultCode) {

                    Activity.RESULT_OK -> {
                        Log.i(TAG, "REQUEST_CAMERA -> RESULT_OK")

                        val afterCaptureIntent = Intent(this, AfterCaptureActivity::class.java)
                        afterCaptureIntent.putExtra(
                            PHOTO,
                            photoFile.absolutePath
                        )
                        startActivity(afterCaptureIntent)
                    }

                    Activity.RESULT_CANCELED -> {
                        Log.i(TAG, "REQUEST_CAMERA -> RESULT_CANCELLED")
//                        TODO()
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

        const val URI = "uri"
        const val PHOTO = "uri"
    }


}

