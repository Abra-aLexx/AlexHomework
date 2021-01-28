package com.abra.homework_8_2.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.abra.homework_8_2.R
import com.abra.homework_8_2.data.CarInfo
import com.abra.homework_8_2.data.WorkInfo
import com.abra.homework_8_2.database.DataBaseCarInfo
import com.abra.homework_8_2.database.WorkInfoDAO
import com.abra.homework_8_2.functions.setImageStatus

class FragmentEditWorkInfo : Fragment(R.layout.fragment_edit_work_info) {
    private lateinit var textData: TextView
    private lateinit var etWorkName: EditText
    private lateinit var etWorkDescription: EditText
    private lateinit var etWorkCost: EditText
    private lateinit var ivPending: ImageView
    private lateinit var ivInProgress: ImageView
    private lateinit var ivCompleted: ImageView
    private lateinit var imgButtonBack: ImageButton
    private lateinit var imgButtonApply: ImageButton
    private lateinit var imgButtonRemove: ImageButton
    private lateinit var tvPending: TextView
    private lateinit var tvInProgress: TextView
    private lateinit var tvCompleted: TextView
    private lateinit var tvCurrentWorkName: TextView
    private lateinit var checkedStatus: String
    private var currentCarId: Long = 0
    private lateinit var currentCar: CarInfo
    private lateinit var currentWorkInfo: WorkInfo
    private lateinit var database: DataBaseCarInfo
    private lateinit var workInfoDAO: WorkInfoDAO
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            textData = findViewById(R.id.tvDate1)
            etWorkName = findViewById(R.id.etWorkName1)
            etWorkDescription = findViewById(R.id.etWorkDescription1)
            etWorkCost = findViewById(R.id.etWorkCost1)
            ivPending = findViewById(R.id.ivPending1)
            ivInProgress = findViewById(R.id.ivInProgress1)
            ivCompleted = findViewById(R.id.ivCompleted1)
            imgButtonApply = findViewById(R.id.imgButtonApplyToWorkList1)
            imgButtonBack = findViewById(R.id.imgButtonBackToWorkList1)
            imgButtonRemove = findViewById(R.id.imgButtonRemove)
            tvPending = findViewById(R.id.tvPending1)
            tvInProgress = findViewById(R.id.tvInProgress1)
            tvCompleted = findViewById(R.id.tvCompleted1)
            tvCurrentWorkName = findViewById(R.id.tvCurrentWorkName)
        }
        initDatabase()
        setImageListeners()
        setButtonsListeners()
        loadDataFromBundle(requireArguments())
    }

    private fun initDatabase() {
        database = DataBaseCarInfo.getDataBase(context as Context)
        workInfoDAO = database.getWorkInfoDAO()
    }

    private fun loadDataFromBundle(bundle: Bundle) {
        bundle.getParcelable<CarInfo>("carInfo")?.run {
            currentCar = this
        }
        currentWorkInfo = bundle.getParcelable("workInfo") ?: WorkInfo()
        with(currentWorkInfo) {
            currentCarId = carInfoId
            textData.text = "${resources.getString(R.string.application_date)} - ${date}"
            etWorkName.setText(workName)
            etWorkDescription.setText(description)
            etWorkCost.setText(cost)
            setIconStatus(status)
            tvCurrentWorkName.text = workName
        }
    }

    private fun setIconStatus(status: String?) {
        when (status) {
            resources.getString(R.string.pending) -> {
                setImageStatus(status, resources, ivPending, tvPending)
                checkedStatus = resources.getString(R.string.pending)
            }
            resources.getString(R.string.in_progress_lowe_case) -> {
                setImageStatus(status, resources, ivInProgress, tvInProgress)
                checkedStatus = resources.getString(R.string.in_progress_lowe_case)
            }
            resources.getString(R.string.completed_in_lower_case) -> {
                setImageStatus(status, resources, ivCompleted, tvCompleted)
                checkedStatus = resources.getString(R.string.completed_in_lower_case)
            }
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
            editWork()
        }
        imgButtonRemove.setOnClickListener {
            removeWork()
        }
    }

    private fun backToPreviousFragment() {
        requireActivity().supportFragmentManager.commit {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            replace(R.id.fragmentContainer, FragmentWorkList::class.java,
                    bundleOf("carInfo" to currentCar))
        }
    }

    private fun removeWork() {
        createDialog()
    }

    private fun editWork() {
        val workName = etWorkName.text.toString()
        val workDescription = etWorkDescription.text.toString()
        val workCost = etWorkCost.text.toString()
        if (workName.isNotEmpty() && workDescription.isNotEmpty() && workCost.isNotEmpty()) {
            val workInfo = WorkInfo(currentWorkInfo.date, workName, workDescription, workCost, checkedStatus, currentCarId).also { it.id = currentWorkInfo.id }
            workInfoDAO.update(workInfo)
            backToPreviousFragment()
        } else {
            Toast.makeText(context, "Fields can't be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createDialog() {
        AlertDialog.Builder(context as Context)
                .setTitle(getString(R.string.remove_work))
                .setMessage(getString(R.string.warning))
                .setPositiveButton("Apply"
                ) { dialogInterface, i ->
                    workInfoDAO.delete(currentWorkInfo)
                    backToPreviousFragment()
                }
                .setNegativeButton("Cancel") { dialogInterface, i -> dialogInterface.cancel() }
                .setCancelable(false)
                .create()
                .show()
    }
}