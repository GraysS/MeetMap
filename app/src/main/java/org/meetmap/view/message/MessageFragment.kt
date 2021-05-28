package org.meetmap.view.message

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import org.meetmap.EventObserver
import org.meetmap.R
import org.meetmap.data.model.domain.Familiar
import org.meetmap.util.showSnackbar
import org.meetmap.view.message.buildMessages.MessagesInjector
import timber.log.Timber


class MessageFragment : Fragment() {

    private lateinit var viewModel: MessagesViewModel
    private lateinit var adapter: MessageListAdapter
    private lateinit var messageList: RecyclerView
    private lateinit var familiar: Familiar

    private lateinit var ibSendArrow: ImageButton
    private lateinit var etMessage: EditText

    private val args: MessageFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
       /* requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().popBackStack()
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.let {
            this.familiar = it.familiar ?: Familiar(0,"","","")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageList = view.findViewById(R.id.rv_message)
        ibSendArrow = view.findViewById(R.id.ib_sendMessage)
        etMessage = view.findViewById(R.id.et_message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //THIS IS IMPORTANT!!!
        messageList.adapter = null
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            MessagesInjector(requireActivity().application).provideMessagesViewModelFactory()
        ).get(MessagesViewModel::class.java)

        setClickListeners()
        setUpAdapter()
        observeViewModel()

        viewModel.handleEvent(MessagesEvent.OnStartGetMessage(familiar))
    }

    private fun setClickListeners() {
        ibSendArrow.setOnClickListener {
            viewModel.handleEvent(MessagesEvent.OnClickButtonSendMessage(familiar,etMessage.text.toString()))
        }
    }

    private fun setUpAdapter() {
        adapter = MessageListAdapter(familiar)
        messageList.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.msgCollectionReference.observe(viewLifecycleOwner, {
            viewModel.messageGoLiveData(it!!).observe(viewLifecycleOwner, { list ->
                adapter.submitList(list)
            })
        })

        viewModel.errorTx.observe(viewLifecycleOwner, {
            showErrorState(it)
        })

        viewModel.goMaps.observe(viewLifecycleOwner, EventObserver {
            startMeetGoogle(familiar)
        })
    }

    private fun startMeetGoogle(familiar: Familiar)  {
        Timber.d("GO GOGO GO GO")
        findNavController().navigate(MessageFragmentDirections.actionMessageFragmentDestToMeetFragmentDest(familiar))
    }

    private fun showErrorState(errorMessage: String) = view?.showSnackbar(errorMessage, Snackbar.LENGTH_SHORT)

}