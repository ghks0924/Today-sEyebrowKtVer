package com.example.today_seyebrowktver.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.BottomSheetFragmentCustomer
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.data.CustomersData
import com.example.today_seyebrowktver.RvCustomerAdapter
import com.example.today_seyebrowktver.databinding.FragmentCustomersBinding
import com.example.today_seyebrowktver.databinding.RvItemCustomersBinding
import com.example.today_seyebrowktver.viewmodel.FragmentCustomerViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

private val TAG = "FragmentCustomers"

class FragmentCustomers : Fragment() {

    //dataBinding
    private lateinit var binding: FragmentCustomersBinding

    //viewModel
    private val fragmentCustomerViewModel: FragmentCustomerViewModel by lazy {
        ViewModelProvider(this).get(FragmentCustomerViewModel::class.java)
    }

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    var data = ArrayList<CustomersData>()
    var dataForSearch = ArrayList<CustomersData>()
    private lateinit var customersSort: String
    var adapter: RvCustomerAdapter? = null
    private lateinit var adapter2: CustomerAdapter

    private lateinit var uid: String

    //Message Group Create activity??? ?????? ??????
    val activityForNewCustomerResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            adapter2.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customers,
            container, false)

        setData() //customer ????????? ???????????? arrayList??? ??????

        val user = mAuth.currentUser
        uid = user!!.uid

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout() //???????????? ??????????????? ??? ??????
    }

    private fun setLayout() {
        binding.abcTv.setOnClickListener(
            View.OnClickListener
            //???????????? ?????? ??????
            {
                binding.abcTv.setTextColor(Color.parseColor("#4f4f4f"))
                binding.savedTv.setTextColor(Color.parseColor("#4D4f4f4f"))
                data.sortBy { data1 -> data1.customerName }
                binding.recyclerview.adapter?.notifyDataSetChanged()
                database.child("users").child(uid).child("customersSort").setValue("name")
                    .addOnSuccessListener {
                    }.addOnFailureListener {
                    }
            })

        binding.savedTv.setOnClickListener(View.OnClickListener
        //????????? ?????? ??????
        {
            binding.abcTv.setTextColor(Color.parseColor("#4D4f4f4f"))
            binding.savedTv.setTextColor(Color.parseColor("#4f4f4f"))
            data.sortByDescending { data1 -> data1.savedate }
//            data.sortBy { data1 -> data1.savedate }
            binding.recyclerview.adapter?.notifyDataSetChanged()
            database.child("users").child(uid).child("customersSort").setValue("date")
                .addOnSuccessListener {
                }.addOnFailureListener {
                }
        })

        binding.edittext.clearFocus()

        binding.fab.setOnClickListener(View.OnClickListener {
            val frag = BottomSheetFragmentCustomer()
            frag.show(parentFragmentManager, frag.tag)
//            (activity as ActivityMain).mSelectHowToCreateCustomer()
            activityForNewCustomerResult
        })

        binding.edittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                var searchText: String = binding.edittext.text.toString().toLowerCase()
                search(searchText)

                if (searchText.isEmpty()) {
//                    setClickListener(data)
                } else {
//                    setClickListener(dataForSearch)
                }

            }

        })
    }

    private fun setData() {
        data.clear()
        data = fragmentCustomerViewModel.customerList
        dataForSearch.clear()
        dataForSearch = fragmentCustomerViewModel.customerList
        setRv()
    }

    private fun setRv() {
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CustomerAdapter(data)
        }

    }

