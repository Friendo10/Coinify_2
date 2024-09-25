package com.example.coinify_2

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var coinCountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // إعداد Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        // مرجع لعنصر عرض العملات
        coinCountTextView = findViewById(R.id.coin_count_text)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // إعداد الـ Fragments
        val homeFragment = HomeFragment()
        val tasksFragment = TasksFragment()
        val storeFragment = StoreFragment()
        val profileFragment = ProfileFragment()

        // تحميل الـ Fragment الأولي
        loadFragment(homeFragment)

        // إعداد مستمع للتنقل
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    showCoinBar()  // إظهار شريط العملات
                    loadFragment(homeFragment)
                    true
                }
                R.id.nav_tasks -> {
                    showCoinBar()  // إظهار شريط العملات
                    loadFragment(tasksFragment)
                    true
                }
                R.id.nav_store -> {
                    showCoinBar()  // إظهار شريط العملات
                    loadFragment(storeFragment)
                    true
                }
                R.id.nav_profile -> {
                    showCoinBar()  // إخفاء شريط العملات في صفحة البروفايل
                    loadFragment(profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // دالة لإظهار شريط العملات وتحديث عدد العملات
    private fun showCoinBar() {
        val coinBar = findViewById<LinearLayout>(R.id.coin_bar)
        coinBar.visibility = View.VISIBLE

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = database.child(userId)

            userRef.child("coins").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val coinCount = snapshot.getValue(Int::class.java) ?: 100
                    coinCountTextView.text = coinCount.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    coinCountTextView.text = "Error"
                }
            })
        }
    }
}
