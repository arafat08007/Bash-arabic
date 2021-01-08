package app.bash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import app.bash.R;
import app.bash.model.AnswerModel;
import app.bash.model.QuestionModel;
import app.bash.utilitis.S;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
    Context context;
    ArrayList<AnswerModel> answerModels;
    QuestionModel QuestionModel;

    public AnswerAdapter(Context context, ArrayList<AnswerModel> answerModels, QuestionModel questionModel){
        S.E("ADP courseModels.size() :: "+ answerModels.size());
        S.E("ADP examModel.getExamId() :: "+ questionModel.getExamId());
        this.context=context;
        this.answerModels = answerModels;
        this.QuestionModel = questionModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_question, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
       // holder.txtChildName.setText(courseModels.get(position).getClassName());
        S.E("getClassName::"+ answerModels.get(position).getClassName());
        holder.txtChildName.setText(answerModels.get(position).getClassName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /// CheckTest(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return answerModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtChildName;
        public ViewHolder( View itemView) {
            super(itemView);
            txtChildName = itemView.findViewById(R.id.txtChildName);

        }
    }
}