//    private fun setClickListener(data: ArrayList<CustomersData>) {
//        adapter!!.itemClick = object : RvCustomerAdapter.ItemClick {
//            override fun onClick(view: View, position: Int) {
//                val intent = Intent(context, ActivityEachCustomer::class.java)
//                intent.putExtra("keyValue", data[position].keyValue)
//                startActivity(intent)
//                Log.d("click?", "click OK" + data[position].keyValue)
//
//            }
//
//        }
//
//        adapter!!.itemLongClick = object : RvCustomerAdapter.ItemLongClick {
//            override fun onClick(view: View, position: Int) {
//                val ab: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
//                ab.setTitle(data[position].customerName)
//                ab.setMessage("????????? ?????? ???????????????????")
//                ab.setPositiveButton("???", DialogInterface.OnClickListener { dialog, which ->
//
//                    database.child("users").child(uid).child("customers")
//                        .child(data[position].keyValue).removeValue()
//                        .addOnSuccessListener {
//                            Log.d("removeValue", "?????? ??????")
//                        }.addOnCanceledListener {
//                            Log.d("removeValue", "?????? ??????")
//                        }
//
//                })
//                ab.setNegativeButton("?????????", DialogInterface.OnClickListener { dialog, which -> })
//                ab.setCancelable(true)
//                ab.show()
//                Log.d("click?", "LongClick OK")
//            }
//
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    //?????? ?????? ????????? ??????
    fun search(charText: String) {
        //?????? ??????????????? ???????????? ????????? ?????? ????????????.
        dataForSearch.clear() // ?????? ??????! ????????? Adapter??? ???????????? ???????????? ?????????
        //??????????????? ???????????? ?????? ???????????? ????????????.
        if (charText.length == 0) {
            binding.recyclerview.adapter = CustomerAdapter(data)
        } else { //???????????????
            for (i in data.indices) { //???????????? ?????? ???????????? ????????????.
                if (data[i].customerName.toLowerCase().contains(charText)
                    || data[i].customerNumber.contains(charText)
                ) {
                    //????????? ???????????? ???????????? ????????????.
                    //list.add(arraylist.get(i));
                    dataForSearch.add(data[i])
                }
            }

            binding.recyclerview.adapter = CustomerAdapter(dataForSearch)
        }

    }

    companion object {
        fun newInstance(): FragmentCustomers {
            val args = Bundle()

            val fragment = FragmentCustomers()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        if (binding.recyclerview.adapter != null) {
            setData()
//            binding.recyclerview.adapter!!.notifyDataSetChanged()
        }
    }

    private inner class CustomerHolder(private val binding: RvItemCustomersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = CustomerViewModel()
        }

        fun bind(customer: CustomersData) {
            binding.apply {
                viewModel?.customer = customer
                executePendingBindings()
            }

            binding.parentCardview.setOnClickListener {
                val intent = Intent(context, ActivityEachCustomer::class.java)
                intent.putExtra("keyValue", customer.keyValue)
                startActivity(intent)
                Log.d("click?", "click OK" + customer.keyValue)
            }

            binding.parentCardview.setOnLongClickListener {
                val ab: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
                ab.setTitle(customer.customerName)
                ab.setMessage("????????? ?????? ???????????????????")
                ab.setPositiveButton("???", DialogInterface.OnClickListener { dialog, which ->

                    database.child("users").child(uid).child("customers")
                        .child(customer.keyValue).removeValue()
                        .addOnSuccessListener {
                            Log.d("removeValue", "?????? ??????")
                        }.addOnCanceledListener {
                            Log.d("removeValue", "?????? ??????")
                        }



                })
                ab.setNegativeButton("?????????", DialogInterface.OnClickListener { dialog, which -> })
                ab.setCancelable(true)
                ab.show()
                Log.d("click?", "LongClick OK")
                return@setOnLongClickListener true
            }
        }
    }


private inner class CustomerAdapter(private val customers: List<CustomersData>) :
    RecyclerView.Adapter<CustomerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            CustomerHolder {
        val binding = DataBindingUtil.inflate<RvItemCustomersBinding>(
            layoutInflater,
            R.layout.rv_item_customers,
            parent,
            false
        )
        return CustomerHolder(binding)

    }

    override fun onBindViewHolder(holder: CustomerHolder, position: Int) {
        val customer = customers[position]
        holder.bind(customer)
    }

    override fun getItemCount(): Int = customers.size

}

fun test() {

}


}