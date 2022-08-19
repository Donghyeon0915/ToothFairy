package com.example.toothfairy.model.repository

import com.example.toothfairy.dto.LoginDto
import com.example.toothfairy.entity.DailyWearTime
import com.example.toothfairy.entity.Patient
import com.example.toothfairy.model.RetrofitClient
import com.example.toothfairy.model.service.PatientService
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.sql.Date

object PatientRepository {
    // VARIABLE
    private val patientService: PatientService

    // 환자 정보 가져오는 메소드
    fun loadPatient(id: String?): Response<Patient?>? {
        val patientCall: Call<Patient?>? = patientService.findByPatientNum(id)

        return try {
            patientCall?.execute()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun login(loginDto: LoginDto?): Response<Patient?>? {
        val patientCall: Call<Patient?>? = patientService.login(loginDto)

        return try {
            patientCall?.execute()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // JSON 데이터를 DailyWearTime으로 변환
    @Throws(JSONException::class)
    private fun jsonToDailyWearTimeList(jsonArray: JSONArray): List<DailyWearTime> {
        val dailyWearTimeList: MutableList<DailyWearTime> = ArrayList()

        for (i in 0 until jsonArray.length()) {
            val data:JSONObject = jsonArray.getJSONObject(i)

            dailyWearTimeList.add(
                DailyWearTime(id = data.getInt("id"),
                              patientNum = data.getString("patientNum"),
                              wearDate = Date.valueOf(data.getString("wearDate")),
                              totalWearTime = data.getLong("totalWearTime"))

            )
        }
        return dailyWearTimeList
    }

//    companion object {
//        var instance = PatientRepository() // 싱글턴 패턴
//    }

    init {
        val retrofit = RetrofitClient.instance
        patientService = retrofit!!.create(PatientService::class.java)
    }
}