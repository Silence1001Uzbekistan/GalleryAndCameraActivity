package uz.artikov.modul_6_lesson_5_test

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.artikov.modul_6_lesson_5_test.databinding.ActivityMainBinding
import com.karumi.dexter.PermissionToken

import com.karumi.dexter.MultiplePermissionsReport

import com.karumi.dexter.listener.multi.MultiplePermissionsListener

import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequest
import uz.artikov.modul_6_lesson_5_test.db.MyDbHelper


class MainActivity : AppCompatActivity() {

    lateinit var myDbHelper: MyDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDbHelper = MyDbHelper(this)

        val list = myDbHelper.getAllImages()

       // binding.pathImage.setImageURI(Uri.parse(list[0].imagePath))

        val bitmap = BitmapFactory.decodeByteArray(list[0].image, 0, list[0].image?.size!!)

        binding.byteImage.setImageBitmap(bitmap)

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {



                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()

        binding.galleryBtn.setOnClickListener {
            val intent = Intent(this, GaleryActivity::class.java)
            startActivity(intent)
        }

        binding.camerBtn.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

    }
}