package org.meetmap.view.familiar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import org.meetmap.R
import org.meetmap.data.model.domain.Familiar
import org.meetmap.util.showSnackbar
import org.meetmap.view.familiar.buildChats.FamiliarInjector
import org.meetmap.view.message.MessageActivity
import timber.log.Timber

const val OBJECT_FAMILIAR = "OBJECT_FAMILIAR"

class FamiliarFragment : Fragment() {

    private lateinit var viewModel: FamiliarViewModel
    private lateinit var adapter: FamiliarListAdapter
    private lateinit var recListFamiliar: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_familiar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recListFamiliar = view.findViewById(R.id.rv_familiars)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //THIS IS IMPORTANT!!!
        recListFamiliar.adapter = null
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            FamiliarInjector(requireActivity().application).provideFamiliarViewModelFactory()
        ).get(FamiliarViewModel::class.java)

        setUpAdapter()
        observeViewModel()

        viewModel.handleEvent(FamiliarEvent.OnStart)
    }

    private fun observeViewModel() {
        viewModel.familiarList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        viewModel.familiar.observe(viewLifecycleOwner, {
            Timber.d("myFamiliar: %s", it)
            startFamiliarDetailWithArgs(it)
        })

        viewModel.errorTx.observe(viewLifecycleOwner,{
            showErrorState(it)
        })
    }


    private fun setUpAdapter() {
        adapter = FamiliarListAdapter()
       /* adapter.event.observe(viewLifecycleOwner, EventObserver {
                viewModel.handleEvent(it)
            }
        )*/
        recListFamiliar.adapter = adapter
        addTo(recListFamiliar).setOnItemClickListener { recyclerView, i, view ->
            viewModel.handleEvent(FamiliarEvent.OnFamiliarItemClick(i))
        }

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startFamiliarDetailWithArgs(familiar: Familiar) {
        val intent = Intent(activity, MessageActivity::class.java)
        intent.putExtra(OBJECT_FAMILIAR,familiar)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            requireActivity().startActivity(intent)
        }
        /*= findNavController().navigate(
        FamiliarFragmentDirections.actionFamiliarFragmentDestToMessageFragmentDest(familiar)*/

    }

    private fun showErrorState(errorMessage: String) = view?.showSnackbar(errorMessage, Snackbar.LENGTH_SHORT)


}