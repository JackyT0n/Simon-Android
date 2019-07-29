package com.ton.jacky.simon;

/**
 * Created by Jacky on 11/30/2017.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;


public class MainMenu extends AppCompatActivity implements Observer
{
    // Private Vars
    Model mModel;
    Button start;

    /**
     * OnCreate
     * -- Called when application is initially launched.
     *    @see <a href="https://developer.android.com/guide/components/activities/activity-lifecycle.html">Android LifeCycle</a>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.main_menu);

        // Get Model instance
        mModel = Model.getInstance();
        mModel.init(4, 2);
        mModel.addObserver(this);
        mModel.initObservers();
        // Get button reference.
        start = (Button) findViewById(R.id.startbutton);

        // set function to allow user to move to MainActivity
        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainMenu.this, MainActivity.class);

                // Start activity
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    //when returning from settings option update game info
    public void onResume(){
        super.onResume();
        setDifficulty();
        setButtons();
        //notify that settings have changed
        mModel.initObservers();
    }
    //retrieve the buttons setting from Preference Manager
    public void setButtons(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int numButtons = Integer.parseInt(prefs.getString("buttonsettings", "4"));
        mModel.setNumButtons(numButtons);



    }
    //retrieve the difficulty setting from Preference Manager
    public void setDifficulty(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int difficulty = Integer.parseInt(prefs.getString("difficultysettings", "2"));
        mModel.setDifficulty(difficulty);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Options Menu
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view1, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle interaction based on item selection
        switch (item.getItemId())
        {
            //case to move to settingsActivity
            case R.id.setting:
                // Create Intent to launch second activity
                Intent intent = new Intent(this, SettingsActivity.class);

                // Start activity
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg)
    {
        setDifficulty();
        setButtons();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mModel.deleteObserver(this);
    }

}