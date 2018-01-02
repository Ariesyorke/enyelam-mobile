package com.danzoye.lib.auth.google;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.oxoneindonesia.android.oxone.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class DGoogleActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String RESULT_EXTRA_ERROR = "error";
    public static final String RESULT_EXTRA_DATA = "data";
    private static final int REQ_CODE_SIGN_IN = 0;
    private GoogleApiClient gApiClient;
    private boolean mIntentInProgress;
    private GPlusAuthResult result;
    @Override
    public void onConnected(Bundle bundle) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gApiClient);
        startActivityForResult(signInIntent, REQ_CODE_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIntentInProgress && connectionResult.hasResolution()) {
            Log.e("danzoye", "no intent in progress and has resolution");
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(connectionResult.getResolution().getIntentSender(),
                        REQ_CODE_SIGN_IN, null, 0, 0, 0);
                mIntentInProgress = false;
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
                Log.e("danzoye", e.toString());
                mIntentInProgress = false;
                Intent data = new Intent();
                data.putExtra(RESULT_EXTRA_ERROR, e);
                setResult(RESULT_FIRST_USER, data);
                finish();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Log.d("danzoye", "on connection suspend = " + (i == CAUSE_NETWORK_LOST ? "network lost" : "service disconnected"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SIGN_IN) {
//            mIntentInProgress = false;
//
//            if (!gApiClient.isConnecting()) {
//                gApiClient.connect();
//            }
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult r) {
        Log.d("Gokleen", "handleSignInResult:" + r.isSuccess());
        /*if (r.isSuccess()) {
            GoogleSignInAccount acct = r.getSignInAccount();
            result = new GPlusAuthResult();
            result.idToken = acct.getIdToken();
            result.authCode = acct.getServerAuthCode();

            Plus.PeopleApi.load(gApiClient, acct.getId()).setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
                @Override
                public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {
                    Exception error = null;
                    Person current = loadPeopleResult.getPersonBuffer().get(0);
                    if (current == null) {
                        error = new NullPointerException("Unable to retrieve current user.");
                        Intent data = new Intent();
                        data.putExtra(RESULT_EXTRA_ERROR, error.toString());
                        setResult(RESULT_OK, data);
                        finish();
                        return;
                    }

                    if (current.hasBirthday()) {
                        try {
                            result.birthday = new SimpleDateFormat("yyyy-MM-dd").parse(current.getBirthday());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            // Log.e("danzoye", e.toString());
                        }
                    }
                    if (current.hasCurrentLocation()) {
                        result.locationName = current.getCurrentLocation();
                    }
                    if (current.hasGender()) {
                        result.gender = current.getGender() == Person.Gender.FEMALE ? GPlusAuthResult.FEMALE : GPlusAuthResult.MALE;
                    }
                    if (current.hasDisplayName()) {
                        result.name = current.getDisplayName();
                    }
                    if (current.hasName() && current.getName().hasFamilyName()) {
                        result.lastName = current.getName().getFamilyName();
                    }
                    if (current.hasName() && current.getName().hasGivenName()) {
                        result.firstName = current.getName().getGivenName();
                    }
                    if (current.hasId()) {
                        result.id = current.getId();
                    }
                    if (current.hasImage() && current.getImage().hasUrl()) {
                        result.profilePictureUrl = current.getImage().getUrl();
                    }
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_EXTRA_DATA, result.toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gApiClient);
            startActivityForResult(signInIntent, REQ_CODE_SIGN_IN);
        }*/

        if (r.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = r.getSignInAccount();
            result = new GPlusAuthResult();
            result.accessToken = acct.getIdToken();
            result.authCode = acct.getServerAuthCode();
            result.id = acct.getId();
            result.email = acct.getEmail();
            result.firstName = acct.getGivenName();
            result.lastName = acct.getFamilyName();
            result.profilePictureUrl = acct.getPhotoUrl().toString();

            Intent intent = new Intent();
            intent.putExtra(RESULT_EXTRA_DATA, result.toString());
            setResult(RESULT_OK, intent);
            finish();

        } else {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gApiClient);
            startActivityForResult(signInIntent, REQ_CODE_SIGN_IN);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dgoogle);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getResources().getString(R.string.alternative_client_id))
                .requestIdToken(getResources().getString(R.string.alternative_client_id))
                .requestEmail()
                .build();


        gApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gApiClient);
        startActivityForResult(signInIntent, REQ_CODE_SIGN_IN);

    }

    /*protected AsyncTask<GPlusAuthResult, Void, GPlusAuthResult> onGetAccessTokenAndUserData() {
        return new AsyncTask<GPlusAuthResult, Void, GPlusAuthResult>() {
            private GPlusAuthResult result;
            private Exception error;

            @Override
            protected GPlusAuthResult doInBackground(GPlusAuthResult... params) {
                // Log.d("danzoye", "try get access token");
                result = params[0];
                try {
                    if (ActivityCompat.checkSelfPermission(DGoogleActivity.this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }
                    String accessToken = GoogleAuthUtil.getToken(DGoogleActivity.this,
                            Plus.AccountApi.getAccountName(gApiClient),
                            "oauth2:" + TextUtils.join(" ", SCOPES));
                    // Log.d("danzoye", "acccess token = " + accessToken);
                    result.accessToken = accessToken;
                } catch (IOException transientEx) {
                    transientEx.printStackTrace();
                    // Log.e("danzoye", transientEx.toString());
                    error = transientEx;
                } catch (UserRecoverableAuthException e) {
                    e.printStackTrace();
                    // Log.e("danzoye", e.toString());
                    error = e;
                } catch (GoogleAuthException authEx) {
                    authEx.printStackTrace();
                    // Log.e("danzoye", authEx.toString());
                    error = authEx;
                } catch (Exception e) {
                    e.printStackTrace();
                    // Log.e("danzoye", e.toString());
                    throw new RuntimeException(e);
                }

                if (result.accessToken == null) {
                    return null;
                }

                result.email = Plus.AccountApi.getAccountName(gApiClient);

                // Log.d("danzoye", "try get person data");

                Person current = Plus.PeopleApi.getCurrentPerson(gApiClient);
                if (current == null) {
                    error = new NullPointerException("Unable to retrieve current user.");
                    return null;
                }

                if (current.hasBirthday()) {
                    try {
                        result.birthday = new SimpleDateFormat("yyyy-MM-dd").parse(current.getBirthday());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        // Log.e("danzoye", e.toString());
                    }
                }
                if (current.hasCurrentLocation()) {
                    result.locationName = current.getCurrentLocation();
                }
                if (current.hasGender()) {
                    result.gender = current.getGender() == Person.Gender.FEMALE ? GPlusAuthResult.FEMALE : GPlusAuthResult.MALE;
                }
                if (current.hasDisplayName()) {
                    result.name = current.getDisplayName();
                }
                if (current.hasName() && current.getName().hasFamilyName()) {
                    result.lastName = current.getName().getFamilyName();
                }
                if (current.hasName() && current.getName().hasGivenName()) {
                    result.firstName = current.getName().getGivenName();
                }
                if (current.hasId()) {
                    result.id = current.getId();
                }
                if (current.hasImage() && current.getImage().hasUrl()) {
                    result.profilePictureUrl = current.getImage().getUrl();
                }

                return result;
            }

            @Override
            protected void onPostExecute(GPlusAuthResult result) {
                super.onPostExecute(result);
                if (result != null) {
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_EXTRA_DATA, result.toString());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Intent data = new Intent();
                    data.putExtra(RESULT_EXTRA_ERROR, error.toString());
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        };
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        //gApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (gApiClient.isConnected()) {
            gApiClient.disconnect();
        }
    }


}
