package com.example.coinify_2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coinify_2.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // تهيئة FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // التعامل مع زر تسجيل الدخول
        binding.btnlogin.setOnClickListener {
            val email = binding.editemaillog.text.toString().trim()  // إزالة المسافات الإضافية
            val password = binding.editpasswordlog.text.toString().trim()

            // التحقق من صحة المدخلات
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // تسجيل الدخول باستخدام Firebase
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // إذا كان تسجيل الدخول ناجحًا
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // إنهاء نشاط تسجيل الدخول
                    } else {
                        // إذا فشل تسجيل الدخول، عرض رسالة توست
                        Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // التعامل مع زر الانتقال إلى صفحة التسجيل
        binding.textsignup.setOnClickListener {
            val signupIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signupIntent)
        }

        // التعامل مع نظام الحواف (Edge Insets)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
