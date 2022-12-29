package uz.artikov.modul_6_lesson_5_test.db

import uz.artikov.modul_6_lesson_5_test.models.ImageModel

interface DbHelper {

    fun insertImage(imageModel: ImageModel)
    fun getAllImages():List<ImageModel>

}