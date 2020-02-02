package com.ashish.mymall;


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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment {

    private EditText email;
    private TextView msg,goBack;
    private Button resetPassBtn;
    private FrameLayout parentframeLayout;

    private FirebaseAuth firebaseAuth;


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_forgot_password, container, false);

        email=view.findViewById(R.id.forgot_pass_email);
        resetPassBtn=view.findViewById(R.id.reset_pass_button);
        msg=view.findViewById(R.id.forgot_pass_msg);
        goBack=view.findViewById(R.id.forgot_pass_go_back);
        parentframeLayout=getActivity().findViewById(R.id.reg_frame_layout);
        firebaseAuth=FirebaseAuth.getInstance();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SigninFragment());
            }
        });

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetPassBtn.setEnabled(false);

                firebaseAuth.sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                msg.setVisibility(View.VISIBLE);
                            }else {
                                String error=task.getException().getMessage();
                                Toast.makeText(getActivity(), error,Toast.LENGTH_SHORT).show();
                            }
                            resetPassBtn.setEnabled(true);
                        }
                    });

            }
        });

    }


    private void checkInputs() {
        if(!TextUtils.isEmpty(email.getText())) {
            resetPassBtn.setEnabled(true);
        }
        else{
            resetPassBtn.setEnabled(false);
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentframeLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}
