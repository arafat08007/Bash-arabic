package app.bash.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.bash.R;
import app.bash.adapter.QuestionAdapter;
import app.bash.model.QuestionModel;
import app.bash.model.AnswerModel;
import app.bash.utilitis.ApiClient;
import app.bash.utilitis.ApiInterface;
import app.bash.utilitis.Helper;
import app.bash.utilitis.JSONParser;
import app.bash.utilitis.SavedData;
import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionFragment extends Fragment {
    ArrayList<AnswerModel> answerArrayList = new ArrayList<>();
    ArrayList<QuestionModel> questionArrayList = new ArrayList<>();
    @BindView(R.id.expandableListView)
    RecyclerView expandableListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, v);
        setList();
        return v;
    }

    private void setList() {

        //parent data

        /*for (int i = 0; i < 5; i++) {
            ExamModel questionModel = new ExamModel();
            questionModel.setExamId(i+"");
            questionModel.setExamTitle("");
            questionModel.setExamDate("");
            questionModel.setIsActive("");
            questionModel.setFromTime("");
            questionModel.setToTime("");
            questionModel.setExamDuration("");
            questionModel.setOpenStatus(false);
            examModelArrayList.add(questionModel);
            for (int j = 0; j < 3; j++) {
                CourseModel questionSubModel = new CourseModel();
                questionSubModel.setExamId(i+"");
                questionSubModel.setClassId("");
                questionSubModel.setClassName("");
                courseModelArrayList.add(questionSubModel);
            }
        }





        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        expandableListView.setLayoutManager(horizontalLayoutManagaer);
        ExamAdapter questionAdapter = new ExamAdapter(getActivity(), examModelArrayList, courseModelArrayList);
        expandableListView.setAdapter(questionAdapter);*/

        new JSONParser(getActivity()).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getQuestion(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject json = data.getJSONObject(i);
                            QuestionModel questionModel = new QuestionModel();
                            questionModel.setExamId(json.getString("id"));

                            AnswerModel questionSubModel = new AnswerModel();
                            questionSubModel.setExamId(json.getString("id"));
                            questionSubModel.setClassId("");


                            if (SavedData.getLanguage().equals("ar")) {
                                questionModel.setExamTitle(json.getString("arabic_question_name"));
                                questionSubModel.setClassName(json.getString("answer"));
                            }else {
                                questionModel.setExamTitle(json.getString("arabic_question_name"));
                                questionSubModel.setClassName(json.getString("answer"));
                            }

                            answerArrayList.add(questionSubModel);
                            questionArrayList.add(questionModel);
                        }

                        if (answerArrayList.size() > 0) {
                            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            expandableListView.setLayoutManager(horizontalLayoutManagaer);
                            QuestionAdapter questionAdapter = new QuestionAdapter(getActivity(), questionArrayList, answerArrayList);
                            expandableListView.setAdapter(questionAdapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
