package thiago.com.bre;

import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView detailImageView, heartImageView, moreImageView;
    ProgressBar progressBar;
    TextView txtMessage;

    private DatabaseReference mDatabase;
    private List<Detail> imagePaths = new ArrayList<>();
    private Detail currentImageDetail;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heartImageView = (ImageView) findViewById(R.id.heartImageView);
        moreImageView = (ImageView) findViewById(R.id.moreImageView);
        detailImageView = (ImageView) findViewById(R.id.detailImageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtMessage = (TextView) findViewById(R.id.message);

        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/CookieMonster.ttf");
        txtMessage.setTypeface(typeface);

        storageRef = storage.getReferenceFromUrl("gs://intense-heat-6355.appspot.com");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("imageDetail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    imagePaths.add(data.getValue(Detail.class));
                }

                chooseImageDetail();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        moreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageDetail();

            }
        });

        heartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHeart();
            }
        });
    }

    private void downloadImage(int rnd){
        progressBar.setVisibility(View.VISIBLE);

        storageRef.child(imagePaths.get(rnd).getImagePath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                progressBar.setVisibility(View.INVISIBLE);

                Picasso.with(MainActivity.this)
                        .load(uri).error(R.drawable.bremenos)
                        .placeholder(R.drawable.bremenos)
                        .into(detailImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }


    private void chooseImageDetail(){
        int rnd =  getRandom();
        currentImageDetail = imagePaths.get(rnd);
        downloadImage(rnd);
        populateView();
    }

    private void updateHeart(){
        if(currentImageDetail.getIsHeart().equals("true"))
            currentImageDetail.setIsHeart("false");
        else
            currentImageDetail.setIsHeart("true");

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("imageDetail")
                .child(currentImageDetail.getId())
                .child("isHeart")
                .setValue(currentImageDetail.getIsHeart());


        toggleHeart();

    }

    private void toggleHeart(){

        if ((currentImageDetail != null) && (currentImageDetail.getIsHeart().equals("true")))
            heartImageView.setImageResource(R.drawable.heart);
        else
            heartImageView.setImageResource(R.drawable.heartoutline);
    }

    private void populateView(){
        toggleHeart();
        txtMessage.setText(currentImageDetail.getPhrase());
    }

    private int getRandom(){
        Random r = new Random();
        return r.nextInt(imagePaths.size());
    }
}
