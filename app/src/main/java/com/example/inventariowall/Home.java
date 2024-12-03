package com.example.inventariowall;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity {
    TextView mNombre;
    ImageView reporte, perfil, inventario, logout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        mNombre = findViewById(R.id.nombre);
        reporte = findViewById(R.id.reporte);
        perfil = findViewById(R.id.perfil);
        inventario = findViewById(R.id.inventario);
        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(Home.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Asegurar que el usuario no pueda regresar a la actividad de perfil
                startActivity(intent);
                finish();
            }
        });


        reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ReportesActivity.class);
                startActivity(intent);
            }
        });
        cargarNombreUsuario();
    }
    private void cargarNombreUsuario() {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();
            db.collection("Empleados").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String nombre = document.getString("nombre");
                                mNombre.setText(nombre);
                            } else {
                                Toast.makeText(Home.this, "No se encontró el usuario", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Home.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(Home.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            // Redirigir al Login si es necesario
            startActivity(new Intent(Home.this, MainActivity.class));
            finish();
        }
    }
}