package thiago.com.bre;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Thiago on 12/02/17.
 */

public class Detail {

    private String id;
    private String isHeart;
    private String imagePath;
    private String phrase;

    public Detail(){

    }

    public Detail(String id, String isHeart, String imagePath, String phrase) {
        this.id = id;
        this.isHeart = isHeart;
        this.imagePath = imagePath;
        this.phrase = phrase;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsHeart() {
        return isHeart;
    }

    public void setIsHeart(String isHeart) {
        this.isHeart = isHeart;

    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    @Override
    public String toString() {
        return phrase+isHeart+imagePath;
    }
}
