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
import com.example.frbpro.R;
import com.example.frbpro.application;
import com.example.frbpro.databinding.FragmentHomeBinding;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        application a =(application)getActivity().getApplication();

        ArrayList<TextView> textViews = new ArrayList<>();

        TextView textView1 = (TextView) root.findViewById(R.id.data1);
        TextView textView2 = (TextView) root.findViewById(R.id.data2);
        TextView textView3 = (TextView) root.findViewById(R.id.data3);
        TextView textView4 = (TextView) root.findViewById(R.id.data4);
        TextView textView5 = (TextView) root.findViewById(R.id.data5);
        TextView textView6 = (TextView) root.findViewById(R.id.data_analyze);
        //对前五个数据进行解析
        //得到结果
        //简单的判断即可
        //TODO

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView1.setText("这是一个测试1");
                textView1.setText(a.getIll());
                textView2.setText("这是一个测试2");
                textView3.setText("这是一个测试3");
                textView4.setText("这是一个测试4");
                textView5.setText("这是一个测试5");
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