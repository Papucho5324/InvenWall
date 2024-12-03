package com.example.inventariowall;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    Button registro;
    EditText email, password, passwordConfirm, nombre;
    TextView login;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        registro = findViewById(R.id.registrar);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        login = findViewById(R.id.login);
        nombre = findViewById(R.id.nombre);

        registro.setOnClickListener(v -> {
            registrar();
        });
    }

    private void registrar() {
        String correo = email.getText().toString().trim();
        String contraseña = password.getText().toString().trim();
        String contraseñaConfirmacion = passwordConfirm.getText().toString().trim();
        String nombreUsuario = nombre.getText().toString().trim();

        // Validación de campos vacíos
        if (correo.isEmpty() || contraseña.isEmpty() || contraseñaConfirmacion.isEmpty() || nombreUsuario.isEmpty()) {
            if (correo.isEmpty()) {
                email.setError("Ingrese su correo");
            }
            if (contraseña.isEmpty()) {
                password.setError("Ingrese su contraseña");
            }
            if (contraseñaConfirmacion.isEmpty()) {
                passwordConfirm.setError("Confirme su contraseña");
            }
            if (nombreUsuario.isEmpty()) {
                nombre.setError("Ingrese su nombre de usuario");
            }
            return; // Detener ejecución si algún campo está vacío
        }

        // Validación de formato de correo
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            email.setError("Correo inválido");
            return;
        }

        // Validación de contraseñas coincidentes
        if (!contraseña.equals(contraseñaConfirmacion)) {
            password.setError("Las contraseñas no coinciden");
            passwordConfirm.setError("Las contraseñas no coinciden");
            return;
        }

        // Validación de longitud de la contraseña (opcional)
        if (contraseña.length() < 6) {
            password.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        // Crear el usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // El usuario ha sido creado con éxito
                            FirebaseUser usuario = auth.getCurrentUser();

                            // Crear un objeto con los datos del usuario
                            Map<String, Object> usuarioData = new HashMap<>();
                            usuarioData.put("nombre", nombreUsuario);
                            usuarioData.put("correo", correo);

                            // Almacenar los datos en Firestore
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("Empleados")
                                    .document(usuario.getUid()) // Guardar el documento con el UID del usuario
                                    .set(usuarioData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                        finish(); // Opcional: Cierra la actividad de registro
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(Registro.this, "Error al guardar los datos en Firestore", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(Registro.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
