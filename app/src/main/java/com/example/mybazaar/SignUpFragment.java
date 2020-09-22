package com.example.mybazaar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private EditText email;
    private EditText fullName;
    private EditText password;
    private EditText confirmPassword;

    private Button signUpBtn;
    private ImageButton closeBtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseBtn=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_sign_up, container, false);
       alreadyHaveAnAccount =view.findViewById(R.id.tv_already_have_an_acc);
       email=view.findViewById(R.id.sign_up_email);
       fullName=view.findViewById(R.id.sign_up_username);
       password=view.findViewById(R.id.sign_up_password);
       confirmPassword=view.findViewById(R.id.sign_up_confirm_password);

       closeBtn=view.findViewById(R.id.sign_up_close_button);
       signUpBtn=view.findViewById(R.id.sign_up_button);
       progressBar=view.findViewById(R.id.sign_up_progressBar);

       parentFrameLayout=getActivity().findViewById(R.id.register_frame_layout);
       firebaseAuth=FirebaseAuth.getInstance();
       firebaseFirestore=FirebaseFirestore.getInstance();

       if (disableCloseBtn){
           closeBtn.setVisibility(View.GONE);
       }else {
           closeBtn.setVisibility(View.VISIBLE);
       }
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                  checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailAndPassword();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void checkInputs(){
        if (!TextUtils.isEmpty(email.getText())){
            if (!TextUtils.isEmpty(fullName.getText())){
                if (!TextUtils.isEmpty(password.getText()) && password.length() >=8){
                      if (!TextUtils.isEmpty(confirmPassword.getText())){
                          signUpBtn.setEnabled(true);
                          signUpBtn.setTextColor(Color.rgb(255,255,255));
                      }else{
                          signUpBtn.setEnabled(false);
                          signUpBtn.setTextColor(Color.rgb(255,255,255));
                      }
                }else{
                    signUpBtn.setEnabled(false);
                    signUpBtn.setTextColor(Color.rgb(255,255,255));
                }
            }else{
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.rgb(255,255,255));
            }
        }else{
           signUpBtn.setEnabled(false);
           signUpBtn.setTextColor(Color.rgb(255,255,255));
        }
    }
    private void checkEmailAndPassword(){

        Drawable customErrorIcon=getResources().getDrawable(R.drawable.custom_error_icon);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());

          if (email.getText().toString().matches(emailPattern)){
              if (password.getText().toString().equals(confirmPassword.getText().toString())){

                  progressBar.setVisibility(View.VISIBLE);
                  signUpBtn.setEnabled(true);
                  signUpBtn.setTextColor(Color.rgb(255,255,255));
                  firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                              @Override
                              public void onComplete(@NonNull Task<AuthResult> task) {
                                  if (task.isSuccessful()){
                                      Map<String,Object> userdata=new HashMap<>();
                                      userdata.put("fullname",fullName.getText().toString());
                                      firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                              .set(userdata)
                                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<Void> task) {
                                                      if (task.isSuccessful()){

                                                          CollectionReference userDataReference=firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");
                                                          Map<String,Object> cartMap=new HashMap<>();
                                                          cartMap.put("list_size",(long)0);

                                                          Map<String,Object> myAddressesMap=new HashMap<>();
                                                          myAddressesMap.put("list_size",(long)0);

                                                          final List<String> documentNames=new ArrayList<>();
                                                          documentNames.add("MY_CART");
                                                          documentNames.add("MY_ADDRESSES");

                                                          List<Map<String,Object>> documentFields=new ArrayList<>();
                                                          documentFields.add(cartMap);
                                                          documentFields.add(myAddressesMap);

                                                          for (int x=0;x<documentNames.size();x++){
                                                              final int finalX = x;
                                                              userDataReference.document(documentNames.get(x))
                                                                      .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                  @Override
                                                                  public void onComplete(@NonNull Task<Void> task) {
                                                                      if (task.isSuccessful()){
                                                                          if (finalX ==documentNames.size() -1) {
                                                                              intent();
                                                                          }
                                                                      }else{
                                                                          progressBar.setVisibility(View.INVISIBLE);
                                                                          signUpBtn.setEnabled(true);
                                                                          signUpBtn.setTextColor(Color.rgb(255,255,255));
                                                                          String error=task.getException().getMessage();
                                                                          Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  }
                                                              });
                                                          }
                                                      }else{
                                                          String error=task.getException(). getMessage();
                                                          Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                      }
                                                  }
                                              });
                                  }else{
                                      progressBar.setVisibility(View.INVISIBLE);
                                      signUpBtn.setEnabled(true);
                                      signUpBtn.setTextColor(Color.rgb(255,255,255));
                                      String error=task.getException().getMessage();
                                      Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                  }
                              }
                          });
              }else{
                  confirmPassword.setError("Password does not match",customErrorIcon);
              }

          }
          else{
              email.setError("Invalid Email",customErrorIcon);
          }
    }
    private void intent(){
        if (disableCloseBtn){
            disableCloseBtn=false;
        }else {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        }
        getActivity().finish();
    }
}