package com.example.mydesign.menu;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mydesign.OpenScreen;
import com.example.mydesign.R;
import com.example.mydesign.UserCartShopping;
import com.example.mydesign.UserSignIn;
import com.example.mydesign.UserSignUp;
import com.google.firebase.auth.FirebaseAuth;

public class UserMenu extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.setting:
                System.out.println("setting");
                return true;
            case R.id.cart:
                startActivity(new Intent(UserMenu.this, UserCartShopping.class));
                return true;
            case R.id.exit:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(UserMenu.this, "Logged Out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserMenu.this, OpenScreen.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
