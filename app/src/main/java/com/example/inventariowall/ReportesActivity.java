package com.example.inventariowall;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReportesActivity extends AppCompatActivity {
    private EditText editTextReportId, editTextDescription;
    private TextView textViewResult;
    private FirebaseFirestore db;
    private TextView textViewLatestReportContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        editTextReportId = findViewById(R.id.editTextReportId);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewResult = findViewById(R.id.textViewResult);
        Button buttonAddReport = findViewById(R.id.buttonAddReport);
        Button buttonUpdateReport = findViewById(R.id.buttonUpdateReport);
        Button buttonDeleteReport = findViewById(R.id.buttonDeleteReport);

        db = FirebaseFirestore.getInstance();
        textViewLatestReportContent = findViewById(R.id.textViewLatestReportContent);


        buttonAddReport.setOnClickListener(v -> addReport());
        buttonUpdateReport.setOnClickListener(v -> updateReport());
        buttonDeleteReport.setOnClickListener(v -> deleteReport());
    }

    private void addReport() {
        String id = editTextReportId.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (!id.isEmpty() && !description.isEmpty()) {
            Map<String, Object> report = new HashMap<>();
            report.put("description", description);

            db.collection("reports").document(id).set(report)
                    .addOnSuccessListener(aVoid -> {
                        textViewResult.setText("Reporte agregado exitosamente");
                        limpiarCampos();
                        mostrarUltimoReporte(id, description); // Muestra el reporte agregado
                    })
                    .addOnFailureListener(e -> textViewResult.setText("Error al agregar el reporte: " + e.getMessage()));
        } else {
            textViewResult.setText("Complete todos los campos");
        }
    }


    private void updateReport() {
        String id = editTextReportId.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (!id.isEmpty() && !description.isEmpty()) {
            DocumentReference reportRef = db.collection("reports").document(id);
            reportRef.update("description", description)
                    .addOnSuccessListener(aVoid -> textViewResult.setText("Reporte actualizado"))
                    .addOnFailureListener(e -> textViewResult.setText("Error al actualizar: " + e.getMessage()));
            limpiarCampos();

        } else {
            textViewResult.setText("Complete todos los campos");
        }
    }

    private void deleteReport() {
        String id = editTextReportId.getText().toString().trim();

        if (!id.isEmpty()) {
            db.collection("reports").document(id).delete()
                    .addOnSuccessListener(aVoid -> textViewResult.setText("Reporte eliminado"))
                    .addOnFailureListener(e -> textViewResult.setText("Error al eliminar: " + e.getMessage()));
            limpiarCampos();
        } else {
            textViewResult.setText("Ingrese el ID del reporte");
        }
    }
    private void limpiarCampos() {
        editTextReportId.setText("");
        editTextDescription.setText("");
    }
    private void mostrarUltimoReporte(String id, String description) {
        String contenido = "ID: " + id + "\nDescripción: " + description;
        textViewLatestReportContent.setText(contenido);
    }


}