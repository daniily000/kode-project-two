package com.daniily000.android.kodeprojecttwo

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.provider.MediaStore
import android.support.media.ExifInterface
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import com.daniily000.android.kodeprojecttwo.MainActivity.Companion.NAME
import com.daniily000.android.kodeprojecttwo.MainActivity.Companion.PHOTO
import kotlinx.android.synthetic.main.activity_after_capture.*
import java.io.File
import android.graphics.BitmapFactory
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION






class AfterCaptureActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_capture)

        // hide hardware buttons and status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        val photoPath = intent.getStringExtra(PHOTO)
        val name = intent.getStringExtra(NAME)

        val uriFromProvider = FileProvider.getUriForFile(
            this,
            "$packageName.fileprovider",
            File(photoPath)
        )

        imageView.setImageBitmap(
            validateBitmap(
                MediaStore.Images.Media.getBitmap(contentResolver, uriFromProvider),
                photoPath
            )
        )

        signTextView.text = name

        val options = BitmapFactory.Options()
        options.inSampleSize = 16
        blurredImageView.setImageBitmap(BitmapFactory.decodeFile(photoPath, options))


    }


    private fun validateBitmap(bitmap: Bitmap, path: String): Bitmap {

        val ei = ExifInterface(path)
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        return when (orientation) {

            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)

            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)

            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)

            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {

        var bitmap = source
        val matrix = Matrix()
        matrix.postRotate(angle)
        try {
            bitmap = Bitmap.createBitmap(
                source, 0, 0, source.width, source.height,
                matrix, true
            )
        } catch (err: OutOfMemoryError) {
            err.printStackTrace()
        }

        return bitmap
    }

    companion object {
        private const val TAG = "AfterCaptureActivity"
    }

}