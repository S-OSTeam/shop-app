package com.example.deamhome

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo3.ApolloClient
import com.example.deamhome.model.TestMutation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_main).setOnClickListener {
            GlobalScope.launch {
                val response =
                    ApolloClient.Builder().serverUrl(BuildConfig.SERVER_URL)
                        .build()
                        .mutation(TestMutation()).execute()
                runOnUiThread {
                    if (!response.hasErrors()) {
                        findViewById<TextView>(R.id.tv_main).text = response.data?.test
                    }
                }
            }
        }
    }
}
