package com.abra.homework_8_2.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.abra.homework_8_2.R
import com.abra.homework_8_2.data.CarInfo
import com.abra.homework_8_2.data.WorkInfo
import com.abra.homework_8_2.database.DataBaseCarInfo
import com.abra.homework_8_2.database.WorkInfoDAO
import java.text.SimpleDateFormat
import java.util.Date

class FragmentAddWork : Fragment(R.layout.fragment_add_work) {
    private lateinit var loader: FragmentLoader
    private lateinit var textData: TextView
    private lateinit var etWorkName: EditText
    private lateinit var etWorkDescription: EditText
    private lateinit var etWorkCost: EditText
    private lateinit var ivPending: ImageView
    private lateinit var ivInProgress: ImageView
    private lateinit var ivCompleted: ImageView
    private lateinit var imgButtonBack: ImageButton
    private lateinit var imgButtonApply: ImageButton
    private lateinit var tvPending: TextView
    private lateinit var tvInProgress: TextView
    private lateinit var tvCompleted: TextView
    private lateinit var checkedStatus: String
    private lateinit var date: String
    private var currentCarId: Long = 0
    private lateinit var currentCar: CarInfo
    private lateinit var database: DataBaseCarInfo
    private lateinit var workInfoDAO: WorkInfoDAO
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            textData = findViewById(R.id.tvDate)
            etWorkName = findViewById(R.id.etWorkName)
            etWorkDescription = findViewById(R.id.etWorkDescription)
            etWorkCost = findViewById(R.id.etWorkCost)
            ivPending = findViewById(R.id.ivPending)
            ivInProgress = findViewById(R.id.ivInProgress)
            ivCompleted = findViewById(R.id.ivCompleted)
            imgButtonApply = findViewById(R.id.imgButtonApplyToWorkList)
            imgButtonBack = findViewById(R.id.imgButtonBackToWorkList)
            tvPending = findViewById(R.id.tvPending)
            tvInProgress = findViewById(R.id.tvInProgress)
            tvCompleted = findViewById(R.id.tvCompleted)
        }
        loader = requireActivity() as FragmentLoader
        initDatabase()
        loadDataFromBundle(requireArguments())
        setImageListeners()
        setButtonsListeners()
        getCurrentDate()
        checkedStatus = resources.getString(R.string.pending)
    }

    private fun initDatabase() {
        database = DataBaseCarInfo.getDataBase(context as Context)
        workInfoDAO = database.getWorkInfoDAO()
    }

    private fun getCurrentDate() {
        val simpleDateFormat = SimpleDateFormat.getDateInstance()
        date = simpleDateFormat.format(Date())
        textData.text = "${resources.getString(R.string.application_date)} - $date"
    }

    private fun loadDataFromBundle(bundle: Bundle) {
        currentCarId = bundle.getLong("currentCarId", 0)
        bundle.getParcelable<CarInfo>("carInfo")?.run {
            currentCar = this
        }
    }

    private fun setImageListeners() {
        ivPending.setOnClickListener {
            ivPending.setImageResource(R.drawable.ic_baseline_handyman_48_pending)
            ivInProgress.setImageResource(R.drawable.ic_baseline_handyman_48_default)
            ivCompleted.setImageResource(R.drawable.ic_baseline_handyman_48_default)
            checkedStatus = resources.getString(R.string.pending)
            tvPending.setTextColor(resources.getColor(R.color.work_status_pending))
            tvInProgress.setTextColor(resources.getColor(R.color.icon_work_default_color))
            tvCompleted.setTextColor(resources.getColor(R.color.icon_work_default_color))
        }
        ivInProgress.setOnClickListener {
            ivPending.setImageResource(R.drawable.ic_baseline_handyman_48_default)
            ivInProgress.setImageResource(R.drawable.ic_baseline_handyman_48_in_progress)
            ivCompleted.setImageResource(R.drawable.ic_baseline_handyman_48_default)
            checkedStatus = resources.getString(R.string.in_progress_lowe_case)
            tvPending.setTextColor(resources.getColor(R.color.icon_work_default_color))
            tvInProgress.setTextColor(resources.getColor(R.color.work_status_in_progress))
            tvCompleted.setTextColor(resources.getColor(R.color.icon_work_default_color))
        }
        ivCompleted.setOnClickListener {
            ivPending.setImageResource(R.drawable.ic_baseline_handyman_48_default)
            ivInProgress.setImageResource(R.drawable.ic_baseline_handyman_48_default)
            ivCompleted.setImageResource(R.drawable.ic_baseline_handyman_48_completed)
            checkedStatus = resources.getString(R.string.completed_in_lower_case)
            tvPending.setTextColor(resources.getColor(R.color.icon_work_default_color))
            tvInProgress.setTextColor(resources.getColor(R.color.icon_work_default_color))
            tvCompleted.setTextColor(resources.getColor(R.color.work_status_completed))
        }
    }

    private fun setButtonsListeners() {
        imgButtonBack.setOnClickListener {
            backToPreviousFragment()
        }
        imgButtonApply.setOnClickListener {
            addWork()
        }
    }

    private fun backToPreviousFragment() {
        loader.loadFragment(FragmentWorkList::class.java,
                FragmentTransaction.TRANSIT_FRAGMENT_CLOSE,
                bundleOf("carInfo" to currentCar))
    }

    private fun addWork() {
        val workName = etWorkName.text.toString()
        val workDescription = etWorkDescription.text.toString()
        val workCost = etWorkCost.text.toString()
        if (workName.isNotEmpty() && workDescription.isNotEmpty() && workCost.isNotEmpty()) {
            workInfoDAO.addWork(WorkInfo(date, workName, workDescription, workCost, checkedStatus, currentCarId))
            backToPreviousFragment()
        } else {
            Toast.makeText(context, "Fields can't be empty", Toast.LENGTH_SHORT).show()
        }
    }
}