package com.example.inventariowall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // Este import es necesario
import android.app.AlertDialog; // Opcional, para mostrar detalles
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class InstalacionAdapter extends RecyclerView.Adapter<InstalacionAdapter.InstalacionViewHolder> {
    private List<Instalacion> listaInstalaciones;
    private Context context;
    private OnInstalacionActionListener listener;



    public interface OnInstalacionActionListener {
        void onEditarInstalacion(Instalacion instalacion);
        void onEliminarInstalacion(Instalacion instalacion);
    }


    public InstalacionAdapter(List<Instalacion> listaInstalaciones, Context context) {
        this.listaInstalaciones = listaInstalaciones;
        this.context = context;
    }

    @NonNull
    @Override
    public InstalacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_instalacion, parent, false);
        return new InstalacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstalacionViewHolder holder, int position) {
        Instalacion instalacion = listaInstalaciones.get(position);

        holder.tvCliente.setText(instalacion.getCliente());
        holder.tvDireccion.setText(instalacion.getDireccion());
        holder.tvFecha.setText(instalacion.getFecha());

        // Menú contextual para editar o eliminar
        holder.itemView.setOnLongClickListener(v -> {
            mostrarMenuOpciones(instalacion);
            return true;
        });

        // Mostrar detalles al hacer click corto
        holder.itemView.setOnClickListener(v -> {
            mostrarDetallesInstalacion(instalacion);
        });
    }
    private void mostrarMenuOpciones(Instalacion instalacion) {
        String[] opciones = {"Editar", "Eliminar"};
        new AlertDialog.Builder(context)
                .setTitle("Opciones")
                .setItems(opciones, (dialog, which) -> {
                    switch (which) {
                        case 0: // Editar
                            listener.onEditarInstalacion(instalacion);
                            break;
                        case 1: // Eliminar
                            new AlertDialog.Builder(context)
                                    .setTitle("Confirmar Eliminación")
                                    .setMessage("¿Estás seguro de eliminar esta instalación?")
                                    .setPositiveButton("Sí", (d, w) -> listener.onEliminarInstalacion(instalacion))
                                    .setNegativeButton("No", null)
                                    .create()
                                    .show();
                            break;
                    }
                })
                .create()
                .show();
    }

    @Override
    public int getItemCount() {
        return listaInstalaciones.size();
    }

    static class InstalacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvCliente, tvDireccion, tvFecha;

        public InstalacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvFecha = itemView.findViewById(R.id.tvFecha);
        }
    }

    private void mostrarDetallesInstalacion(Instalacion instalacion) {
        // Implementación de ejemplo para mostrar detalles
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles de Instalación")
                .setMessage(
                        "Cliente: " + instalacion.getCliente() + "\n" +
                                "Dirección: " + instalacion.getDireccion() + "\n" +
                                "Fecha: " + instalacion.getFecha() + "\n" +
                                "Estado: " + instalacion.getEstado() + "\n" +
                                "Observaciones: " + instalacion.getObservaciones()
                )
                .setPositiveButton("Cerrar", null)
                .create()
                .show();
    }
}