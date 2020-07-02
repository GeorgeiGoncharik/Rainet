package xyz.goshanchik.rainet.plantdetail

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.plant_tracker_fragment.*
import xyz.goshanchik.rainet.R
import xyz.goshanchik.rainet.databinding.PlantDetailFragmentBinding
import xyz.goshanchik.rainet.model.PlantDatabase
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class PlantDetailFragment : Fragment() {

    companion object {
        fun newInstance() = PlantDetailFragment()
        private val REQUEST_IMAGE_CAPTURE = 1
        private val REQUEST_TAKE_PHOTO = 2
    }

    private lateinit var viewModel: PlantDetailViewModel
    private lateinit var binding: PlantDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.plant_detail_fragment, container, false
        )
        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application

        val dao = PlantDatabase.getInstance(application).databaseDao
        val args = PlantDetailFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = PlantDetailViewModelFactory(key = args.plantId, dataSource = dao)

        viewModel = ViewModelProvider(this, viewModelFactory).get(PlantDetailViewModel::class.java)

//        activity!!.actionBar!!.title = viewModel.plant.value!!.name

        binding.viewModel = viewModel

        binding.nameText.doOnTextChanged { text, _, _, _ ->
            viewModel.changeName(text.toString())
        }

        binding.descriptionText.doOnTextChanged { text, _, _, _ ->
            viewModel.changeDescription(text.toString())
        }

        binding.buttonPeriod.setOnClickListener{
            Log.i("PlantDetailFragment" , "buttonPeriod listener called.")
            viewModel.navigateToTakeImage()
            viewModel.onNavigateToTakeImage()
        }

        viewModel.plant.observe(viewLifecycleOwner, Observer {
            Log.i("PlantDetailFragment" , "plant observer called.")
            //TODO cleanup
            it?.wateringPeriod?.let { it1 -> binding.buttonPeriod.NumToString(it1) }


            val file = File(it!!.photoUri)
            if(file.exists()){
                val myBitmap: Bitmap = BitmapFactory.decodeFile(file.toString())
                binding.imageView.setImageBitmap(myBitmap)
            }
        })

        viewModel.navigateToTakeImage.observe(viewLifecycleOwner, Observer {
            Log.i("PlantDetailFragment" , "navigateToTakeImage observer called.")
            if(it){
                dispatchTakePictureIntent()
            }
        })

        return binding.root
    }


    private lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        activity!!,
                        "xyz.goshanchik.rainet.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.i("PlantDetailFragment" , "onActivityResult called.")
            viewModel.changePhotoUri(currentPhotoPath)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        bottomAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.camera_item -> {
                    viewModel.navigateToTakeImage()
                    viewModel.onNavigateToTakeImage()
                    true
                }
                R.id.gallery_item -> {
                    Toast.makeText(activity!!, "gallery called", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }
}