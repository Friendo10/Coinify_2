package com.example.coinify_2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coinify_2.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ربط الـ View Binding
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // تهيئة Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // تهيئة قاعدة البيانات
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        // زر التسجيل
        binding.btnsignup.setOnClickListener {
            val username = binding.edituser.text.toString()
            val email = binding.editemial.text.toString()
            val password = binding.editpas.text.toString()
            val confirmPassword = binding.editRpas.text.toString()

            // التحقق من الحقول
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && username.isNotEmpty()) {
                if (password == confirmPassword) {

                    // إنشاء مستخدم جديد باستخدام Firebase Auth
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            // الحصول على ID المستخدم من Firebase Authentication
                            val userId = firebaseAuth.currentUser?.uid

                            // حفظ بيانات المستخدم في Firebase Realtime Database
                            if (userId != null) {
                                val userMap = mapOf(
                                    "username" to username,
                                    "email" to email,
                                    "password" to password,
                                    "coins" to 0 // تعيين عدد العملات الافتراضي إلى 0
                                )
                                databaseReference.child(userId).setValue(userMap).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                                        // الانتقال إلى صفحة تسجيل الدخول
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(this, "Failed to save user data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // زر الانتقال إلى صفحة تسجيل الدخول
        binding.textlogin.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }
}
