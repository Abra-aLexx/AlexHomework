package com.abra.homework_5

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.Toolbar
import java.text.SimpleDateFormat
import java.util.*

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
    private var checkedStatus = "pending"
    private lateinit var date: String
    private lateinit var currentCarInfo: CarInfo

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
        if (intent != null) {
            currentCarInfo = intent.getParcelableExtra("currentCarInfo")!!
        }
        setSupportActionBar(toolbar)
        setImageListeners()
        setButtonsListeners()
        val simpleDateFormat = SimpleDateFormat.getDateInstance()
        date = simpleDateFormat.format(Date())
        textData.text = "${resources.getString(R.string.application_date)} - $date"
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
            showActivity(true)
        }
        imgButtonApply.setOnClickListener {
            showActivity(false)
        }
    }

    private fun showActivity(isButtonBack: Boolean) {
        val intent = Intent()
        if (!isButtonBack) {
            val workName = etWorkName.text.toString()
            val workDescription = etWorkDescription.text.toString()
            val workCost = etWorkCost.text.toString()
            if (workName.isNotEmpty() && workDescription.isNotEmpty() && workCost.isNotEmpty()) {
                intent.putExtra("workInfo", WorkInfo(date, workName, workDescription, workCost, checkedStatus))
                intent.putExtra("isButtonBack", isButtonBack)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show()
            }
        } else {
            intent.putExtra("isButtonBack", isButtonBack)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}