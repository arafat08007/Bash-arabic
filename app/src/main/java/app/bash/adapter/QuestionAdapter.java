package app.bash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.bash.R;
import app.bash.model.QuestionModel;
import app.bash.model.AnswerModel;
import app.bash.utilitis.S;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    Context context;
    ArrayList<QuestionModel> questionModels;
    ArrayList<AnswerModel> answerModels;

    public QuestionAdapter(Context context, ArrayList<QuestionModel> questionModels, ArrayList<AnswerModel> answerModels) {
        this.context = context;
        this.questionModels = questionModels;
        this.answerModels = answerModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_questionsub, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        /* holder.title.setText(examModels.get(position).getExamTitle());
       if (examModels.get(position).getExamDate().contains("T")) {
            holder.txtDate.setText("Date " + changeDateFormat(examModels.get(position).getExamDate().split("T")[0]));
        }
        holder.txtTime.setText("Time "+examModels.get(position).getFromTime()+"-"+examModels.get(position).getToTime());
*/
        holder.title.setText(questionModels.get(position).getExamTitle());

        if (questionModels.get(position).getOpenStatus()){
            holder.recyclerViewCourse.setVisibility(View.VISIBLE);
            holder.imgArrow.setRotation(360);
            holder.imgArrow.setBackground(context.getResources().getDrawable(R.drawable.btn_bg));
        }else {
            holder.recyclerViewCourse.setVisibility(View.GONE);
            holder.imgArrow.setRotation(270);
            holder.imgArrow.setBackground(context.getResources().getDrawable(R.drawable.btn_grey_bg));
          }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionModels.get(position).getOpenStatus()) {
                    holder.recyclerViewCourse.setVisibility(View.GONE);
                    S.E("status ::::: :: "+ questionModels.get(position).getOpenStatus());
                    questionModels.get(position).setOpenStatus(false);
                    notifyDataSetChanged();
                } else {

                    for (int j = 0; j < questionModels.size(); j++) {
                        if (!questionModels.get(position).getExamId().equals(questionModels.get(j).getExamId())) {
                            S.E("exam status : "+ questionModels.get(position).getOpenStatus());
                            questionModels.get(j).setOpenStatus(false);
                        }
                    }
                    notifyDataSetChanged();

                    ArrayList arrayListTemp = new ArrayList();
                    for (int i = 0; i < answerModels.size(); i++) {
                        if (answerModels.get(i).getExamId().equals(questionModels.get(position).getExamId())) {
                            arrayListTemp.add(answerModels.get(i));
                            S.E("course name :: "+ answerModels.get(i).getClassName());
                        }
                    }
                    S.E("arrayListTemp.size() :::: "+arrayListTemp.size());
                    if (arrayListTemp.size() > 0) {
                        holder.recyclerViewCourse.setVisibility(View.VISIBLE);
                        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        holder.recyclerViewCourse.setLayoutManager(horizontalLayoutManagaer);
                        AnswerAdapter examAdapter = new AnswerAdapter(context, arrayListTemp, questionModels.get(position));
                        holder.recyclerViewCourse.setAdapter(examAdapter);
                        questionModels.get(position).setOpenStatus(true);
                        notifyDataSetChanged();
                        S.E("arrayListTemp.size() :::: "+arrayListTemp.size());
                    } else {
                        holder.recyclerViewCourse.setVisibility(View.GONE);
                        S.T(context, "Courses not found.!");
                    }
                }





            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return questionModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, txtDuration, txtDate, txtTime;
        RecyclerView recyclerViewCourse;
        ImageView imgArrow;

        public ViewHolder( View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
           /* txtDuration = itemView.findViewById(R.id.txtDuration);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);*/
            recyclerViewCourse = itemView.findViewById(R.id.recyclerViewCourse);
            imgArrow = itemView.findViewById(R.id.imgArrow);
        }
    }
}

