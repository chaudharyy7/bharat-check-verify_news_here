package com.example.bharatcheck.GeminiApi

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object GeminiHelper {
    private val client = OkHttpClient()
    private const val apiKey = "AIzaSyC2Cc0_FfiIdd1yGcxf8izEWHIIqNLpl60"

    private fun buildPrompt(userQuery: String, matchingArticles: String?, mode: String): String {
        if (mode == "textVerify"){
            return """
            🧠 You are an advanced **News Verification AI Assistant**.

            📌 Objective:
            Your task is to verify the **truthfulness and trust level** of a given news headline or claim provided by the user.

            🔍 Use the real-time articles below to fact-check and verify:
            ${matchingArticles ?: "No related articles found in recent news."}

            🔎 Guidelines:
            1. If the news has been confirmed by **trusted sources** like:
               - 🏛️ Government portals (PIB, india.gov.in)
               - 📰 Trusted Indian media: NDTV, Times of India, ANI, etc.
               - 🌍 Global media: Reuters, BBC, WHO, etc.
               - Full news full explanation and justification and source.

               ➤ Then mark:
               🟢 TRUE — Explain why & mention the news source(s)

            2. If it was debunked by fact-checkers like:
               - Alt News, BOOM, PIB Fact Check, Google Fact Check Explorer

               ➤ Then mark:
               🔴 FALSE — Explain who debunked and why.

            3. If insufficient proof or not yet reported:
               ➤ Then mark:
               🟡 UNVERIFIED — Explain it needs official confirmation.

            ❗If user enter any link or clicks link. Don't accept it and say to user to enter only news in text formate which will be related to news or articles..

            📝 Claim to verify:
            "$userQuery"
        """.trimIndent()
        }else{
            return """
            🧠 You are an advanced **News Verification AI Assistant**.

            📌 Objective:
            Your task is to verify the **truthfulness and trust level** of a given news headline or claim provided by the user.

            🔍 Use the real-time articles below to fact-check and verify:
            ${matchingArticles ?: "No related articles found in recent news."}

            🔎 Guidelines:
            1. If the news has been confirmed by **trusted sources** like:
               - 🏛️ Government portals (PIB, india.gov.in)
               - 📰 Trusted Indian media: NDTV, Times of India, ANI, etc.
               - 🌍 Global media: Reuters, BBC, WHO, etc.
               - Full news full explanation and justification and source.

               ➤ Then mark:
               🟢 TRUE — Explain why & mention the news source(s)

            2. If it was debunked by fact-checkers like:
               - Alt News, BOOM, PIB Fact Check, Google Fact Check Explorer

               ➤ Then mark:
               🔴 FALSE — Explain who debunked and why.

            3. If insufficient proof or not yet reported:
               ➤ Then mark:
               🟡 UNVERIFIED — Explain it needs official confirmation.

            ❗If user enter any text or news in text form. Don't accept it and say to user to enter only website links which will be related to news or articles.

            📝 Claim to verify:
            "$userQuery"
        """.trimIndent()
        }
    }

    fun getNewsVerification(
        userQuery: String,
        matchingArticles: String?,
        mode: String,
        onResult: (String) -> Unit
    ) {
        val prompt = buildPrompt(userQuery, matchingArticles,mode)

        val jsonBody = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().put(JSONObject().put("text", prompt)))
                })
            })
        }

        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            jsonBody.toString()
        )

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult("❌ Network error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (body.isNullOrEmpty()) {
                    onResult("❌ Empty response from Gemini.")
                    return
                }

                try {
                    val json = JSONObject(body)
                    if (json.has("error")) {
                        onResult("⚠️ API Error: ${json.getJSONObject("error").getString("message")}")
                        return
                    }

                    val result = json
                        .getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")

                    onResult(result.trim())
                } catch (e: Exception) {
                    onResult("❌ Failed to parse response: ${e.message}")
                }
            }
        })
    }
    fun verifyImage(imageBytes: ByteArray, onResult: (String) -> Unit) {
        val base64Image = android.util.Base64.encodeToString(imageBytes, android.util.Base64.NO_WRAP)

        val jsonBody = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("inline_data", JSONObject().apply {
                                put("mime_type", "image/jpeg")
                                put("data", base64Image)
                            })
                        })
                        put(JSONObject().put("text", """
                        🧠 You are an advanced **News Verification AI Assistant**.
                        
                        📸 The user has uploaded an image. Analyze it and determine:
                        1. If it's from a real event, identify the context (date, location, incident).
                        2. Whether it has been used in misinformation before.
                        3. Detect signs of image tampering, AI generation, or photoshop edits.
                        4. Provide a reliable conclusion with justification.
                        
                        ⚠️ If the image lacks verifiable news context, clearly mark it as:
                        🟡 UNVERIFIED — explain the reason and what’s missing.
                        
                        Output with:
                        🟢 TRUE / 🔴 FALSE / 🟡 UNVERIFIED
                        — along with explanation and any known references.
                    """.trimIndent()))
                    })
                })
            })
        }

        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            jsonBody.toString()
        )

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro-vision:generateContent?key=$apiKey")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult("❌ Network error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (body.isNullOrEmpty()) {
                    onResult("❌ Empty response from Gemini.")
                    return
                }

                try {
                    val json = JSONObject(body)
                    val result = json
                        .getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")

                    onResult(result.trim())
                } catch (e: Exception) {
                    onResult("❌ Failed to parse response: ${e.message}")
                }
            }
        })
    }

}
