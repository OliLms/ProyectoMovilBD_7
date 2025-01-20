package adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import POJO.contactos;
import com.example.proyecto.R;

public class EliminarcontactosAdaptador extends RecyclerView.Adapter<EliminarcontactosAdaptador.ViewHolder> {
    private Context context;
    private ArrayList<contactos> listaContactos;
    private ArrayList<contactos> contactosSeleccionados;

    public EliminarcontactosAdaptador(Context context, ArrayList<contactos> listaContactos) {
        this.context = context;
        this.listaContactos = listaContactos;
        this.contactosSeleccionados = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contacto_eliminar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final contactos contacto = listaContactos.get(position);
        holder.tvNombre.setText(contacto.getNombre());
        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setChecked(contactosSeleccionados.contains(contacto));
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                contactosSeleccionados.add(contacto);
            } else {
                contactosSeleccionados.remove(contacto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public ArrayList<contactos> obtenerContactosSeleccionados() {
        return contactosSeleccionados;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.txtNombre);
            checkBox = itemView.findViewById(R.id.checkbox_contacto);
        }
    }
}
