package com.buddybuild.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.buddybuild.BuddyBuildApplication;
import com.buddybuild.Coordinator;
import com.buddybuild.R;
import com.buddybuild.ui.view.BuddyBuildWhiteButton;
import com.buddybuild.utils.ObservableUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LoginFragment extends Fragment {

    @Inject
    protected Coordinator coordinator;

    @BindView(R.id.fragment_login_email_edittext)
    protected EditText emailEditText;
    @BindView(R.id.fragment_login_password_edittext)
    protected EditText passwordEditText;
    @BindView(R.id.fragment_login_button)
    protected BuddyBuildWhiteButton loginButton;
    @BindView(R.id.progress_indicator)
    protected View progressIndicator;
    @BindView(R.id.fragment_login_email_til)
    TextInputLayout emailTextInputLayout;
    @BindView(R.id.fragment_login_password_til)
    TextInputLayout passwordTextInputLayout;

    private Unbinder unbinder;

    private TextWatcher validateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            emailTextInputLayout.setError(null);
            passwordTextInputLayout.setError(null);
            validate();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private ProgressIndicatorDelegate progressIndicatorDelegate;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        BuddyBuildApplication application = (BuddyBuildApplication) context.getApplicationContext();
        application.getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);

        emailEditText.addTextChangedListener(validateTextWatcher);
        passwordEditText.addTextChangedListener(validateTextWatcher);
        validate();

        progressIndicatorDelegate = new ProgressIndicatorDelegate(getContext());
        progressIndicatorDelegate.setProgressIndicator(progressIndicator);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fragment_login_button)
    protected void onLoginClicked() {

        progressIndicatorDelegate.fadeInProgress();

        ObservableUtils.zipWithTimer(
                coordinator.login(emailEditText.getText().toString(), passwordEditText.getText().toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> progressIndicatorDelegate.fadeOutProgress(() -> {
                            switch (result) {
                                case SUCCESS:
                                    Intent intent = OverviewActivity.newIntent(getContext());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    break;

                                case UNKNOWN_EMAIL:
                                    emailTextInputLayout.setError("unknown email");
                                    break;

                                case EMAIL_PASSWORD_MISMATCH:
                                    passwordTextInputLayout.setError("email and pw don't match");
                                    break;

                                case THROTTLE_LIMIT:
                                    emailTextInputLayout.setError("too many attempts, please wait");
                                    break;

                                case UNKNOWN_FAILURE:
                                    // todo generic dialog?
                                    break;

                                default:
                                    throw new RuntimeException("unknown result state: " + result);
                            }
                        }),
                        throwable -> {
                            Timber.w(throwable.getMessage());
                            progressIndicatorDelegate.fadeOutProgress(() -> {
                                // todo generic dialog?
                            });
                        });
    }

    private void validate() {
        if (emailEditText.getText().toString().isEmpty()
                || passwordEditText.getText().toString().isEmpty()) {
            loginButton.setEnabled(false);
            return;
        }

        loginButton.setEnabled(true);
    }
}
