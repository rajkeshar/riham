package com.mobiato.sfa.Fragments;


import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mobiato.sfa.App;
import com.mobiato.sfa.R;
import com.mobiato.sfa.databinding.FragmentFragmentTwoBinding;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.ReportBarData;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTwo extends Fragment {

    FragmentFragmentTwoBinding binding;
    private ArrayList<ReportBarData> arrBarDat = new ArrayList<>();
    private ArrayList<BarEntry> barEntry;
    private ArrayList<String> barEntryLabels;
    private BarDataSet Bardataset;
    private BarData barData;
    private DBManager db;
    private int CSDSale = 0, CSDSalePC = 0, CSDTarget = 0, biscutSale = 0, biscutSalePc = 0, biscutTarget = 0, waterSale = 0, waterSalePC = 0, waterTarget = 0,
            juiceSale = 0, juiceSalePc = 0, juiceTarget = 0,hamperTarget = 0,confectionaryTarget=0,confectionarySale=0;
    private int csdPer = 0, biscutPer = 0, waterPer = 0, juicePer = 0,hamperPer = 0,HamperSale = 0, HamperPC = 0,ConfectionaryPer=0,ConfectionaryPC=0;
    private Salesman mSalesman;
    private HashMap<String, String> CSDSMAP, biscutSMAP, juiceSMAP, waterSMAP,HamperSMAP,ConfectionarySMAP;

    public FragmentTwo() {
        // Required empty public constructor
    }

    public static FragmentTwo createInstance() {
        FragmentTwo partThreeFragment = new FragmentTwo();
        return partThreeFragment;
    }

    static FragmentManager fragmentManager;

    public static Fragment newInstance() {
        FragmentTwo fragment = new FragmentTwo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_fragment_two, container, false);
        View view = binding.getRoot();

        db = new DBManager(getActivity());
        mSalesman = Settings.getSalesmanData(App.SALESMAN);

        double todaySale = db.getTotalAmtSale();
        binding.tvGrossSales.setText("" + UtilApp.getNumberFormate(Math.round(todaySale)) + " UGX");

       // int totalQty = db.getTotalQtySale();
        HashMap<String, String> mHasMap = db.getTotalCategoryQtySale();
        binding.tvSales.setText("" + mHasMap.get("Case") + "/" + mHasMap.get("PCs"));


        //CSDSale = db.getTotalCategoryQtySale("2");
        HamperSMAP=db.getTotalCategoryPCSQtySale("5");
        CSDSMAP = db.getTotalCategoryPCSQtySale("2");
        //waterSale = db.getTotalCategoryQtySale("3");
        waterSMAP = db.getTotalCategoryPCSQtySale("3");
        //biscutSale = db.getTotalCategoryQtySale("1");
        biscutSMAP = db.getTotalCategoryPCSQtySale("1");
        juiceSMAP = db.getTotalCategoryPCSQtySale("4");
        ConfectionarySMAP = db.getTotalCategoryPCSQtySale("6");

        CSDSale = Integer.parseInt(CSDSMAP.get("Case"));
        CSDSalePC = Integer.parseInt(CSDSMAP.get("PCs"));

        HamperSale = Integer.parseInt(HamperSMAP.get("Case"));
        HamperPC = Integer.parseInt(HamperSMAP.get("PCs"));

        waterSale = Integer.parseInt(waterSMAP.get("Case"));
        waterSalePC = Integer.parseInt(waterSMAP.get("PCs"));

        biscutSale = Integer.parseInt(biscutSMAP.get("Case"));
        biscutSalePc = Integer.parseInt(biscutSMAP.get("PCs"));

        juiceSale = Integer.parseInt(juiceSMAP.get("Case"));
        juiceSalePc = Integer.parseInt(juiceSMAP.get("PCs"));

        confectionarySale= Integer.parseInt(ConfectionarySMAP.get("Case"));
        ConfectionaryPC = Integer.parseInt(ConfectionarySMAP.get("PCs"));


        binding.txtCSDSell.setText("" + CSDSale + "/" + CSDSalePC);
        binding.txtBiscuitSell.setText("" + biscutSale + "/" + biscutSalePc);
        binding.txtWaterSell.setText("" + waterSale + "/" + waterSalePC);
        binding.txtJuiceSell.setText("" + juiceSale + "/" + juiceSalePc);
        binding.txtHamperSell.setText("" + HamperSale + "/" + HamperPC);
        binding.txtConfectionarySell.setText("" + confectionarySale + "/" + ConfectionaryPC);

//        binding.txtCSDSell.setText("" + CSDSale);
//        binding.txtBiscuitSell.setText("" + biscutSale);
//        binding.txtWaterSell.setText("" + waterSale);

        if (mSalesman.getCSDTarget() != null && !mSalesman.getCSDTarget().equalsIgnoreCase(""))
            CSDTarget = Integer.parseInt(mSalesman.getCSDTarget());

        if (mSalesman.getJuiceTarget() != null && !mSalesman.getJuiceTarget().equalsIgnoreCase(""))
            juiceTarget = Integer.parseInt(mSalesman.getJuiceTarget());

        if (mSalesman.getWaterTarget() != null && !mSalesman.getWaterTarget().equalsIgnoreCase(""))
            waterTarget = Integer.parseInt(mSalesman.getWaterTarget());

        if (mSalesman.getBiscutsTarget() != null && !mSalesman.getBiscutsTarget().equalsIgnoreCase(""))
            biscutTarget = Integer.parseInt(mSalesman.getBiscutsTarget());

        if (mSalesman.getHamperTarget() != null && !mSalesman.getHamperTarget().equalsIgnoreCase(""))
            hamperTarget = Integer.parseInt(mSalesman.getHamperTarget());

        if (CSDSale > 0 && CSDTarget > 0) {
            csdPer = (int) ((CSDSale * 100) / Math.round((CSDTarget)));
        }

        if (waterSale > 0 && waterTarget > 0) {
            waterPer = (int) ((waterSale * 100) / Math.round((waterTarget)));
        }

        if (biscutSale > 0 && biscutTarget > 0) {
            biscutPer = (int) ((biscutSale * 100) / Math.round((biscutTarget)));
        }

        if (juiceSale > 0 && juiceTarget > 0) {
            juicePer = (int) ((juiceSale * 100) / Math.round((juiceTarget)));
        }

        if (HamperSale > 0 && hamperTarget > 0) {
            hamperPer = (int) ((HamperSale * 100) / Math.round((hamperTarget)));
        }

        if (confectionarySale > 0 && confectionaryTarget > 0) {
            ConfectionaryPer = (int) ((confectionarySale * 100) / Math.round((confectionaryTarget)));
        }


        binding.progressCSD.setProgressColor(Color.parseColor("#6EACD2"));
        binding.progressCSD.setProgressBackgroundColor(Color.parseColor("#F3F3F3"));
        binding.progressCSD.setMax(100);
        binding.progressCSD.setProgress(csdPer);

        binding.progressJuice.setProgressColor(Color.parseColor("#6EACD2"));
        binding.progressJuice.setProgressBackgroundColor(Color.parseColor("#F3F3F3"));
        binding.progressJuice.setMax(100);
        binding.progressJuice.setProgress(juicePer);

        binding.progressWater.setProgressColor(Color.parseColor("#6EACD2"));
        binding.progressWater.setProgressBackgroundColor(Color.parseColor("#F3F3F3"));
        binding.progressWater.setMax(100);
        binding.progressWater.setProgress(waterPer);

        binding.progressBiscuit.setProgressColor(Color.parseColor("#6EACD2"));
        binding.progressBiscuit.setProgressBackgroundColor(Color.parseColor("#F3F3F3"));
        binding.progressBiscuit.setMax(100);
        binding.progressBiscuit.setProgress(biscutPer);

        binding.progressHamper.setProgressColor(Color.parseColor("#6EACD2"));
        binding.progressHamper.setProgressBackgroundColor(Color.parseColor("#F3F3F3"));
        binding.progressHamper.setMax(100);
        binding.progressHamper.setProgress(hamperPer);

        binding.progressConfectionary.setProgressColor(Color.parseColor("#6EACD2"));
        binding.progressConfectionary.setProgressBackgroundColor(Color.parseColor("#F3F3F3"));
        binding.progressConfectionary.setMax(100);
        binding.progressConfectionary.setProgress(ConfectionaryPer);

        setBarArrData();
        setBardata();

        return view;
    }

    private void setBarArrData() {
        arrBarDat = new ArrayList<ReportBarData>();
        ReportBarData b1 = new ReportBarData();
        b1.setName("7AM");
        b1.setValue(getTimeSales("07:00 AM", "08:00 AM"));
        arrBarDat.add(b1);
        b1 = new ReportBarData();
        b1.setName("8AM");
        b1.setValue(getTimeSales("08:00 AM", "09:00 AM"));
        arrBarDat.add(b1);
        b1 = new ReportBarData();
        b1.setName("9AM");
        b1.setValue(getTimeSales("09:00 AM", "10:00 AM"));
        arrBarDat.add(b1);
        b1 = new ReportBarData();
        b1.setName("10AM");
        b1.setValue(getTimeSales("10:00 AM", "11:00 AM"));
        arrBarDat.add(b1);
        b1 = new ReportBarData();
        b1.setName("11AM");
        b1.setValue(getTimeSales("11:00 AM", "12:00 PM"));
        arrBarDat.add(b1);
        b1 = new ReportBarData();
        b1.setName("12PM");
        b1.setValue(getTimeSales("12:00 PM", "01:00 PM"));
        arrBarDat.add(b1);
        b1 = new ReportBarData();
        b1.setName("1PM");
        b1.setValue(getTimeSales("01:00 PM", "02:00 PM"));
        arrBarDat.add(b1);
        b1 = new ReportBarData();
        b1.setName("2PM");
        b1.setValue(getTimeSales("02:00 PM", "03:00 PM"));
        arrBarDat.add(b1);
        b1 = new ReportBarData();
        b1.setName("3PM");
        b1.setValue(getTimeSales("03:00 PM", "04:00 PM"));
        arrBarDat.add(b1);
        b1 = new ReportBarData();
        b1.setName("4PM");
        b1.setValue(getTimeSales("04:00 PM", "05:00 PM"));
        arrBarDat.add(b1);
        b1 = new ReportBarData();
        b1.setName("5PM");
        b1.setValue(getTimeSales("05:00 PM", "06:00 PM"));
        arrBarDat.add(b1);
        b1 = new ReportBarData();
        b1.setName("6PM");
        b1.setValue(getTimeSales("06:00 PM", "07:00 PM"));
        arrBarDat.add(b1);
        b1 = new ReportBarData();
        b1.setName("7PM");
        b1.setValue(getTimeSales("07:00 PM", "08:00 PM"));
        arrBarDat.add(b1);
    }

    private void setBardata() {
        barEntry = new ArrayList<>();
        barEntryLabels = new ArrayList<String>();

        for (int i = 0; i < arrBarDat.size(); i++) {
            barEntry.add(new BarEntry(Integer.parseInt(arrBarDat.get(i).getValue()), i));
            barEntryLabels.add(arrBarDat.get(i).getName());
        }

        binding.rChart.setVisibility(View.VISIBLE);
        binding.rChart.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        binding.rChart.setDrawBarShadow(false);
        binding.rChart.setDrawValueAboveBar(false);
        binding.rChart.setDescription("");
        binding.rChart.getAxisLeft().setDrawGridLines(false);
        binding.rChart.getXAxis().setDrawGridLines(false);
        binding.rChart.getAxisRight().setEnabled(false);
        binding.rChart.getAxisLeft().setAxisLineColor(ColorTemplate.rgb("#606AA9"));
        binding.rChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.rChart.getXAxis().setAxisLineColor(ColorTemplate.rgb("#606AA9"));
        binding.rChart.setClickable(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        binding.rChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        binding.rChart.setPinchZoom(false);
        binding.rChart.setScaleEnabled(false);
        binding.rChart.setDoubleTapToZoomEnabled(false);
        binding.rChart.setDrawGridBackground(false);
        binding.rChart.setHovered(false);
        binding.rChart.setTouchEnabled(false);

        Bardataset = new BarDataSet(barEntry, "");
        Bardataset.setDrawValues(true);
        Bardataset.setValueTextColor(Color.WHITE);
        barData = new BarData(barEntryLabels, Bardataset);

        Bardataset.setColor(ColorTemplate.rgb("#606AA9"));

        binding.rChart.setData(barData);

        binding.rChart.animateY(2000);
    }

    public String getTimeSales(String minTime, String maxTime) {
        String amtSale = "0";
        double totalSale = db.getTimeslotSales(minTime, maxTime);
        amtSale = "" + Math.round(totalSale);
        return amtSale;
    }

}
