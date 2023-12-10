package team1XuongMobile.fpoly.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import team1XuongMobile.fpoly.myapplication.databinding.FragmentChatGptBinding;
import team1XuongMobile.fpoly.myapplication.models.Message;
import team1XuongMobile.fpoly.myapplication.adapter.MessageAdapter;

public class ChatGptFragment extends Fragment {

    private FragmentChatGptBinding binding;
    private List<Message> messageList;
    private MessageAdapter adapter;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatGptBinding.inflate(inflater, container, false);

        messageList = new ArrayList<>();

        adapter = new MessageAdapter(messageList);
        binding.rcvChat.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(true);
        binding.rcvChat.setLayoutManager(llm);

        client = new OkHttpClient();

        listener();

        return binding.getRoot();
    }

    private void listener() {
        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = binding.edtCauhoi.getText().toString().trim();
                addToChat(question, Message.SEND_BY_ME);
                binding.edtCauhoi.setText("");
                callAPI(question);
            }
        });
    }

    private void addToChat(String message, String sentBy) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sentBy));
                adapter.notifyDataSetChanged();
                binding.rcvChat.smoothScrollToPosition(adapter.getItemCount());
            }
        });
    }

    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SEND_BY_BOT);
    }


// ...

    void callAPI(String question) {
        //okhttp
        messageList.add(new Message("Typing... ", Message.SEND_BY_BOT));

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo");

            JSONArray messageArr = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("role", "user");
            obj.put("content", question);
            messageArr.put(obj);

            jsonBody.put("messages", messageArr);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("\n" +
                        "https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer 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") // key moi
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    addResponse("Failed to load response due to " + response.body().toString());
                }
            }
        });


    }


}
