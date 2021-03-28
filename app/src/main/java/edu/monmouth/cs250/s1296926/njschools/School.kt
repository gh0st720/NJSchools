package edu.monmouth.cs250.s1296926.njschools

import android.content.Context
import org.json.JSONArray
import org.json.JSONException

class School  (val schoolID: Int, val name: String, val address: String, val category: String, val lat: Double, val long: Double) {
    companion object {
        fun getSchoolsFromFile ( filename: String,  context: Context): MutableList<School> {
            val schools = mutableListOf<School>()

            try {
                // Load data
                val jsonString = loadJsonFromAsset(filename, context)
                if (jsonString != null) {
                    val schoolArray = JSONArray(jsonString)

                    // Get Student objects from data
                    (0 until schoolArray.length()).mapTo(schools) {
                        School(
                            schoolArray.getJSONObject(it).getInt("OBJECTID"),
                            schoolArray.getJSONObject(it).getString("SCHOOLNAME"),
                            schoolArray.getJSONObject(it).getString("ADDRESS1"),
                            schoolArray.getJSONObject(it).getString("CATEGORY"),
                            schoolArray.getJSONObject(it).getDouble("coordinates[0]"),
                                schoolArray.getJSONObject(it).getDouble("coordinates[1]")

                        )
                    }
                    print ("parsed data")

                } else {
                    println("not a valid JSON string")
                }


            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return schools
        }

        // open file and read all characters into a buffer. Convert buffer to String

        private fun loadJsonFromAsset(filename: String, context: Context): String? {
            var json: String?


            try {
                val inputStream = context.assets.open(filename)
                val size = inputStream.available()
                val buffer = ByteArray(size)

                inputStream.read(buffer)
                inputStream.close()
                val charset = Charsets.UTF_8

                json = buffer.toString(charset)


            } catch (ex: java.io.IOException) {
                ex.printStackTrace()
                return null
            }

            return json
        }
    }

}