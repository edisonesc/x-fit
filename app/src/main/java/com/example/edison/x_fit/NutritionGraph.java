package com.example.edison.x_fit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;

import devlight.io.library.ArcProgressStackView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NutritionGraph.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NutritionGraph#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NutritionGraph extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    PieChart pieChart = null;
    ArcProgressStackView arcProgressStackView = null;
    private OnFragmentInteractionListener mListener;
    private int mCounter = 0;
    public NutritionGraph() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NutritionGraph.
     */
    // TODO: Rename and change types and number of parameters
    public static NutritionGraph newInstance(String param1, String param2) {
        NutritionGraph fragment = new NutritionGraph();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);




        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View rootView =  inflater.inflate(R.layout.fragment_nutrition_graph, container, false);

        pieChart = rootView.findViewById(R.id.pieChart);
        arcProgressStackView = rootView.findViewById(R.id.apsv);
        Legend legend = pieChart.getLegend();
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(45f);
        pieChart.setHoleColor(Color.parseColor("#0b0c0d"));
        pieChart.setTransparentCircleRadius(61f);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
        models.add(new ArcProgressStackView.Model("Protein", 70, Color.BLACK, Color.parseColor("#00695c")));
        models.add(new ArcProgressStackView.Model("Carbs", 30, Color.BLACK, Color.parseColor("#005662")));
        models.add(new ArcProgressStackView.Model("Carbs", 30, Color.BLACK, Color.parseColor("#006db3")));
        models.add(new ArcProgressStackView.Model("Overall", 35, Color.BLACK, Color.parseColor("#000a12")));

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0F, 105.0F);
        valueAnimator.setDuration(800);
        valueAnimator.setStartDelay(200);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.start();


        arcProgressStackView.setModels(models);

        ArrayList<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry(34f, "Carbohydrates"));
        values.add(new PieEntry(70f, "Protein"));
        values.add(new PieEntry(42f, "Fats"));
        values.add(new PieEntry(34f, "Misc"));

        pieChart.animateY(3000, Easing.EasingOption.EaseInCubic);
        PieDataSet dataSet = new PieDataSet(values, "Daily Intake") ;
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(Color.parseColor("#29434e"),
                Color.parseColor("#212121"),
                Color.parseColor("#263238"),
                Color.parseColor("#37474f")
                );
        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
        legend.setTextColor(Color.WHITE);
        legend.setTypeface(EasyFonts.caviarDreams(getContext()));


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
//        pieChart.animateY(1000, Easing.EasingOption.EaseInCubic);
        Toast.makeText(getContext(), "Resumed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getContext(), "Start", Toast.LENGTH_SHORT).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
