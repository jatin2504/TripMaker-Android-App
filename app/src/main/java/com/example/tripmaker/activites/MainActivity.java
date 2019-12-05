package com.example.tripmaker.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tripmaker.R;
import com.example.tripmaker.fragments.LoginFragment;
import com.example.tripmaker.fragments.RegistrationFragment;
import com.example.tripmaker.models.Gender;
import com.example.tripmaker.models.JoinedTrip;
import com.example.tripmaker.models.Trip;
import com.example.tripmaker.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RegistrationFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener {

    public static FirebaseAuth mAuth;
    private final int RC_SIGN_IN = 10001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore db;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        testing chat activity
//        Intent i1 = new Intent(MainActivity.this, ChatActivity.class);
//        startActivity(i1);
//        finish();
        pb = findViewById(R.id.mainActivityPB);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        if (mAuth.getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, new LoginFragment(), "f_login").commit();

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        } else {
            Intent i = new Intent(MainActivity.this, DashboardActivity.class);
            pb.setVisibility(View.INVISIBLE);
            startActivity(i);
            finish();
        }

    }


    @Override
    public void onFragmentChange(String state) {
        if (state == RegistrationFragment.FRAGMENT_STATE) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, new LoginFragment(), "f_login").commit();
        } else if (state == LoginFragment.FRAGMENT_STATE) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, new RegistrationFragment(), "f_registration").commit();
        }
    }

    @Override
    public void loginUser(final User user) {
        if(pb.getVisibility() == View.VISIBLE)
            return;
        pb.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("MainActivity", "signInWithEmail:success");
                            existsUser(user);
                        } else {
                            pb.setVisibility(View.INVISIBLE);
                            Log.w("MainActivity", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void signInWithGoogle() {
        if(pb.getVisibility() == View.VISIBLE)
            return;
        pb.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void storeUserInSharedPred(User user) {
        SharedPreferences preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("userObj", json);
        prefsEditor.commit();
    }

    private void existsUser(final User user) {
        CollectionReference reference = db.collection("users");
        Query query = reference.whereEqualTo("email", user.getEmail());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        Log.d("MainActivity", "User already exists");
                        User user = new User();
                        List<JoinedTrip> joinedTrips = new ArrayList<>();
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        user.setId(documentSnapshot.getId());
                        user.setLastName((documentSnapshot.getData().get("lastName") != null) ? documentSnapshot.getData().get("lastName").toString() : "");
                        user.setEmail((documentSnapshot.getData().get("email") != null) ? documentSnapshot.getData().get("email").toString() : "");
                        user.setFirstName((documentSnapshot.getData().get("firstName") != null) ? documentSnapshot.getData().get("firstName").toString() : "");
                        user.setGender((documentSnapshot.getData().get("gender") != null) ? Gender.valueOf((String) documentSnapshot.getData().get("gender")) : null);
                        user.setImageUrl((documentSnapshot.getData().get("imageUrl") != null) ? documentSnapshot.getData().get("imageUrl").toString() : null);
                        for (HashMap<String, Object> l1 : (ArrayList<HashMap<String, Object>>) documentSnapshot.getData().get("trips")) {
                            JoinedTrip jt1 = new JoinedTrip();
                            jt1.setTripId(l1.get("tripId").toString());
                            jt1.setJoinedDate((Timestamp) l1.get("joinedDate"));
                            joinedTrips.add(jt1);
                        }
                        user.setTrips(joinedTrips);
                        storeUserInSharedPred(user);
                        Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(i);
                        pb.setVisibility(View.INVISIBLE);
                        finish();
                    } else {
                        saveUserData(user);
                    }

                } else {
                    pb.setVisibility(View.INVISIBLE);
                    Log.d("MainActivity", "Error getting documents: ", task.getException());
                }
            }
        });

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("MainActivity", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        pb.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("MainActivity", "Google signIn:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            User user = new User();
                            user.setEmail(firebaseUser.getEmail());
                            user.setFirstName(firebaseUser.getDisplayName().split(" ")[0]);
                            user.setLastName(firebaseUser.getDisplayName().split(" ")[1]);
                            existsUser(user);
                        } else {
                            Log.w("MainActivity", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onClickRegister(final User user) {
        if(pb.getVisibility() == View.VISIBLE)
            return;
        pb.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("MainActivity", "createUserWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            saveUserData(user);
                        } else {
                            Log.w("MainActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void saveUserData(final User user) {
        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                user.setId(documentReference.getId());
                storeUserInSharedPred(user);
                Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                pb.setVisibility(View.INVISIBLE);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("MainActivity", "Google sign in failed", e);
            }
        }
    }
}
