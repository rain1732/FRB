package com.example.frbpro.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.frbpro.LoginActivity;
import com.example.frbpro.R;
import com.example.frbpro.application;
import com.example.frbpro.databinding.FragmentHomeBinding;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    ArrayList<String> strings = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        application a = (application) getActivity().getApplication();

        ArrayList<TextView> textViews = new ArrayList<>();

        TextView textView1 = (TextView) root.findViewById(R.id.data1);
        TextView textView2 = (TextView) root.findViewById(R.id.data2);
        TextView textView3 = (TextView) root.findViewById(R.id.data3);
        TextView textView4 = (TextView) root.findViewById(R.id.data4);
        TextView textView5 = (TextView) root.findViewById(R.id.data5);
        TextView textView6 = (TextView) root.findViewById(R.id.data_analyze);
        application app = (application) getActivity().getApplication();

        //对前五个数据进行解析
        //得到结果
        //简单的判断即可
        //TODO

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s1 = LoginActivity.sendPost("http://10.192.81.122:8080/user/get",
                                "userName=" + app.getId() +"&ill="+app.getIll() + "&i=" + "1");
                        textView1.setText(s1);
                    }
                }).start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s1 = LoginActivity.sendPost("http://10.192.81.122:8080/user/get",
                                "userName=" + app.getId() +"&ill="+app.getIll() + "&i=" + "2");
                        textView2.setText(s1);
                    }
                }).start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s1 = LoginActivity.sendPost("http://10.192.81.122:8080/user/get",
                                "userName=" + app.getId() +"&ill="+app.getIll() + "&i=" + "3");
                        textView3.setText(s1);
                    }
                }).start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s1 = LoginActivity.sendPost("http://10.192.81.122:8080/user/get",
                                "userName=" + app.getId() +"&ill="+app.getIll() + "&i=" + "4");
                        textView4.setText(s1);
                    }
                }).start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s1 = LoginActivity.sendPost("http://10.192.81.122:8080/user/get",
                                "userName=" + app.getId() +"&ill="+app.getIll() + "&i=" + "5");
                        textView5.setText(s1);
                    }
                }).start();
                //textView1.setText("这是一个测试1");
                /*textView1.setText(strings.get(4));
                textView2.setText(strings.get(3));
                textView3.setText(strings.get(2));
                textView4.setText(strings.get(1));*/

                textView6.setText("这是全新的数据分析");
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}