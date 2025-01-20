package adaptadores;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import POJO.contactos;
import com.example.proyecto.R;

public class adaptadorcontacto extends RecyclerView.Adapter<adaptadorcontacto.ViewHolder> {
    private Context context;
    private ArrayList<contactos> listaContactos;
    private OnContactoClickListener contactoClickListener;

    public interface OnContactoClickListener {
        void onContactoClick(contactos contacto);
    }

    public adaptadorcontacto(Context context, ArrayList<contactos> listaContactos, OnContactoClickListener contactoClickListener) {
        this.context = context;
        this.listaContactos = listaContactos;
        this.contactoClickListener = contactoClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contacto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        contactos contacto = listaContactos.get(position);
        holder.txtNombre.setText(contacto.getNombre());
        holder.tvTelefonoContacto.setText(contacto.getTelefono());
        holder.btnLlamar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + contacto.getTelefono()));
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 10);
                return;
            }
            context.startActivity(intent);
        });
        holder.itemView.setOnClickListener(v -> contactoClickListener.onContactoClick(contacto));
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, tvTelefonoContacto;
        Button btnLlamar;
        ImageView imgContacto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreVer);
            tvTelefonoContacto = itemView.findViewById(R.id.tvTelefonoContacto);
            btnLlamar = itemView.findViewById(R.id.btnLlamar);
            imgContacto = itemView.findViewById(R.id.imgContacto);
        }
    }
}
