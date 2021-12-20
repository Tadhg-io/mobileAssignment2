package ie.wit.pintmark.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import ie.wit.pintmark.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_marker_image.toString())
    intentLauncher.launch(chooseFile)
}