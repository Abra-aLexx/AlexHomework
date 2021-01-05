package com.abra.homework_5

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar

class EditWorkActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
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
    private lateinit var currentWorkInfo: WorkInfo
    private val RESULT_CODE_BUTTON_BACK = 6
    private val RESULT_CODE_BUTTON_REMOVE = 7
    private lateinit var database: DataBaseCarInfo
    private lateinit var workInfoDAO: WorkInfoDAO
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_work)
        toolbar = findViewById(R.id.toolBarEditWork)
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
        database = DataBaseCarInfo.getDataBase(applicationContext)
        workInfoDAO = database.getWorkInfoDAO()
        setSupportActionBar(toolbar)
        setImageListeners()
        setButtonsListeners()
        loadDataFromIntent()
    }

    private fun loadDataFromIntent() {
        if (intent != null) {
            currentWorkInfo = intent.getParcelableExtra("workInfo")!!
                currentCarId = currentWorkInfo.carInfoId
                val date = currentWorkInfo.date
                textData.text = "${resources.getString(R.string.application_date)} - $date"
                etWorkName.setText(currentWorkInfo.workName)
                etWorkDescription.setText(currentWorkInfo.description)
                etWorkCost.setText(currentWorkInfo.cost)
                setIconStatus(currentWorkInfo.status)
                tvCurrentWorkName.text = currentWorkInfo.workName
        }
    }

    private fun setIconStatus(status: String?) {
        when (status) {
            "pending" -> {
                ivPending.setImageResource(R.drawable.ic_baseline_handyman_48_pending)
                tvPending.setTextColor(resources.getColor(R.color.work_status_pending))
                checkedStatus = "pending"
            }
            "in progress" -> {
                ivInProgress.setImageResource(R.drawable.ic_baseline_handyman_48_in_progress)
                tvInProgress.setTextColor(resources.getColor(R.color.work_status_in_progress))
                checkedStatus = "in progress"
            }
            "completed" -> {
                ivCompleted.setImageResource(R.drawable.ic_baseline_handyman_48_completed)
                tvCompleted.setTextColor(resources.getColor(R.color.work_status_completed))
                checkedStatus = "completed"
            }
        }
    }

    private fun setImageListeners() {
        ivPending.setOnClickListener {
            ivPending.setImageResource(R.drawable.ic_baseline_handyman_48_pending)
            ivInProgress.setImageResource(R.drawable.ic_baseline_handyman_48_default)
            ivCompleted.setImageResource(R.drawable.ic_baseline_handyman_48_default)
            checkedStatus = "pending"
            tvPending.setTextColor(resources.getColor(R.color.work_status_pending))
            tvInProgress.setTextColor(resources.getColor(R.color.icon_work_default_color))
            tvCompleted.setTextColor(resources.getColor(R.color.icon_work_default_color))
        }
        ivInProgress.setOnClickListener {
            ivPending.setImageResource(R.drawable.ic_baseline_handyman_48_default)
            ivInProgress.setImageResource(R.drawable.ic_baseline_handyman_48_in_progress)
            ivCompleted.setImageResource(R.drawable.ic_baseline_handyman_48_default)
            checkedStatus = "in progress"
            tvPending.setTextColor(resources.getColor(R.color.icon_work_default_color))
            tvInProgress.setTextColor(resources.getColor(R.color.work_status_in_progress))
            tvCompleted.setTextColor(resources.getColor(R.color.icon_work_default_color))
        }
        ivCompleted.setOnClickListener {
            ivPending.setImageResource(R.drawable.ic_baseline_handyman_48_default)
            ivInProgress.setImageResource(R.drawable.ic_baseline_handyman_48_default)
            ivCompleted.setImageResource(R.drawable.ic_baseline_handyman_48_completed)
            checkedStatus = "completed"
            tvPending.setTextColor(resources.getColor(R.color.icon_work_default_color))
            tvInProgress.setTextColor(resources.getColor(R.color.icon_work_default_color))
            tvCompleted.setTextColor(resources.getColor(R.color.work_status_completed))
        }
    }

    private fun setButtonsListeners() {
        imgButtonBack.setOnClickListener {
            showActivity(true, false)
        }
        imgButtonApply.setOnClickListener {
            showActivity(false, false)
        }
        imgButtonRemove.setOnClickListener {
            showActivity(false, true)
        }
    }

    private fun showActivity(isButtonBack: Boolean, isRemoved: Boolean) {
        val intent = Intent()
        if (!isButtonBack) {
            if (!isRemoved) {
                val workName = etWorkName.text.toString()
                val workDescription = etWorkDescription.text.toString()
                val workCost = etWorkCost.text.toString()
                if (workName.isNotEmpty() && workDescription.isNotEmpty() && workCost.isNotEmpty()) {
                    val workInfo = WorkInfo(currentWorkInfo.date,workName,workDescription,workCost,checkedStatus,currentCarId).also { it.id = currentWorkInfo.id }
                    workInfoDAO.update(workInfo)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show()
                }
            } else {
                createDialog(intent,isButtonBack)
            }

        } else {
            setResult(RESULT_CODE_BUTTON_BACK, intent)
            finish()
        }
    }

    fun createDialog(intent: Intent, isButtonBack: Boolean) {
        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.remove_work))
                .setMessage(getString(R.string.warning))
                .setPositiveButton("Apply",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            workInfoDAO.delete(currentWorkInfo)
                            setResult(RESULT_CODE_BUTTON_REMOVE, intent)
                            finish()
                        })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })
                .setCancelable(false)
                .create()
                .show()
    }
}