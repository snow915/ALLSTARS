package com.store;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;

import java.util.HashMap;
import java.util.Map;

public class Pay extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    String API_GET_TOKEN="http://52.201.229.104/braintree_allstars/main.php";
    String API_CHECKOUT="http://52.201.229.104/braintree_allstars/checkout.php";

    String token,amount;
    HashMap<String,String> paramsHash;
    Button btn_pay;
    EditText edit_amount;
    LinearLayout group_payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        edit_amount=(EditText)findViewById(R.id.edit_amount);
        btn_pay=(Button) findViewById(R.id.btn_pay);
        group_payment=(LinearLayout)findViewById(R.id.payment_group);

        new Pay.getToken().execute();

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPayment();
            }
        });

    }

    private void submitPayment(){
        String payValue=edit_amount.getText().toString();
        if(!payValue.isEmpty()) {
            DropInRequest dropInRequest=new DropInRequest().clientToken(token);
            startActivityForResult(dropInRequest.getIntent(getApplicationContext()),REQUEST_CODE);
        } else {
            Toast.makeText(getApplicationContext(), "Enter a valid amount for payment", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendPayments(){
        RequestQueue queue= Volley.newRequestQueue(Pay.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, API_CHECKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.toString().contains("Successful")){
                            Toast.makeText(Pay.this, "Payment Success", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Pay.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("Response",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Err",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(paramsHash==null)
                    return null;
                Map<String,String> params=new HashMap<>();
                for(String key:paramsHash.keySet())
                {
                    params.put(key,paramsHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RetryPolicy mRetryPolicy=new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        queue.add(stringRequest);
    }

    private class getToken extends AsyncTask {
        ProgressDialog mDailog;

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient client = new HttpClient();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(final String responseBody) {
                    mDailog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            group_payment.setVisibility(View.VISIBLE);
                            token = responseBody;
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    mDailog.dismiss();
                    Log.d("Err", exception.toString());
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDailog=new ProgressDialog(Pay.this,android.R.style.Theme_DeviceDefault_Light_Dialog);
            mDailog.setCancelable(false);
            mDailog.setMessage("Loading Wallet, Please Wait");
            mDailog.show();
        }

        @Override
        protected void onPostExecute(Object o){
            super.onPostExecute(o);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode== REQUEST_CODE){
            if(resultCode==RESULT_OK)
            {
                DropInResult result=data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce= result.getPaymentMethodNonce();
                String strNounce=nonce.getNonce();
                if(!edit_amount.getText().toString().isEmpty())
                {
                    amount=edit_amount.getText().toString();
                    paramsHash=new HashMap<>();
                    paramsHash.put("amount",amount);
                    paramsHash.put("nonce",strNounce);

                    sendPayments();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
            else if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(), "User canceled", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Exception error=(Exception)data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("Err",error.toString());
            }
        }
    }
}
