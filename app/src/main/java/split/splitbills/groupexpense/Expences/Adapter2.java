package split.splitbills.groupexpense.Expences;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.List;
import java.util.logging.Handler;

import split.splitbills.groupexpense.DATA.Member;
import split.splitbills.groupexpense.R;

import static android.content.Context.MODE_PRIVATE;


public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder> {
    private List<Member> model;
    private Context context;


    public Adapter2(List<Member> model, Context context) {
        this.model = model;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.box, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.TEXT.setText(model.get(position).getName());
        holder.Num.setText(model.get(position).getNUMBER());
//        holder.Image.setImageURI((model.get(position).getUri()));
//        if(model.get(position).getUri()!=null)
//        {
            Glide.with(holder.Image.getContext())
                    .load((model.get(position).getUri()))
                    .error(R.drawable.person)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.Image);
//        }

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView TEXT;
        ImageView Image;
        TextView Num;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    edit(model.get(getAdapterPosition()));
                    return true;
                }
            });

            TEXT = itemView.findViewById(R.id.EditName);
            Image = itemView.findViewById(R.id.profile);
            Num = itemView.findViewById(R.id.phonenumber);
        }


        private void edit(Member ITEM) {

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.add_member);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            Button saveButton;
            final EditText name;
            TextView title;
            name = dialog.findViewById(R.id.name);
            saveButton = dialog.findViewById(R.id.saveButton);
            saveButton.setText(R.string.update_text);
            title = dialog.findViewById(R.id.title);
            title.setText(R.string.edit_NAME);
            name.setText(ITEM.getName());
            saveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ITEM.setName(name.getText().toString().toUpperCase());

                    if (!name.getText().toString().trim().isEmpty()) {
                        String NAME = name.getText().toString().toUpperCase();
                        model.get(getAdapterPosition()).setName(NAME);
                        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(model);
                        editor.putString("task list", json);
                        editor.apply();
                        dialog.dismiss();
                        notifyItemChanged(getAdapterPosition(), ITEM); //important!
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(view, "Empty Field Not Allowed", Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                    dialog.dismiss();

                }
            });
            dialog.show();
        }
    }
}
