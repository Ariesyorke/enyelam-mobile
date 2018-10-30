package com.nyelam.android;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Aprilian Nur Wakhid Daini on 4/3/2018.
 */

public abstract class BasicFragment extends Fragment {

    protected ProgressDialog pDialog;
    protected boolean isDrag = true, isEnableAutoLoadMore = true, status_progress = false;
    protected MyRecyclerViewInterface myRecyclerViewInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        injectViews(view);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please wait..");
    }

    /**
     * Every fragment has to inflate a layout in the onCreateView method. We have added this method to
     * avoid duplicate all the inflate code in every fragment. You only have to return the layout to
     * inflate in this method when extends BaseFragment.
     */
    protected abstract int getFragmentLayout();

    /**
     * Replace every field annotated using @Inject annotation with the provided dependency specified
     * inside a Dagger module value.
     */
    /*private void injectDependencies() {
        ((BaseActivity) getActivity()).bind(this);
    }*/

    /**
     * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
     * value.
     *
     * @param view to extract each widget injected in the fragment.
     */
    private void injectViews(final View view) {
        ButterKnife.bind(this, view);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //injectDependencies();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface MyRecyclerViewInterface {
        public void loadMore();
    }

    protected void showDialog(){
        if(pDialog != null) pDialog.show();
    }

    protected void dismissDialog(){
        if(pDialog != null) pDialog.dismiss();
    }

}
