package uz.artikov.modul_6_lesson_5_test

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import uz.artikov.modul_6_lesson_5_test.databinding.ActivityGaleryBinding
import uz.artikov.modul_6_lesson_5_test.db.MyDbHelper
import uz.artikov.modul_6_lesson_5_test.models.ImageModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files

class GaleryActivity : AppCompatActivity() {

    lateinit var binding: ActivityGaleryBinding
    var REQUEST_CODE = 1

    lateinit var myDbHelper: MyDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGaleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.oldBtn.setOnClickListener {
            pickImageFromOldGallery()
        }

        binding.newBtn.setOnClickListener {
            pickImageFromNewGallery()
        }

        binding.deleteBtn.setOnClickListener {
            clearImages()
        }


    }

    private fun pickImageFromOldGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun clearImages() {

        val filesDir = filesDir

        if(filesDir.isDirectory){
            val listFiles = filesDir.listFiles()

            for (listFile in listFiles) {
                Toast.makeText(this, "clearImages:${listFile.absolutePath}", Toast.LENGTH_SHORT).show()
                listFile.delete()
            }

        }

    }

    private fun pickImageFromNewGallery() {
        getImageContent.launch("image/*")
    }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            uri ?:return@registerForActivityResult
            binding.galleryImg.setImageURI(uri)

            val openInputStream = contentResolver?.openInputStream(uri)
            val file = File(filesDir, "image.jpg")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()

            val fileAbsolutePath = file.absolutePath

            val byteArray = ByteArray(file.length().toInt())
            val fileInputStream = FileInputStream(file)
            val readBytes = fileInputStream.readBytes()
            val imageModel = ImageModel(fileAbsolutePath, readBytes)
            myDbHelper.insertImage(imageModel)

            Toast.makeText(this, fileAbsolutePath, Toast.LENGTH_SHORT).show()

        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            binding.galleryImg.setImageURI(uri)

            val openInputStream = contentResolver?.openInputStream(uri)
            val file = File(filesDir, "image.jpg")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()

            val fileAbsolutePath = file.absolutePath
            Toast.makeText(this, fileAbsolutePath, Toast.LENGTH_SHORT).show()

        }

    }


}