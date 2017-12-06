package heinhtetoo.yuelibrary.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heinhtetoo.yuelibrary.BuildConfig;
import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.data.models.UserModel;
import heinhtetoo.yuelibrary.data.vos.UserVO;
import heinhtetoo.yuelibrary.utils.MMFontUtils;

public class UserAccountActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "UserAccountActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private GoogleApiClient mGoogleApiClient;

    public ProgressDialog mProgressDialog;

    private UserModel mUserModel;

    @Bind(R.id.iv_back)
    ImageView ivBack;

    @Bind(R.id.iv_profile_pic)
    ImageView ivProfile;

    @Bind(R.id.tv_user_name)
    TextView tvUserName;

    @Bind(R.id.tv_email)
    TextView tvEmail;

    @Bind(R.id.tv_phone)
    TextView tvPhone;

    @Bind(R.id.tv_reserved_book_count)
    TextView tvReserved;

    @Bind(R.id.tv_published_story_count)
    TextView tvPublished;

    @Bind(R.id.btn_google_login)
    Button btnLogin;

    @Bind(R.id.btn_logout)
    Button btnLogout;

    @Bind(R.id.layout_user_head)
    RelativeLayout rlHead;

    @Bind(R.id.profile_holder)
    CardView cvProfile;

    @Bind(R.id.email_holder)
    LinearLayout layoutEmailHolder;

    @Bind(R.id.phone_holder)
    LinearLayout layoutPhoneHolder;

    @Bind(R.id.divider_1)
    TextView tvDivider1;

    @Bind(R.id.divider_2)
    TextView tvDivider2;

    @Bind(R.id.dashboard_holder)
    LinearLayout rlDashboard;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, UserAccountActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        ButterKnife.bind(this, this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.Google_Sign_In_Id_Token)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        mAuth = FirebaseAuth.getInstance();

        mUserModel = UserModel.getInstance();
    }

    @OnClick(R.id.btn_google_login)
    public void onClickGoogleLogin() {
        signIn();
    }

    @OnClick(R.id.btn_logout)
    public void onClickLogout() {
        showConfirmLogoutDialog();
    }

    private void showConfirmLogoutDialog() {

        String confirmMsg = getResources().getString(R.string.confirm_logout);

        new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setMessage(confirmMsg)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        signOut();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mUserModel.isUserSignIn()) {
            showProgress(getString(R.string.file_loading));
            mUserModel.syncUserInfo(mUserModel.getAccountIdFromPref(this), new UserModel.SyncUserInfoDelegate() {
                @Override
                public void syncUserInfo(UserVO user) {
                    updateUser(user);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            processGoogleSignInResult(result);
        }
    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void processGoogleSignInResult(GoogleSignInResult signInResult) {
        if (signInResult.isSuccess()) {
            // Google Sign-In was successful, authenticate with Firebase
            final GoogleSignInAccount account = signInResult.getSignInAccount();
            UserModel.getInstance().firebaseAuthWithGoogle(account, new UserModel.SignInWithGoogleAccountDelegate() {
                @Override
                public void onSuccessSignIn(UserVO user) {
                    updateUser(user);
                    UserModel.getInstance().saveAccountIdInPref(UserAccountActivity.this, user);
                }

                @Override
                public void onFailureSignIn(String msg) {
                    updateUser(null);
                }

                @Override
                public void showProgressDialog(String msg) {
                    showProgress(msg);
                }

                @Override
                public void hideProgressDialog() {
                    hideProgress();
                }
            });
        } else {
            Log.e(TAG, "Google Sign-In failed.");
            Snackbar.make(tvUserName, "Your Google sign-in failed.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void signOut() {
        mUserModel.signOut(mGoogleApiClient, new UserModel.LogoutDelegate() {
            @Override
            public void onLogoutSuccess() {
                mUserModel.removeAccountIdFromPref(UserAccountActivity.this);
                updateUser(null);
            }
        });
    }

    private void updateUser(UserVO user) {
        if (user != null) {
            Glide.with(getApplicationContext())
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.manga_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivProfile);
            tvUserName.setText(MMFontUtils.mmTextUnicodeOrigin(user.getDisplayName()));
            tvEmail.setText(user.getEmail());
            tvPhone.setText(user.getPhone());
            tvReserved.setText(String.valueOf(user.getReservedBookList().size()));
            tvPublished.setText(String.valueOf(user.getPublishedStoryList().size()));

            ivProfile.setVisibility(View.VISIBLE);
            tvUserName.setVisibility(View.VISIBLE);

            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);

            rlHead.setBackground(getResources().getDrawable(R.drawable.triangle_background));
            cvProfile.setVisibility(View.VISIBLE);
            layoutEmailHolder.setVisibility(View.VISIBLE);
            layoutPhoneHolder.setVisibility(View.VISIBLE);
            tvDivider1.setVisibility(View.VISIBLE);
            tvDivider2.setVisibility(View.VISIBLE);
            rlDashboard.setVisibility(View.VISIBLE);
            hideProgress();
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);

            rlHead.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primary));
            cvProfile.setVisibility(View.GONE);
            layoutEmailHolder.setVisibility(View.GONE);
            layoutPhoneHolder.setVisibility(View.GONE);
            tvDivider1.setVisibility(View.GONE);
            tvDivider2.setVisibility(View.GONE);
            rlDashboard.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(UserAccountActivity.this);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
