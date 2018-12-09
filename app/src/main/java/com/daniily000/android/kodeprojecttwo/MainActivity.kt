package com.daniily000.android.kodeprojecttwo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {


    private var mUriFromProvider: Uri? = null
    private lateinit var mPhotoFile: File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        captureButton.setOnClickListener {

            // no text specified
            if (nameText.text.toString() == "") {
                Snackbar.make(nameText, R.string.noNameWarning, Snackbar.LENGTH_LONG).show()
            } else {

                val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (captureIntent.resolveActivity(packageManager) != null) {

                    try {
                        mPhotoFile = File.createTempFile("photo_${Date().time}", ".jpg", this.filesDir)
                        Log.i(TAG, "file=${mPhotoFile.absolutePath}")
                        Log.i(TAG, "filesDir=$filesDir")
                        Log.i(TAG, "packageName=$packageName")


                        mPhotoFile.createNewFile()

                        mUriFromProvider = FileProvider.getUriForFile(
                            this,
                            "$packageName.fileprovider",
                            mPhotoFile
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
    }

    // save instances
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(NAME_VALUE, nameText.text.toString())
        if (mUriFromProvider != null) {
            outState?.putParcelable(URI, mUriFromProvider)
        }
    }

    // restore instances
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        nameText.setText(savedInstanceState?.getString(NAME_VALUE))
        mUriFromProvider = savedInstanceState?.getParcelable(URI)
    }

    // call AfterCaptureActivity if OK, respond if not
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
                            mPhotoFile.absolutePath
                        )
                        afterCaptureIntent.putExtra(
                            NAME,
                            nameText.text.toString()
                        )

                        startActivity(afterCaptureIntent)
                    }

                    Activity.RESULT_CANCELED -> {
                        Log.i(TAG, "REQUEST_CAMERA -> RESULT_CANCELLED")
                        Snackbar.make(rootViewGroup, R.string.photoCancel, Snackbar.LENGTH_LONG).show()
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
        private const val NAME_VALUE = "name"
        private const val REQUEST_CAMERA = 0

        const val URI = "uri"
        const val PHOTO = "photo"
        const val NAME = "name"
    }


}

