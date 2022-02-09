package split.splitbills.groupexpense.Expences;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import split.splitbills.groupexpense.DATA.MODEL;
import split.splitbills.groupexpense.R;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.String.format;

public class ADAPTER extends RecyclerView.Adapter<ADAPTER.ViewHolder> {

    private List<MODEL> MODEL;
    private Context context;
    private String currency;


    public ADAPTER(List<MODEL> model, Context context) {
        this.MODEL = model;
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        currency = sharedPreferences.getString("currency", null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expencebox, parent, false);
        return new ViewHolder(view, context);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(format("TITLE :%s", MODEL.get(position).getTitle()));
        holder.name.setText(format("PAYER :%s", MODEL.get(position).getName()));
        holder.Date.setText(format("DATE :%s" + " ", MODEL.get(position).getDate()));
        holder.Number.setText(String.format((currency + " " + MODEL.get(position).getMoney())));
    }

    @Override
    public int getItemCount() {
        return MODEL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView name;
        TextView Date;
        TextView Number;
        ImageButton delete;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            title = itemView.findViewById(R.id.TITLE);
            name = itemView.findViewById(R.id.Payer);
            Date = itemView.findViewById(R.id.DATETIME);
            Number = itemView.findViewById(R.id.paisa);
            delete = itemView.findViewById(R.id.delete_button);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position;
            position = getAdapterPosition();
            MODEL ITEM = MODEL.get(position);
            switch (v.getId()) {
                case R.id.delete_button:

                    delete(ITEM);
                    break;
            }
        }

        private void delete(split.splitbills.groupexpense.DATA.MODEL item) {

            TextView text;
            final Dialog dialog = new Dialog(context);

            dialog.setContentView(R.layout.confirmation_pop);

            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            text = dialog.findViewById(R.id.title);
            text.setText("Are You Sure to delete?");

            ((TextView) dialog.findViewById(R.id.conf_no_button)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            ((TextView) dialog.findViewById(R.id.conf_yes_button)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    MODEL.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(MODEL);
                    editor.putString("list", json);
                    editor.apply();
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }
}

