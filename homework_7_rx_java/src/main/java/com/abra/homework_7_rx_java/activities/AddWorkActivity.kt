package com.abra.homework_7_rx_java.activities

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.abra.homework_7_rx_java.R
import com.abra.homework_7_rx_java.data.WorkInfo
import com.abra.homework_7_rx_java.repositories.DatabaseRepository
import java.text.SimpleDateFormat
import java.util.Date

private const val RESULT_CODE_BUTTON_BACK = 6

class AddWorkActivity : AppCompatActivity() {
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
    private lateinit var tvPending: TextView
    private lateinit var tvInProgress: TextView
    private lateinit var tvCompleted: TextView
    private lateinit var checkedStatus: String
    private lateinit var date: String
    private var currentCarId: Long = 0
    private lateinit var repository: DatabaseRepository

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work)
        toolbar = findViewById(R.id.toolBarAddWork)
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
        repository = DatabaseRepository()
        loadDataFromIntent()
        setSupportActionBar(toolbar)
        setImageListeners()
        setButtonsListeners()
        getCurrentDate()
        checkedStatus = resources.getString(R.string.pending)
    }

    private fun getCurrentDate() {
        val simpleDateFormat = SimpleDateFormat.getDateInstance()
        date = simpleDateFormat.format(Date())
        textData.text = "${resources.getString(R.string.application_date)} - $date"
    }

    private fun loadDataFromIntent() {
        if (intent != null) {
            currentCarId = intent.getLongExtra("currentCarId", 0)
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
            backToPreviousActivity()
        }
        imgButtonApply.setOnClickListener {
            editWorkAndBackToPreviousActivity()
        }
    }

    private fun backToPreviousActivity() {
        setResult(RESULT_CODE_BUTTON_BACK, intent)
        finish()
    }

    private fun editWorkAndBackToPreviousActivity() {

        val workName = etWorkName.text.toString()
        val workDescription = etWorkDescription.text.toString()
        val workCost = etWorkCost.text.toString()
        if (workName.isNotEmpty() && workDescription.isNotEmpty() && workCost.isNotEmpty()) {
            repository.addWork(WorkInfo(date, workName, workDescription, workCost, checkedStatus, currentCarId))
                    .subscribe {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
        } else {
            Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show()
        }
    }
}