package nuttapon.dots.co.th.dotssolutions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class MainFragment extends Fragment{


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Login Controller

        loginController();

    } //Main Method

    private void loginController() {
        Button button = getView().findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText editText = getView().findViewById(R.id.edtIdCustomer);
                String idCustomerString = editText.getText().toString().trim();

                MyAlert myAlert = new MyAlert(getActivity());
                MyConstant myConstant = new MyConstant();
                String[] columnStrings = myConstant.getColumnTcust();
                String[] UserLoginStrings = new String[columnStrings.length];

                if (idCustomerString.isEmpty()){
                 //   Have Space
                    myAlert.normalDialog("Have Space",
                            "Please Fill ID Customer");
                } else if (idCustomerString.length() != 11){
                    myAlert.normalDialog("ID Customer False",
                            "Please Fill ID Customer only 11 Digit");

                } else {


                    try {

                        GetUserWhereIdCustomer getUserWhereIdCustomer = new GetUserWhereIdCustomer(getActivity());
                        getUserWhereIdCustomer.execute(idCustomerString, myConstant.getUrlGetUserWhereCustID());

                        String jsonString = getUserWhereIdCustomer.get();
                        Log.d("3SepV1", "json ==>" + jsonString);

                        if (jsonString.equals("null")){
                            myAlert.normalDialog("No ID Customer",
                                    "No" + idCustomerString + "in my database");

                        } else {

                            SharedPreferences sharedPreferences = getActivity()
                                    .getSharedPreferences("MyData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("User", jsonString);
                            editor.commit();

                            startActivity(new Intent(getActivity(), ServiceActivity.class));
                            getActivity().finish();

                        }


                    } catch (Exception e){
                        e.printStackTrace();
                    }


                } //if

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }
}