package uz.artikov.modul_6_lesson_5_test.models

class ImageModel {

    var id: Int? = null
    var imagePath: String? = null
    var image: ByteArray? = null

    constructor(id: Int?, imagePath: String?, image: ByteArray?) {
        this.id = id
        this.imagePath = imagePath
        this.image = image
    }

    constructor(imagePath: String?, image: ByteArray?) {
        this.imagePath = imagePath
        this.image = image
    }

    constructor()


}
