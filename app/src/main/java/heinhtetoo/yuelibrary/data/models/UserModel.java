package heinhtetoo.yuelibrary.data.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import heinhtetoo.yuelibrary.R;
import heinhtetoo.yuelibrary.activities.UserAccountActivity;
import heinhtetoo.yuelibrary.data.vos.UserVO;
import heinhtetoo.yuelibrary.utils.PrefUtils;

/**
 * Created by Hein Htet Oo on 11/29/2017.
 */

public class UserModel {

    private static final String TAG = "UserAccountActivity";

    public static final String LIB_USER = "users";

    private static UserModel objInstance;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUserDr;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private UserVO mUser;

    private UserModel() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mUserDr = mDatabaseReference.child(LIB_USER);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    public static UserModel getInstance() {
        if (objInstance == null) {
            objInstance = new UserModel();
        }
        return objInstance;
    }

    public boolean isUserSignIn() {
        return mUser != null;
    }

    public void firebaseAuthWithGoogle(final GoogleSignInAccount acct,
                                       final SignInWithGoogleAccountDelegate delegate) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        delegate.showProgressDialog("Authenticating");

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            checkExistingJobPoster(acct, delegate);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            delegate.onFailureSignIn(task.getException().getMessage());
                        }

                        delegate.hideProgressDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        delegate.onFailureSignIn(e.getMessage());
                    }
                });
    }

    private void checkExistingJobPoster(final GoogleSignInAccount signInAccount,
                                        final SignInWithGoogleAccountDelegate delegate) {
        //String formattedEmail = FirebaseUtils.replaceDotsWithCommas(signInAccount.getEmail());
        DatabaseReference singleAccountDR = mUserDr.child(signInAccount.getId());
        singleAccountDR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserVO user = dataSnapshot.getValue(UserVO.class);
                if (user != null) {
                    delegate.onSuccessSignIn(user);
                    mUser = user;
                } else {
                    Uri photoUri = signInAccount.getPhotoUrl();
                    String photoUrl = (photoUri != null) ? photoUri.toString() : null;

                    UserVO newUser = new UserVO(signInAccount.getId(), signInAccount.getDisplayName(),
                            signInAccount.getEmail(), photoUrl,
                            null, null, null, null);
                    registerNewUser(newUser);
                    delegate.onSuccessSignIn(newUser);
                    mUser = newUser;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                delegate.onFailureSignIn(databaseError.getMessage());
            }
        });
    }

    public void syncJobPosterInfo(String accountId, final SyncUserInfoDelegate delegate) {
        DatabaseReference singleAccountDR = mUserDr.child(accountId);
        singleAccountDR.keepSynced(true);
        singleAccountDR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserVO user = dataSnapshot.getValue(UserVO.class);
                if (user != null) {
                    delegate.syncUserInfo(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void signOut(GoogleApiClient mGoogleApiClient, final LogoutDelegate delegate) {
        mFirebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        mUser = null;
                        delegate.onLogoutSuccess();
                    }
                });
    }

    public void registerNewUser(UserVO user) {
        mUserDr.child(String.valueOf(user.getAccountId())).setValue(user);
    }

    public void saveAccountIdInPref(Context context, UserVO userVO) {
        SharedPreferences.Editor editor = PrefUtils.getSharedPrefs(context).edit();
        editor.putString(context.getString(R.string.account_id_key), userVO.getAccountId());
        editor.apply();

    }

    public String getAccountIdFromPref(Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPrefs(context);
        return sharedPreferences.getString(context.getString(R.string.account_id_key), null);
    }

    public void removeAccountIdFromPref(Context context) {
        SharedPreferences.Editor editor = PrefUtils.getSharedPrefs(context).edit();
        editor.putString(context.getString(R.string.account_id_key), null);
        editor.apply();
    }

    public void loadUserFromPref(String id) {
        mUserDr.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserVO user = dataSnapshot.getValue(UserVO.class);
                if (user != null) {
                    mUser = user;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public UserVO getUser() {
        return mUser;
    }

    public interface SignInWithGoogleAccountDelegate {
        void onSuccessSignIn(UserVO user);

        void onFailureSignIn(String msg);

        void showProgressDialog(String msg);

        void hideProgressDialog();
    }

    public interface LogoutDelegate {
        void onLogoutSuccess();
    }

    public interface SyncUserInfoDelegate {
        void syncUserInfo(UserVO user);
    }
}
