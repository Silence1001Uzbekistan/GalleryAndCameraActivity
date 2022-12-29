package uz.artikov.modul_6_lesson_5_test

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import kotlinx.coroutines.currentCoroutineContext
import uz.artikov.modul_6_lesson_5_test.databinding.ActivityCameraBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.jvm.Throws

class CameraActivity : AppCompatActivity() {

    lateinit var binding: ActivityCameraBinding
    lateinit var currentImagePath: String
    var requestCode = 1

    lateinit var photoURI: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val imageFile = createImageFile()




        binding.oldBtn.setOnClickListener {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)

            val photoFile = createImageFile()

            photoFile.also {

                val photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, it)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, requestCode)

            }

        }

        binding.newBtn.setOnClickListener {

            val imageFile = createImageFile()
            photoURI = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID,
                imageFile
            )

            getTakeImageContent.launch(photoURI)
        }

        binding.deleteBtn.setOnClickListener {

        }

    }

    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {

            if (it) {
                binding.cameraImg.setImageURI(photoURI)
                val openInputStream = contentResolver?.openInputStream(photoURI)
                val file = File(filesDir, "image.jpg")
                val fileOutputStream = FileOutputStream(file)
                openInputStream?.copyTo(fileOutputStream)
                openInputStream?.close()
                fileOutputStream.close()

                val fileAbsolutePath = file.absolutePath
                Toast.makeText(this, fileAbsolutePath, Toast.LENGTH_SHORT).show()
            }

        }

    @SuppressLint("NewApi")
    @Throws(IOException::class)
    private fun createImageFile(): File {

        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("JPEG_$format", ".jpg", externalFilesDir).apply {
            currentImagePath = absolutePath
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (::currentImagePath.isInitialized) {
            binding.cameraImg.setImageURI(Uri.fromFile(File(currentImagePath)))
        }

    }

}