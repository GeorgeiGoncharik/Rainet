package xyz.goshanchik.rainet.planttracker

import android.app.SearchManager
import android.content.Context
import android.content.Context.SEARCH_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.plant_tracker_fragment.*
import xyz.goshanchik.rainet.R
import xyz.goshanchik.rainet.databinding.PlantTrackerFragmentBinding
import xyz.goshanchik.rainet.model.PlantDatabase


class PlantTrackerFragment : Fragment() {

    companion object {
        fun newInstance() = PlantTrackerFragment()
    }

    private lateinit var viewModel: PlantTrackerViewModel
    private lateinit var binding: PlantTrackerFragmentBinding
    private lateinit var adapter: PlantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /**
         * all kind of bindings and initializations goes here
         */
        val application = requireNotNull(this.activity).application

        binding = DataBindingUtil
            .inflate(inflater, R.layout.plant_tracker_fragment, container, false)
        binding.lifecycleOwner = this

        val dao = PlantDatabase.getInstance(application).databaseDao
        val viewModelFactory = PlantTrackerViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlantTrackerViewModel::class.java)

        adapter = PlantAdapter()
        binding.recyclerPlants.adapter = adapter

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    val item = adapter.getNoteAt(viewHolder.adapterPosition).copy()

                    viewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))

                    Snackbar.make(activity!!.findViewById(android.R.id.content), "Are you sure you want to delete it?", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        viewModel.add(item)
                    }
                        .setAnchorView(if(addButton.isVisible) addButton else bottomAppBar)
                        .show()
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerPlants)

        /**
         * Set up search bar
         */


        /**
         * here is the place for viewModel observers
         */
        viewModel.plants.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.navigateToPlantDetailFragment.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(PlantTrackerFragmentDirections.actionPlantTrackerFragmentToPlantDetailFragment(it.id))
                Log.i("PlantTrackerFragment", "navigating: ${it.id}")
                viewModel.doneNavigating()
            }
        })

        //TODO: convert to data binding inside the layout
        binding.addButton.setOnClickListener {
            viewModel.addAndNavigate()
        }

        binding.recyclerPlants.setOnItemClickListener {
            val item = adapter.getNoteAt(it)
            viewModel.navigateToPlantDetailFragment(item)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        bottomAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.search_item -> {
                    Toast.makeText(context, "Pressed search", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}